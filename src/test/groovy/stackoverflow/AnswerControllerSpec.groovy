package stackoverflow

import grails.test.mixin.*
import spock.lang.*

@TestFor(AnswerController)
@Mock([User, Post, Answer, Question])
class AnswerControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null

        def user = new User(username: 'me', password: 'password', email: 'test.user@domain.com')

        def question = new Question(text: "Everything is said in the title : what is the meaning of life ?",
                vote: 0,
                created: new Date(),
                edited: new Date(),
                title: "What is the meaning of life ?",
                views: 0,
                resolved: false,
                user: user)

        params["text"] = "test"
        params["vote"] = 2
        params["created"] = new Date()
        params["edited"] = new Date()
        params["user"] = user
        params["question"] = question
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.answer!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def answer = new Answer()
            answer.validate()
            controller.save(answer)

        then:"The create view is rendered again with the correct model"
            model.answer!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            answer = new Answer(params)

            controller.save(answer)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/answer/show/1'
            controller.flash.message != null
            Answer.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def answer = new Answer(params)
            controller.show(answer)

        then:"A model is populated containing the domain instance"
            model.answer == answer
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def answer = new Answer(params)
            controller.edit(answer)

        then:"A model is populated containing the domain instance"
            model.answer == answer
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/answer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def answer = new Answer()
            answer.validate()
            controller.update(answer)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.answer == answer

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            answer = new Answer(params).save(flush: true)
            controller.update(answer)

        then:"A redirect is issued to the show action"
            answer != null
            response.redirectedUrl == "/answer/show/$answer.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            response.redirectedUrl == '/answer/index'
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def answer = new Answer(params).save(flush: true)

        then:"It exists"
            Answer.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(answer)

        then:"The instance is deleted"
            Answer.count() == 0
            flash.message != null
    }

    void "Test downVote and upVote"(){
        when:"downVote is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.downVote(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/answer/index'
            flash.message != null

        when:"upVote is called for a domain instance that doesn't exist"
            response.reset()
            controller.upVote(null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/answer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the upVote action"
            response.reset()
            populateValidParams(params)
            def answer = new Answer(params).save(flush: true)
            controller.upVote(answer)

        then:"The edit view is rendered again with the invalid instance"
            Answer.get(answer.id).vote == answer.vote

        when:"An invalid domain instance is passed to the downVote action"
            response.reset()
            populateValidParams(params)
            answer = new Answer(params).save(flush: true)
            controller.downVote(answer)

        then:"The edit view is rendered again with the invalid instance"
            Answer.get(answer.id).vote == answer.vote

    }

    void "Test change text"(){
        when:"change text is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.updateText(null,null)

        then:"A 404 error is returned"
            response.redirectedUrl == '/answer/index'
            flash.message != null

        when:"An invalid domain instance is passed to the upVote action"
            response.reset()
            populateValidParams(params)
            def answer = new Answer(params).save(flush: true)
            controller.updateText(answer,"test")

        then:"The edit view is rendered again with the invalid instance"
            Answer.get(answer.id).text == "test"
    }

}

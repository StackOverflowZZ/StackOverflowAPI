package stackoverflow

import grails.test.mixin.*
import spock.lang.*

@TestFor(QuestionController)
@Mock([User, Post, Question, Tag])
class QuestionControllerSpec extends Specification {

    def populateValidParams(params) {
        assert params != null
        def user = new User(username: 'me', password: 'password', email: 'test.user@domain.com')

        params["text"] = "Everything is said in the title : what is the meaning of life ?"
        params["vote"] = 0
        params["created"] = new Date()
        params["edited"] = new Date()
        params["title"] = "What is the meaning of life ?"
        params["resolved"] = false
        params["user"] = user
    }

    void "Test the index action returns the correct model"() {

        when:"The index action is executed"
            controller.index()

        then:"The model is correct"
            !model.questionList
            model.questionCount == 0
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
            controller.create()

        then:"The model is correctly created"
            model.question!= null
    }

    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def question = new Question()
            question.validate()
            controller.save(question)

        then:"The create view is rendered again with the correct model"
            model.question!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            question = new Question(params)

            controller.save(question)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/question/show/1'
            controller.flash.message != null
            Question.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def question = new Question(params)
            controller.show(question)

        then:"A model is populated containing the domain instance"
            model.question == question
    }

    void "Test that the edit action returns the correct model"() {
        when:"The edit action is executed with a null domain"
            controller.edit(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the edit action"
            populateValidParams(params)
            def question = new Question(params)
            controller.edit(question)

        then:"A model is populated containing the domain instance"
            model.question == question
    }

    void "Test the update action performs an update on a valid domain instance"() {
        when:"Update is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.update(null)

        then:"A 404 error is returned"
            flash.message != null

        when:"An invalid domain instance is passed to the update action"
            response.reset()
            def question = new Question()
            question.validate()
            controller.update(question)

        then:"The edit view is rendered again with the invalid instance"
            view == 'edit'
            model.question == question

        when:"A valid domain instance is passed to the update action"
            response.reset()
            populateValidParams(params)
            question = new Question(params).save(flush: true)
            controller.update(question)

        then:"A redirect is issued to the show action"
            question != null
            response.redirectedUrl == "/question/show/$question.id"
            flash.message != null
    }

    void "Test that the delete action deletes an instance if it exists"() {
        when:"The delete action is called for a null instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'DELETE'
            controller.delete(null)

        then:"A 404 is returned"
            flash.message != null

        when:"A domain instance is created"
            response.reset()
            populateValidParams(params)
            def question = new Question(params).save(flush: true)

        then:"It exists"
            Question.count() == 1

        when:"The domain instance is passed to the delete action"
            controller.delete(question)

        then:"The instance is deleted"
            Question.count() == 0
            flash.message != null
    }

    void "Test downVote and upVote"(){
        when:"downVote is called for a domain instance that doesn't exist"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'PUT'
            controller.downVote(null)

        then:"A 404 error is returned"
            flash.message != null

        when:"upVote is called for a domain instance that doesn't exist"
            response.reset()
            controller.upVote(null)

        then:"A 404 error is returned"
            flash.message != null

        when:"An invalid domain instance is passed to the upVote action"
            response.reset()
            populateValidParams(params)
            def question = new Question(params).save(flush: true)
            controller.upVote(question)

        then:"The edit view is rendered again with the invalid instance"
            Question.get(question.id).vote == question.vote

        when:"An invalid domain instance is passed to the downVote action"
            response.reset()
            populateValidParams(params)
            question = new Question(params).save(flush: true)
            controller.downVote(question)

        then:"The edit view is rendered again with the invalid instance"
            Question.get(question.id).vote == question.vote

    }

    void "Test change text"(){
        when:"change text is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.updateText(null,null,null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/'
        flash.message != null

        when:"An invalid domain instance is passed to the upVote action"
        response.reset()
        populateValidParams(params)
        def question = new Question(params).save(flush: true)
        controller.updateText(question, "title test", "test")

        then:"The edit view is rendered again with the invalid instance"
        Question.get(question.id).text == "test"
        Question.get(question.id).title == "title test"
    }
}

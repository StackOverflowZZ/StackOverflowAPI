package stackoverflow

import grails.test.mixin.*
import spock.lang.*

@TestFor(TagController)
@Mock([User, Tag, Question])
class TagControllerSpec extends Specification {

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

        params["name"] = "Life"
        params["questions"] = [question]

    }


    void "Test the save action correctly persists an instance"() {

        when:"The save action is executed with an invalid instance"
            request.contentType = FORM_CONTENT_TYPE
            request.method = 'POST'
            def tag = new Tag()
            tag.validate()
            controller.save(tag)

        then:"The create view is rendered again with the correct model"
            model.tag!= null
            view == 'create'

        when:"The save action is executed with a valid instance"
            response.reset()
            populateValidParams(params)
            tag = new Tag(params)

            controller.save(tag)

        then:"A redirect is issued to the show action"
            response.redirectedUrl == '/tag/show/1'
            controller.flash.message != null
            Tag.count() == 1
    }

    void "Test that the show action returns the correct model"() {
        when:"The show action is executed with a null domain"
            controller.show(null)

        then:"A 404 error is returned"
            response.status == 404

        when:"A domain instance is passed to the show action"
            populateValidParams(params)
            def tag = new Tag(params)
            controller.show(tag)

        then:"A model is populated containing the domain instance"
            model.tag == tag
    }
}

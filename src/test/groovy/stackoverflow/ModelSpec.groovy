package stackoverflow

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Answer)
class ModelSpec extends Specification {

    User user
    Role role
    Answer answer
    Question question
    Comment commentQuest
    Comment commentAnsw
    Tag tag

    def setup() {
    }

    def cleanup() {
    }

    void "Creation models"() {
        when: "Role creating"
        role = new Role(authority: 'ROLE_ADMIN')
        then: "Role created"
        role != null

        when: "USer creating"
        user = new User(username: 'me', password: 'password', email: 'test.user@domain.com')
        then: "User created"
        user != null

        when: "Question creating"
        question = new Question(text: "Everything is said in the title : what is the meaning of life ?",
            vote: 0,
            created: new Date(),
            edited: new Date(),
            title: "What is the meaning of life ?",
            views: 0,
            resolved: false,
            user: user)
        then: "Question created"
        question != null

        when: "Answer creating"
        answer = new Answer(text: "No it is a serious answer #BackToTheFuture",
                vote: 2,
                created: new Date(),
                question: question,
                user: user
        )
        then: "Answer created"
        answer != null

        when: "Comment creating"
        commentQuest = new Comment(text: "Are you serious ?",
                vote: 5,
                created: new Date(),
                edited: new Date(),
                question: question,
                anwser: null,
                user: user
        )

        commentAnsw = new Comment(text: "Go back to Google+, faggot",
                vote: 0,
                created: new Date(),
                edited: new Date(),
                question: null,
                anwser: answer,
                user: user
        )

        then: "Comment created"
        commentQuest != null
        commentAnsw != null

        when: "Tag creating"
        tag = new Tag(name: "Life", question:question, questions:[question])
        then: "Tag created"
        tag != null
/*
        when:"Question saved"
        question.save()
        then:"Question created"
        Question.count == 1

        when:"Answer saved"
        answer.save("failOnError": true)
        then:"Answer created"
        Answer.count == 1

        when:"Comment saved"
        commentQuest.save("failOnError": true)
        commentAnsw.save("failOnError": true)
        then:"Comment created"
        Comment.count == 2

        when:"Tag saved"
        tag.save("failOnError": true)
        then:"Tag created"
        Tag.count == 1

        when:"Role saved"
        role.save("failOnError": true)
        then:"Role created"
        Role.count == 1

        when:"User saved"
        user.save("failOnError": true)
        then:"Question created"
        User.count == 1

        when:"User Role linked"
        UserRole.create user, role

        UserRole.withSession {
            it.flush()
            it.clear()
        }
        then:"Question created"
        UserRole.count() == 1*/
    }
}

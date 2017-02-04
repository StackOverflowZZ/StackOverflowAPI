package stackoverflow

import grails.util.Environment

class BootStrap {

    def init = { servletContext ->

        Badge.createAllBadge()
        Feature.createAllFeatures()

        def adminRole = new Role(authority: 'ROLE_ADMIN').save()
        def userRole = new Role(authority: 'ROLE_USER').save()
        def anonymousRole = new Role(authority: 'ROLE_ANONYMOUS').save()


        // def anonymousUser = new User (username:'anonymous', password: '1234').save()

        // Stub data
        if (Environment.current == Environment.DEVELOPMENT
                || Environment.current == Environment.TEST) {

            def testUser1 = new User(username: 'me', password: 'password', email: 'test.user@domain.com')
            def testUser2 = new User(username: 'foo', password: 'password', email: 'test.foo@domain.com')
            def testUser3 = new User(username: 'bar', password: 'password', email: 'test.bar@domain.com')

            Tag tag1 = new Tag(name: "Life")
            Tag tag2 = new Tag(name: "Meaning")

            // Question user 1
            Question question1 = new Question(text: "Everything is said in the title : what is the meaning of life ?",
                    vote: 0,
                    created: new Date(),
                    edited: new Date(),
                    title: "What is the meaning of life ?",
                    views: 0,
                    resolved: false,
                    tags: [tag1])

            Answer answer2 = new Answer(text: "The answer below wasn't a very serious answer.",
                    vote: 5,
                    created: new Date(),
                    edited: new Date(),
                    question: question1
            )

            Answer answer1 = new Answer(text: "No it is a serious answer #BackToTheFuture",
                    vote: 2,
                    created: new Date(1484651600000),
                    edited: new Date(1484652600000),
                    question: question1
            )

            Answer answer4 = new Answer(text: "42",
                    vote: 2,
                    created: new Date(1485651600000),
                    edited: new Date(1485652600000),
                    question: question1
            )

            Answer answer3 = new Answer(text: "You Only Live Once",
                    vote: 2,
                    created: new Date(1484651600000),
                    edited: new Date(1484652600000),
                    question: question1
            )

            Comment commentQuest1 = new Comment(text: "Are you serious ?",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentQuest2 = new Comment(text: "Are you serious ?",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentQuest3 = new Comment(text: "Very long comment to see if it fits or not. " +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not.",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentQuest4 = new Comment(text: "Are you serious ?",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentAnsw1 = new Comment(text: "Go back to Google+, faggot",
                    vote: 0,
                    created: new Date(1484651600000),
                    edited: new Date(1484652600000)
            )

            Comment commentAnsw2 = new Comment(text: "Are you serious ?",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentAnsw3 = new Comment(text: "Very long comment to see if it fits or not. " +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not." +
                    "Very long comment to see if it fits or not. Very long comment to see if it fits or not.",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            Comment commentAnsw4 = new Comment(text: "Are you serious ?",
                    vote: 5,
                    created: new Date(),
                    edited: new Date()
            )

            // Question 2
            Question question2 = new Question(text: "This is a very long sentence, allowing me to see what would happen" +
                    " if I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if" +
                    " I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if I" +
                    " wrote a lot.",
                    vote: 0,
                    created: new Date(),
                    edited: new Date(),
                    title: "What",
                    views: 0,
                    resolved: true,
                    tags:[tag2])

            Question question3 = new Question(text: "This is a very long sentence, allowing me to see what would happen" +
                    " if I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if" +
                    " I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if I" +
                    " wrote a lot.",
                    vote: 0,
                    created: new Date(),
                    edited: new Date(),
                    title: "is",
                    views: 0,
                    resolved: true,
                    tags:[tag2,tag1])

            Question question4 = new Question(text: "This is a very long sentence, allowing me to see what would happen" +
                    " if I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if" +
                    " I wrote a lot. I repeat. This is a very long sentence, allowing me to see what would happen if I" +
                    " wrote a lot.",
                    vote: 0,
                    created: new Date(),
                    edited: new Date(),
                    title: "the meaning ?",
                    views: 0,
                    resolved: false,
                    tags:[tag2])

            testUser1.addToQuestions(question1)
            testUser2.addToQuestions(question2)
            testUser2.addToQuestions(question3)
            testUser2.addToQuestions(question4)

            testUser1.addToAnswers(answer1)
            testUser2.addToAnswers(answer2)
            testUser3.addToAnswers(answer3)
            testUser1.addToAnswers(answer4)

            testUser2.addToComments(commentAnsw1)
            testUser2.addToComments(commentAnsw2)
            testUser2.addToComments(commentAnsw3)
            testUser2.addToComments(commentAnsw4)

            testUser3.addToComments(commentQuest1)
            testUser3.addToComments(commentQuest2)
            testUser3.addToComments(commentQuest3)
            testUser3.addToComments(commentQuest4)

            question1.addToComments(commentQuest1)
            question1.addToComments(commentQuest2)
            question1.addToComments(commentQuest3)
            question1.addToComments(commentQuest4)
            answer1.addToComments(commentAnsw1)
            answer1.addToComments(commentAnsw2)
            answer1.addToComments(commentAnsw3)
            answer1.addToComments(commentAnsw4)

            tag1.save("failOnError": true)
            tag2.save("failOnError": true)

            question1.save("failOnError": true)
            question2.save("failOnError": true)
            question3.save("failOnError": true)
            question4.save("failOnError": true)

            answer1.save("failOnError": true)
            answer2.save("failOnError": true)
            answer4.save("failOnError": true)
            answer3.save("failOnError": true)

            commentQuest1.save("failOnError": true)
            commentQuest2.save("failOnError": true)
            commentQuest3.save("failOnError": true)
            commentQuest4.save("failOnError": true)
            commentAnsw1.save("failOnError": true)
            commentAnsw2.save("failOnError": true)
            commentAnsw3.save("failOnError": true)
            commentAnsw4.save("failOnError": true)

            Badge.controlBadges(testUser1)
            Badge.controlBadges(testUser2)
            Badge.controlBadges(testUser3)

            testUser1.save("failOnError": true)
            testUser2.save("failOnError": true)
            testUser3.save("failOnError": true)

            UserRole.create testUser1, adminRole
            UserRole.create testUser2, userRole
            UserRole.create testUser3, userRole


            UserRole.withSession {
                it.flush()
                it.clear()
            }

            assert User.count() == 3
            assert Role.count() == 3
            assert UserRole.count() == 3
        }
    }

    def destroy = { }
}

package stackoverflow

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class AnswerController {

    static allowedMethods = [show:"GET", addAnswer: "POST", upVote: "PUT", downVote: "PUT", updateText: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']

    @Secured(['ROLE_ANONYMOUS'])
    def show(Answer answer) {
        if(Feature.findByName("Answer").getEnable()) {
            respond answer
        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }

    @Transactional
    def addAnswer(){
        if(Feature.findByName("Answer").getEnable()) {
            Answer answer = new Answer(
                    text: params.text,
                    vote: 0,
                    created: new Date(),
                    question: Question.get(params.idQuestion),
                    user: (User)getAuthenticatedUser()
            )

            if (answer == null) {
                transactionStatus.setRollbackOnly()
                render status: NOT_FOUND
                return
            }

            if (answer.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond answer.errors, view:'create'
                return
            }

            answer.save flush:true

            Badge.controlBadges((User)getAuthenticatedUser())?.save()

            respond answer, [status: CREATED]
        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }

    @Transactional
    def upVote(Answer answer){
        if(Feature.findByName("Answer").getEnable() && Feature.findByName("Vote").getEnable()) {
            if (answer == null) {
                transactionStatus.setRollbackOnly()
                notFound()
                return
            }

            if (answer.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond answer.errors, view:'edit'
                return
            }

            answer.vote++

            answer.user.reputation += User.REPUTATION_COEF
            Badge.controlBadges(answer.user)
            answer.user.save flush:true

            answer.save flush:true

            respond answer, [status: OK]
        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }

    @Transactional
    def downVote(Answer answer) {
        if(Feature.findByName("Answer").getEnable() && Feature.findByName("Vote").getEnable()) {
            if (answer == null) {
                transactionStatus.setRollbackOnly()
                render status: NOT_FOUND
                return
            }

            if (answer.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond answer.errors, view:'edit'
                return
            }

            answer.vote--
            answer.save flush:true

            answer.user.reputation -= User.REPUTATION_COEF
            Badge.controlBadges(answer.user)
            answer.user.save flush:true

            respond answer, [status: OK]

        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }

    @Transactional
    def updateText(Answer answer, String text) {
        if(Feature.findByName("Answer").getEnable()) {
            if (answer == null) {
                transactionStatus.setRollbackOnly()
                render status: NOT_FOUND
                return
            }

            if (answer.hasErrors()) {
                transactionStatus.setRollbackOnly()
                respond answer.errors, view:'edit'
                return
            }

            answer.text = text
            answer.edited = new Date()
            answer.save flush:true

            response answer, [status: OK]
        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }

    @Transactional
    def delete(Answer answer) {
        if(Feature.findByName("Answer").getEnable()) {
            if (answer == null) {
                transactionStatus.setRollbackOnly()
                render status: NOT_FOUND
                return
            }

            def questionId = answer.question.id
            answer.delete flush:true

            render status: NO_CONTENT
        } else {
            render status: SERVICE_UNAVAILABLE
        }
    }
}

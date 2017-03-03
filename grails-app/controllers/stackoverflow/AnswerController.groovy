package stackoverflow

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class AnswerController {

    static allowedMethods = [show:"GET", addAnswer: "POST", upVote: "PUT", downVote: "PUT",
                             update: "PUT", updateText: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']

    @Secured(['ROLE_ANONYMOUS'])
    def show(Answer answer) {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }
        respond answer
    }

    @Transactional
    def addAnswer(){
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

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
    }

    @Transactional
    def upVote(Answer answer){
        if(!Feature.findByName("Answer").getEnable() || !Feature.findByName("Vote").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

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
    }

    @Transactional
    def downVote(Answer answer) {
        if(!Feature.findByName("Answer").getEnable() || !Feature.findByName("Vote").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

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
    }

    @Transactional
    def update(Answer answer) {

        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

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

        answer.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'answer.label', default: 'Answer'), answer.id])
                redirect answer
            }
            '*'{ respond answer, [status: OK] }
        }
    }

    @Transactional
    def updateText(Answer answer, String text) {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

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
    }

    @Transactional
    def delete(Answer answer) {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        if (answer == null) {
            transactionStatus.setRollbackOnly()
            render status: NOT_FOUND
            return
        }

        def questionId = answer.question.id
        answer.delete flush:true

        render status: NO_CONTENT
    }
}

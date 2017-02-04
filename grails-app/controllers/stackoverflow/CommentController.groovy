package stackoverflow

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ANONYMOUS'])
@Transactional(readOnly = true)
class CommentController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def show(Comment comment) {
        respond comment
    }

    def create() {
        respond new Comment(params)
    }


    @Secured(['ROLE_USER'])
    @Transactional
    def addComment(){
        def idQuestion = params.idAnswer!=null?Answer.get(params.idAnswer).question.id:params.idQuestion

        Comment comment = new Comment(
                text: params.text,
                vote: 0,
                created: new Date(),
                user: (User)getAuthenticatedUser()
        )

        if(params.idAnswer!=null){
            comment.setAnswer(Answer.get(params.idAnswer))
        }else{
            comment.setQuestion(Question.get(params.idQuestion))
        }

        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'create'
            return
        }

        comment.save flush:true
        Badge.controlBadges(comment.user)?.save()

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect controller: 'Question', action: 'show', id: idQuestion
            }
            '*' { respond comment, [status: CREATED] }
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    @Transactional
    def upVote(Comment comment){


        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'edit'
            return
        }

        def idQuestion = comment.answer!=null?comment.answer.question.id:comment.question.id
        comment.vote++
        comment.user.reputation += User.REPUTATION_COEF
        Badge.controlBadges(comment.user)
        comment.user.save flush: true

        comment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect controller: 'Question', action: 'show', id: idQuestion
            }
            '*'{ respond comment, [status: OK] }
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    @Transactional
    def downVote(Comment comment) {


        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'edit'
            return
        }

        def idQuestion = comment.answer!=null?comment.answer.question.id:comment.question.id
        comment.vote--
        comment.user.reputation -= User.REPUTATION_COEF
        Badge.controlBadges(comment.user)
        comment.user.save flush: true
        comment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect controller: 'Question', action: 'show', id: idQuestion
            }
            '*'{ respond comment, [status: OK] }
        }
    }

    @Transactional
    def updateText(Comment comment, String text) {
        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'edit'
            return
        }

        def idQuestion = comment.answer!=null?comment.answer.question.id:comment.question.id
        comment.text = text
        comment.edited = new Date()
        comment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect controller: 'Question', action: 'show', id: idQuestion
            }
            '*'{ respond comment, [status: OK] }
        }
    }

    @Transactional
    def save(Comment comment) {
        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'create'
            return
        }

        comment.save flush:true
        Badge.controlBadges(comment.user)?.save()

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect comment
            }
            '*' { respond comment, [status: CREATED] }
        }
    }

    def edit(Comment comment) {
        respond comment
    }

    @Transactional
    def update(Comment comment) {
        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'edit'
            return
        }

        comment.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect comment
            }
            '*'{ respond comment, [status: OK] }
        }
    }

    @Transactional
    def delete(Comment comment) {

        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        def questionId = comment.answer!=null? comment.answer.question.id:comment.question.id
        comment.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'comment.label', default: 'Comment'), comment.id])
                redirect action:"show", controller: "question", id: questionId, method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'comment.label', default: 'Comment'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

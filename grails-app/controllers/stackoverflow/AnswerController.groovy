package stackoverflow

import grails.rest.RestfulController
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class AnswerController  extends RestfulController {

    static allowedMethods = [show:"GET", addAnswer: "POST", upVote: "PUT", downVote: "PUT",
                             update: "PUT", updateText: "PUT", delete: "DELETE"]
    static responseFormats = ['json', 'xml']

    AnswerController() {
        super(Question)
    }

    @Secured(['ROLE_ANONYMOUS'])
    show() {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    @Transactional
    save(){
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

        // Send response
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: CREATED]
            }
        }
    }

    @Transactional
    update(Answer answer) {

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
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: OK]
            }
        }
    }

    @Transactional
    delete(Answer answer) {
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

        request.withFormat {
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }

    @Transactional
    upVote(Answer answer){
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


        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: OK]
            }
        }
    }

    @Transactional
    downVote(Answer answer) {
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

        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: OK]
            }
        }
    }

    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }
}

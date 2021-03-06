package stackoverflow

import grails.rest.RestfulController
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class AnswerController  extends RestfulController {

    static allowedMethods = [upVote: "PUT", downVote: "PUT"]
    static responseFormats = ['json', 'xml']

    AnswerController() {
        super(Answer)
    }

    // GET LIST
    @Secured(['ROLE_ANONYMOUS'])
    index(Integer max) {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond Answer.list(params), model:[questionCount: Answer.count()]
    }

    // GET WITH ID
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
		
		// Create resource
        def answer = createResource()

        // Assign defaults
        answer.vote = 0
        answer.created = new Date()
        answer.user = (User) getAuthenticatedUser()

        if (answer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond answer.errors, view:'create'
            return
        }

        // Save
        saveResource answer

        Badge.controlBadges((User)getAuthenticatedUser())?.save()

        // Send response
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: CREATED, view:'show']
            }
        }
    }

    @Transactional
    update() {

		if (!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Answer answer = queryForResource(params.id)

        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }
		
		answer.properties = getObjectToBind()

        if (answer.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond answer.errors, view: 'edit'  // STATUS CODE 422
            return
        }

		answer.edited = new Date()
        updateResource answer

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: OK]
            }
        }
    }

    @Transactional
    delete() {
        if(!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        def answer = queryForResource(params.id)
        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        deleteResource answer

        request.withFormat {
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }

    @Transactional
    upVote(){
		if (!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Answer answer = queryForResource(params.id)

        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        answer.vote++
        answer.user.reputation += User.REPUTATION_COEF
        Badge.controlBadges(answer.user)

        updateResource answer
        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: answer.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond answer, [status: OK]
            }
        }
    }

    @Transactional
    downVote() {
       if (!Feature.findByName("Answer").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Answer answer = queryForResource(params.id)

        if (answer == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        answer.vote--
        answer.user.reputation += User.REPUTATION_COEF
        Badge.controlBadges(answer.user)

        updateResource answer
        request.withFormat {
            '*'{
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

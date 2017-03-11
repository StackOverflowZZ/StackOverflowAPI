package stackoverflow

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.NO_CONTENT
import static org.springframework.http.HttpStatus.OK

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class QuestionController extends RestfulController {

    static allowedMethods = [test:"GET", upVote: "PUT", downVote: "PUT",
                             addView:"PUT", setResolved: "PUT", updateText: "PUT"]

    static responseFormats = ['json', 'xml']

    QuestionController() {
        super(Question)
    }

    // GET LIST
    @Secured(['ROLE_ANONYMOUS'])
    index(Integer max) {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond Question.list(params), model:[questionCount: Question.count()]
    }

    // GET WITH ID
    @Secured(['ROLE_ANONYMOUS'])
    show() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    // POST : create question
    @Transactional
    save() {

        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        // Create resource
        def question = createResource()

        // Assign defaults
        question.resolved =  false
        question.vote = 0
        question.created = new Date()
        question.user = (User)getAuthenticatedUser()

        // Verify
        if (question.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond question.errors, view:'create' // STATUS CODE 422
            return
        }

        // Save
        saveResource question

        // Change badges
        Badge.controlBadges(question.user)?.save()

        // Send response
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: CREATED]
            }
        }
    }

    // PUT : edit question
    @Transactional
    update() {

        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Question question = queryForResource(params.id)

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.properties = getObjectToBind()

        if (question.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond question.errors, view:'edit' // STATUS CODE 422
            return
        }

        updateResource question
        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: OK]
            }
        }
    }

    // DELETE : delete question
    @Transactional
    delete() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        def question = queryForResource(params.id)
        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        deleteResource question

        request.withFormat {
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }


    // TODO : do we really need it ?
    def test(Question question) {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond question
    }

    @Transactional
    upVote() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Question question = queryForResource(params.id)

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.vote++
        question.user.reputation += User.REPUTATION_COEF
        Badge.controlBadges(question.user)

        updateResource question
        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: OK]
            }
        }
    }

    @Transactional
    downVote() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Question question = queryForResource(params.id)

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.vote--
        question.user.reputation -= User.REPUTATION_COEF
        Badge.controlBadges(question.user)

        updateResource question
        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: OK]
            }
        }
    }

    @Secured(['ROLE_ANONYMOUS'])
    @Transactional
    addView() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Question question = queryForResource(params.id)

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.view++
        updateResource question

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: OK]
            }
        }
    }

    @Transactional
    setResolved() {
        if(!Feature.findByName("Question").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Question question = queryForResource(params.id)

        if (question == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        question.resolved = true
        updateResource question

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: question.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond question, [status: OK]
            }
        }
    }

    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }
}

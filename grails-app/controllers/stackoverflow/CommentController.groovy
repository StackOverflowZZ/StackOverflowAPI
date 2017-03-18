package stackoverflow

import grails.rest.RestfulController
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_USER'])
@Transactional(readOnly = true)
class CommentController extends RestfulController {

    static allowedMethods = [show:"GET", addComment: "POST", upVote: "PUT", downVote: "PUT",
                             update: "PUT", updateText: "PUT", delete: "DELETE"]

    static responseFormats = ['json', 'xml']

    CommentController() {
        super(Comment)
    }

    // GET LIST
    @Secured(['ROLE_ANONYMOUS'])
    index(Integer max) {
        if(!Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond Comment.list(params), model:[commentCount: Comment.count()]
    }


    // GET WITH ID
    @Secured(['ROLE_ANONYMOUS'])
    show() {
        if(!Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    @Secured(['ROLE_USER'])
    @Transactional
    def save() {

        if(!Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        // Create resource
        def comment = createResource()

        // Assign defaults
        comment.vote = 0
        comment.created = new Date()
        comment.user = (User) getAuthenticatedUser()

        // Verify
        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view:'create'  // STATUS CODE 422
            return
        }

        // Save
        saveResource comment

        // Change badges
        Badge.controlBadges(comment.user)?.save()

        // Send response
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: comment.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond comment, [status: CREATED, view:'show']
            }
        }
    }

    @Transactional
    def update() {

        if (!Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        Comment comment = queryForResource(params.id)

        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (comment.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond comment.errors, view: 'edit'  // STATUS CODE 422
            return
        }

		comment.edited = new Date()
        updateResource comment

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: comment.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond comment, [status: OK]
            }
        }
    }

    @Transactional
    delete() {
        if(!Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        def comment = queryForResource(params.id)
        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        deleteResource comment

        request.withFormat {
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }

    @Transactional
    def upVote(){

        if(!Feature.findByName("Vote").getEnable() || !Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        Comment comment = queryForResource(params.id)

        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        comment.vote++
        comment.user.reputation += User.REPUTATION_COEF
        Badge.controlBadges(comment.user)

        updateResource comment

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: comment.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond comment, [status: OK]
            }
        }
    }

    @Transactional
    def downVote(){

        if(!Feature.findByName("Vote").getEnable() || !Feature.findByName("Comment").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        Comment comment = queryForResource(params.id)

        if (comment == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        comment.vote--
        comment.user.reputation -= User.REPUTATION_COEF
        Badge.controlBadges(comment.user)

        updateResource comment

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: comment.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond comment, [status: OK]
            }
        }
    }

    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }
}

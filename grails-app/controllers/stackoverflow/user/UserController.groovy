package stackoverflow.user

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders
import stackoverflow.Feature
import stackoverflow.Role
import stackoverflow.User
import stackoverflow.UserRole

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ANONYMOUS'])
@Transactional(readOnly = true)
class UserController extends RestfulController {

    static allowedMethods = [getUserByName: 'GET']
    static responseFormats = ['json', 'xml']

    UserController() {
        super(User)
    }

    // GET LIST
    def index(Integer max) {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond User.list(params), model:[userCount: User.count()]
    }

    // GET WITH ID
    def show() {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    def getUserByName() {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond User.findByUsername(params.id);
    }


    @Transactional
    def save() {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        // Create resource
        def user = createResource()

        // Verify
        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'create' // STATUS CODE 422
            return
        }

        // Save
        saveResource user

        // Define role
        UserRole.create user, Role.findByAuthority('ROLE_USER')

        // Send response
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: user.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond user, [status: CREATED]
            }
        }
    }

    @Secured(['ROLE_USER'])
    @Transactional
    update() {

        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
            return
        }

        User user = queryForResource(params.id)

        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        user.properties = getObjectToBind()

        /* TODO : if not ADMIN and not the same user, don't update
        if (user.username != (User) getAuthenticatedUser()) {
            render status: UNAUTHORIZED
            return
        }*/

        if (user.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond user.errors, view:'edit' // STATUS CODE 422
            return
        }

        updateResource user

        request.withFormat {
            '*'{
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: user.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond user, [status: OK]
            }
        }
    }

    // DELETE : delete user
    @Secured(['ROLE_ADMIN'])
    @Transactional
    delete() {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        def user = queryForResource(params.id)
        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        // Get user roles linked to user
        Collection<UserRole> userRoles = UserRole.findAllByUser(user)
        userRoles*.delete()
        deleteResource user

        request.withFormat {
            '*'{ render status: NO_CONTENT } // NO CONTENT STATUS CODE
        }
    }
}

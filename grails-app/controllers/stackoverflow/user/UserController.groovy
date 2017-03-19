package stackoverflow.user

import grails.plugin.springsecurity.SpringSecurityUtils
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

    static allowedMethods = [getUserByName: 'GET', updateRole: 'PUT']
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
        def connectedUser = (User) getAuthenticatedUser()

        // If not ADMIN and not the same user, don't update
        if(!SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')
                && user.id != connectedUser.id) {
            transactionStatus.setRollbackOnly()
            render status: FORBIDDEN
            return
        }

        if (user == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        user.properties = getObjectToBind()

        /* TODO : check if roles are defined, and if they are, change roles
            http://stackoverflow.com/questions/6409548/grails-spring-security-plugin-modify-logged-in-users-authorities
        */

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

    @Secured(['ROLE_ADMIN'])
    @Transactional
    def updateRole() {

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

        if(request.JSON.role != null && request.JSON.role != "") {
            def newRole = Role.findByAuthority(request.JSON.role)
            if(newRole) {
                UserRole.removeAll(user)
                UserRole.create user, newRole
            }
        }

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

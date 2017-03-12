package stackoverflow.user

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders
import stackoverflow.Feature
import stackoverflow.Role
import stackoverflow.User

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE

@Secured(['ROLE_ANONYMOUS'])
@Transactional(readOnly = true)
class UserController extends RestfulController {

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
        respond User.list(params), model:[questionCount: User.count()]
    }

    // GET WITH ID
    def show() {
        if(!Feature.findByName("User").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
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
        UserRole.create user, Role.findByAuthority('ROLE_USER')

        saveResource user

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
}

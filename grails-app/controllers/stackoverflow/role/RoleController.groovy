package stackoverflow.role
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ANONYMOUS'])
class RoleController extends grails.plugin.springsecurity.ui.RoleController {
}

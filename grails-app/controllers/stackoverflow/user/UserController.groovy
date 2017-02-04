package stackoverflow.user
import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ANONYMOUS'])
class UserController extends grails.plugin.springsecurity.ui.UserController {
}

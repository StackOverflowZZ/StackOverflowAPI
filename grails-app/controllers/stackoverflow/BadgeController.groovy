package stackoverflow

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ANONYMOUS'])
@Transactional(readOnly = true)
class BadgeController extends RestfulController {

    static responseFormats = ['json', 'xml']

    BadgeController() {
        super(Badge)
    }

    def index(Integer max) {

        if(!Feature.findByName("Badge").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond Badge.list(params), model:[badgeCount: Badge.count()]
    }

    def show(){
        if(!Feature.findByName("Badge").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'badge.label', default: 'Badge'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

package stackoverflow

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ANONYMOUS'])
class TagController extends RestfulController {

    TagController() {
        super(Tag)
    }

    static responseFormats = ['json', 'xml']

    def index(Integer max) {
        if(!Feature.findByName("Tag").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        params.max = Math.min(max ?: 10, 100)
        respond Tag.list(params), model:[tagCount: Tag.count()]
    }

    def show() {
        if(!Feature.findByName("Tag").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond queryForResource(params.id)
    }

    protected void notFound() {
        request.withFormat {
            '*'{ render status: NOT_FOUND }
        }
    }
}

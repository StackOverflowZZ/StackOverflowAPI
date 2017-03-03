package stackoverflow

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ANONYMOUS'])
class TagController {

    static allowedMethods = [show: "GET", get: "GET"]
    static responseFormats = ['json', 'xml']

    def show(Tag tag) {
        if(!Feature.findByName("Tag").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond tag
    }

    def get(Integer id){
        if(!Feature.findByName("Tag").getEnable()) {
            render status: SERVICE_UNAVAILABLE
        }

        respond Tag.findById(id)
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'tag.label', default: 'Tag'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

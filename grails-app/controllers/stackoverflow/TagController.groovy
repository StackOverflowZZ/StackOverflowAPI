package stackoverflow

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
@Secured(['ROLE_ANONYMOUS'])
class TagController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]


    def show(Tag tag) {
        respond tag
    }

    @Transactional
    def save(Tag tag) {
        if (tag == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (tag.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond tag.errors, view:'create'
            return
        }

        tag.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'tag.label', default: 'Tag'), tag.id])
                redirect tag
            }
            '*' { respond tag, [status: CREATED] }
        }
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

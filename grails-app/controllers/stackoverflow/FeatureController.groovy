package stackoverflow

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ANONYMOUS'])
@Transactional(readOnly = true)
class FeatureController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    @Transactional
    def toggle(Feature feature) {
        if (feature == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feature.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feature.errors, view:'edit'
            return
        }

        feature.enable = !feature.enable
        feature.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'feature.label', default: 'Feature'), feature.id])
                redirect controller: 'Feature', action: 'index'
            }
            '*'{ respond feature, [status: OK] }
        }
    }

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Feature.list(params), model:[featureCount: Feature.count()]
    }

    def show(Feature feature) {
        respond feature
    }

    def create() {
        respond new Feature(params)
    }

    @Transactional
    def save(Feature feature) {
        if (feature == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feature.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feature.errors, view:'create'
            return
        }

        feature.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'feature.label', default: 'Feature'), feature.id])
                redirect feature
            }
            '*' { respond feature, [status: CREATED] }
        }
    }

    def edit(Feature feature) {
        respond feature
    }

    @Transactional
    def update(Feature feature) {
        if (feature == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (feature.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feature.errors, view:'edit'
            return
        }

        feature.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'feature.label', default: 'Feature'), feature.id])
                redirect feature
            }
            '*'{ respond feature, [status: OK] }
        }
    }

    @Transactional
    def delete(Feature feature) {

        if (feature == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        feature.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'feature.label', default: 'Feature'), feature.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'feature.label', default: 'Feature'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}

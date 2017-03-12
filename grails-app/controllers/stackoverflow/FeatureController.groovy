package stackoverflow

import grails.plugin.springsecurity.annotation.Secured

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class FeatureController {

    static allowedMethods = [index:"GET", toggle: "PUT"]
    static responseFormats = ['json', 'xml']

    @Transactional
    toggle(Feature feature) {
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

    @Secured(['ROLE_ANONYMOUS'])
    def healthCheck(){
        Feature feature = Feature.findByName(params.name)
        if (feature==null){
            respond status: NOT_FOUND
            return
        }
        if (!feature.enable) {
            respond status: SERVICE_UNAVAILABLE
            return
        }
        respond status: OK
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

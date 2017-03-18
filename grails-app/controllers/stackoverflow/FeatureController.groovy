package stackoverflow

import grails.plugin.springsecurity.annotation.Secured
import grails.rest.RestfulController
import grails.transaction.Transactional
import grails.web.http.HttpHeaders

import static org.springframework.http.HttpStatus.*

@Secured(['ROLE_ADMIN'])
@Transactional(readOnly = true)
class FeatureController extends RestfulController {

    static allowedMethods = [index:"GET"]
    static responseFormats = ['json', 'xml']
	
	
    FeatureController() {
        super(Feature)
    }

	def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Feature.list(params), model:[featureCount: Feature.count()]
    }
	
    @Transactional
    def update() {
		Feature feature = queryForResource(params.id)
	 
        if (feature == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

		feature.properties = getObjectToBind()
		 
        if (feature.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond feature.errors, view:'edit'
            return
        }

        updateResource feature
        request.withFormat {
            '*' {
                response.addHeader(HttpHeaders.LOCATION,
                        grailsLinkGenerator.link( resource: this.controllerName, action: 'show',id: feature.id, absolute: true,
                                namespace: hasProperty('namespace') ? this.namespace : null ))
                respond feature, [status: OK]
            }
        }
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

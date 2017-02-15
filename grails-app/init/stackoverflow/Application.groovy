package stackoverflow

import grails.boot.GrailsApp
import grails.boot.config.GrailsAutoConfiguration

class Application extends GrailsAutoConfiguration {
    static void main(String[] args) {
        GrailsApp.run(Application, args)
    }

    @Override
    Closure doWithSpring() {
        { ->
            // Create instance for URL health indicator.
            urlHealthCheck(UrlHealthIndicator, 'http://intranet', 2000)
        }
    }
}
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
            urlHealthCheckUser(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=User', 2000)
            urlHealthCheckComment(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Comment', 2000)
            urlHealthCheckAnswer(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Answer', 2000)
            urlHealthCheckQuestion(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Question', 2000)
            urlHealthCheckBadge(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Badge', 2000)
            urlHealthCheckTag(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Tag', 2000)
            urlHealthCheckVote(UrlHealthIndicator, 'http://localhost:8080/Feature/healthCheck?name=Vote', 2000)
        }
    }
}
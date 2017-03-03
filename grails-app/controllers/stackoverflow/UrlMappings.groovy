package stackoverflow

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/questions"(resources:'question')
        "/answers"(resources:'answer')
        "/comments"(resources:'comment')
        "/tags"(resources:'tag')
        "/badge"(resources:'badge')
        "/feature"(resources:'feature')

        "/"(controller:'question')

    }
}

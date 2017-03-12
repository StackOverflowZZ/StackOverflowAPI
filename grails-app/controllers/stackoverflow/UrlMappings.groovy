package stackoverflow

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/question"(resources:'question')
        "/user"(resources:'user')
        "/answer"(resources:'answer')
        "/comment"(resources:'comment')

        "/tag"(resources:'tag')
        "/badge"(resources:'badge')
        "/feature"(resources:'feature')

        "/"(controller:'question')

    }
}

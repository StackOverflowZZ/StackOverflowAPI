package stackoverflow

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:'question')
        "500"(view:'/error')
        "404"(view:'/notFound')

        "503"(view:'/unavailable')
    }
}

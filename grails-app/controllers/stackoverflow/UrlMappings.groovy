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
        "/answer"(resources:'answer', excludes:['create','edit'])
        "/comment"(resources:'comment', excludes:['create','edit'])


        "/tag"(resources:'tag')
        "/badge"(resources:'badge')
        "/feature"(resources:'feature')

        "/"(controller:'question')

    }
}

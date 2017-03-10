package stackoverflow

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/questions"(resources:'question', excludes:['create','edit'])
        "/answers"(resources:'answer', excludes:['create','edit'])
        "/comments"(resources:'comment', excludes:['create','edit'])


        "/tags"(resources:'tag')
        "/badge"(resources:'badge')
        "/feature"(resources:'feature')

        "/"(controller:'question')

    }
}

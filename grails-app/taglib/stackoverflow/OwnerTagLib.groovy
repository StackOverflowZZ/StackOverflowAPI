package stackoverflow

class OwnerTagLib {

    def springSecurityService

    def isOwner = { attrs, body ->
        def loggedInUser = springSecurityService.currentUser
        def owner = attrs?.owner

        // Check if admin
        def isAdmin = false
        def authorities = springSecurityService.getAuthentication().getAuthorities()

        for(role in authorities) {
            if(role.getAuthority().equals('ROLE_ADMIN')) {
                isAdmin = true
            }
        }
        //Role.findByAuthority('ROLE_ADMIN').id

        if(loggedInUser?.id == owner?.id || isAdmin) {
            out << body()
        }
    }
}

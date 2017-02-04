package stackoverflow

class Badge {

    String name

    static belongsTo = User
    static hasMany = [users: User]

    static constraints = {
        users nullable: true
    }

    static createAllBadge (){
        new Badge(name:"Ask a question").save()
        new Badge(name:"Give an answer").save()
        new Badge(name:"Comment a question").save()
        new Badge(name:"Comment an answer").save()
        new Badge(name:"Ask 3 questions").save()
        new Badge(name:"Got 50 reputation points once").save()
    }

    static User controlBadges(User user) {
        if (user != null) {
            if(user.badges == null) {
                user.badges = []
            }

            if (user.questions != null && user.questions.size() == 1) {
                user.addToBadges(findByName("Ask a question"))
            }

            if (user.questions != null && user.questions.size() == 3) {
                user.addToBadges(findByName("Ask 3 questions"))
            }

            if (user.answers != null && user.answers.size() == 1) {
                user.addToBadges(findByName("Give an answer"))
            }

            if (user.comments != null && user.comments.count({ it.question == null }) == 1) {
                user.addToBadges(findByName("Comment an answer"))
            }

            if (user.comments != null && user.comments.count({ it.answer == null }) == 1) {
                user.addToBadges(findByName("Comment a question"))
            }

            if (user.reputation >= 50) {
                user.addToBadges(findByName("Got 50 reputation points once"))
            }

            user.badges = user.badges.unique { it.name }
        }
        return user
    }
}

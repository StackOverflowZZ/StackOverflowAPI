package stackoverflow

class Question extends Post {

    String title
    Boolean resolved

    static belongsTo = [user: User]

    static hasMany = [answers: Answer, comments: Comment, tags: Tag]

    static constraints = {
        tags nullable: true
        answers nullable: true
        comments nullable: true
        tags nullable: true
    }

    // Default en bdd

    static mapping = {
        answers order: 'desc', sort: 'vote'
        comments order: 'asc', sort: 'created'
    }
}

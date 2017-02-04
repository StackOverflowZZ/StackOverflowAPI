package stackoverflow


class Answer extends Post {

    static belongsTo = [question: Question, user: User]
    static hasMany = [comments: Comment]

    static constraints = {
        comments nullable:true
    }

    static mapping = {
        comments order: 'asc', sort: 'created'
    }
}

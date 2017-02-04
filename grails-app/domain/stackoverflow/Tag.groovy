package stackoverflow

class Tag {
    String name

    static hasMany = [questions: Question]
    static belongsTo = Question

    static constraints = {

    }
}

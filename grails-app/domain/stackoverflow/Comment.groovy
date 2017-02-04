package stackoverflow

class Comment extends Post {

    static belongsTo = [answer: Answer, question: Question, user: User]

    static constraints = {
        // Personalized validator to be associated to a question OR an answer.
        answer( nullable: true,
                validator: {val, obj ->
            if(obj.question != null)
                return true
        })

        question( nullable: true,
                validator: {val, obj ->
            if(obj.answer != null)
                return true
        })
    }
}

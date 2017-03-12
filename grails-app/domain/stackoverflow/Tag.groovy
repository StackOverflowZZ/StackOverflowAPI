package stackoverflow

import grails.rest.Resource

@Resource(uri='/tag', formats=['json', 'xml'])
class Tag {
    String name

    static hasMany = [questions: Question]
    static belongsTo = Question

    static constraints = {

    }
}

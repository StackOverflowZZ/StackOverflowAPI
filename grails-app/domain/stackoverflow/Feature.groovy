package stackoverflow

class Feature {

    String name
    Boolean enable

    static createAllFeatures (){
        new Feature(name: "User", enable: true).save()
        new Feature(name: "Vote", enable: true).save()
        new Feature(name: "Comment", enable: true).save()
        new Feature(name: "Answer", enable: true).save()
        new Feature(name: "Badge", enable: true).save()
        new Feature(name: "Tag", enable: true).save()
        new Feature(name: "Question", enable: true).save()
    }

    static constraints = {

    }
}

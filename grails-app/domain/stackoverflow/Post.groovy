package stackoverflow


class Post {

    String text
    Integer vote
    Date created
    Date edited

    static constraints = {
        // To add later
        // Make sure boostraps init can handle this constraint

       /* edited (nullable: true, validator:{
            value, reference ->
                return value == null || value >= reference.created
        })*/

        edited nullable: true
    }

    static mapping = {
        text sqlType: 'longtext'
    }
}

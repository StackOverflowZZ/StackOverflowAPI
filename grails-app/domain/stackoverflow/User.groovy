package stackoverflow

import grails.rest.Resource
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
@Resource(uri='/user', formats=['json', 'xml'])
class User implements Serializable {

	static final int REPUTATION_COEF = 5

	static hasMany = [questions: Question, answers: Answer ,comments: Comment, badges: Badge]
	int reputation = 0

	private static final long serialVersionUID = 1

	transient springSecurityService

	String username
	String password
    String email
	boolean enabled = true
	boolean accountExpired
	boolean accountLocked
	boolean passwordExpired

	Set<Role> getAuthorities() {
		UserRole.findAllByUser(this)*.role
	}

	def beforeInsert() {
		encodePassword()
	}

	def beforeUpdate() {
		if (isDirty('password')) {
			encodePassword()
		}
	}

	protected void encodePassword() {
		password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
	}

	static transients = ['springSecurityService']

	static constraints = {
		password blank: false, password: true
		username blank: false, unique: true
        email blank: false, unique: true
		questions nullable: true
		answers nullable: true
		comments nullable: true
		badges nullable: true
	}

	static mapping = {
		password column: '`password`'
	}
}

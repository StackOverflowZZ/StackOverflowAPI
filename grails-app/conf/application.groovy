

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'stackoverflow.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'stackoverflow.UserRole'
grails.plugin.springsecurity.authority.className = 'stackoverflow.Role'
grails.plugin.springsecurity.logout.postOnly=false
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true
grails.plugin.springsecurity.successHandler.defaultTargetUrl='/question/index'
grails.plugin.springsecurity.ui.register.postRegisterUrl = '/question/index'

grails.plugin.springsecurity.rest.login.active=true
grails.plugin.springsecurity.rest.login.endpointUrl='/api/login'
grails.plugin.springsecurity.rest.login.failureStatusCode=401
grails.plugin.springsecurity.rest.token.validation.enableAnonymousAccess = true

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/**', filters: 'JOINED_FILTERS,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter']
]



grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_ADMIN > ROLE_USER
   ROLE_USER > ROLE_ANONYMOUS
'''

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
		[pattern: '/',               access: ['permitAll']],
		[pattern: '/error',          access: ['permitAll']],
		[pattern: '/health',         access: ['ROLE_ANONYMOUS']],
		[pattern: '/index',          access: ['permitAll']],
		[pattern: '/shutdown',       access: ['permitAll']],
		[pattern: '/assets/**',      access: ['permitAll']],
		[pattern: '/**/favicon.ico', access: ['permitAll']],
		[pattern: '/**/js/**',       access: ['permitAll']],
		[pattern: '/**/css/**',      access: ['permitAll']],
		[pattern: '/**/images/**',   access: ['permitAll']],
		[pattern: '/**/fonts/**',	 access: ['permitAll']],
		[pattern: '/user/**',        access: 'ROLE_ADMIN'],
		[pattern: '/**/register',	 access: 'ROLE_ANONYMOUS']
]

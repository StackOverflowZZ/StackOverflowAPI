<html>
    <head>
        <meta name="layout" content="${gspLayout ?: 'main'}"/>
        <title><g:message code='signin.page.title'/> - Stack Overflow</title>
        <asset:stylesheet src="userRegistration.css.css"/>
    </head>

    <body>
        <div class="container top-margin-50">
            <div id="logbox" class="top-margin-50">
                <form action="${postUrl ?: '/login/authenticate'}" method="POST" id="loginForm" autocomplete="off">
                    <h1><g:message code='login.title.label' default="Account Login"/></h1>

                    <input name="${usernameParameter ?: 'username'}" type="text" id="username" autofocus="autofocus"
                           class="input pass" placeholder="${message(code: 'login.field.username', default: 'Enter your username')}"/>

                    <input name="${passwordParameter ?: 'password'}" type="password" required="required" id="password"
                           placeholder="${message(code: 'login.field.password', default: 'Enter your username')}" class="input pass"/>

                    <input type="submit" id="submit" value="${message(code: 'login.input.submit', default: 'Sign in')}" class="inputButton"/>

                    <div class="text-center">
                        <g:link controller="user" action="create">
                            <g:message code="login.link.signup" default="Register a new account"/>
                        </g:link>
                    </div>
                </form>
                <g:if test='${flash.message}'>
                    <div class="alert alert-danger top-margin-50">${flash.message}</div>
                </g:if>
            </div>
        </div>
    </body>
</html>
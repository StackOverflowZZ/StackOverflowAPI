<!doctype html>
<html lang="fr" class="utf-8">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <title>
        <g:layoutTitle default="Stack Overflow de Pierre et Adrien"/>
    </title>

    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <asset:stylesheet src="application.css"/>
    <link rel="shortcut icon" href="${createLinkTo(dir:'images', file:'favicon.ico')}" type="image/x-icon" />

    <g:layoutHead/>

</head>
<body>
    <div class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                </button>
                <g:link class="navbar-brand" url="/">${message(code: 'menu.home.label', default: 'Home')}</g:link>
            </div>
            <div class="navbar-collapse collapse">
                <ul class="nav navbar-nav"></ul>
                <!-- Login part -->
                <div class="nav navbar-nav navbar-right">
                    <sec:ifNotLoggedIn>
                        <g:link class="navbar-brand" url="/user/create">${message(code: 'menu.user.signup', default: 'Sign up')}</g:link>
                        <g:link class="navbar-brand" url="/login">${message(code: 'menu.user.signin', default: 'Sign in')}</g:link>
                    </sec:ifNotLoggedIn>
                    <sec:ifLoggedIn>
                        <g:link class="navbar-brand" url="/user/edit/${sec.loggedInUserInfo(field: 'id')}">
                            <sec:username/>
                        </g:link>
                        <g:link class="navbar-brand" url="/logout">${message(code: 'menu.user.signout', default: 'Sign out')}</g:link>
                    </sec:ifLoggedIn>
                </div>
            </div>
        </div>
    </div>

    <div class="container body-content">
        <g:layoutBody/>
    </div>

    <div class="footer" role="contentinfo"></div>

    <div id="spinner" class="spinner" style="display:none;">
        <g:message code="spinner.alt" default="Loading&hellip;"/>
    </div>

    <asset:javascript src="application.js"/>
</body>
</html>

<sec:ifNotSwitched>
	<sec:ifAllGranted roles='${securityConfig.ui.switchUserRoleName}'>
	<g:set var='username' value='${uiPropertiesStrategy.getProperty(user, 'username')}'/>
	<g:if test='${username}'>
	<g:set var='canRunAs' value='${true}'/>
	</g:if>
	</sec:ifAllGranted>
</sec:ifNotSwitched>
<html>
<head>
	<meta name="layout" content="${gspLayout ?: 'main'}"/>
	<title><g:message code="user.page.title" args="[user.username]" default="{0}"/> - Stack Overflow</title>
</head>
<body>
<div class="container top-margin-50">

    <h1 class="text-center">
        <g:message code="user.profile.title" args="[user.username]" default="${user.username}'s profile"/>
        <span class="badge">${user.reputation}</span>
    </h1>


    <sec:ifLoggedIn>
        <g:form controller="user" action="update" method="POST" name="updateForm" autocomplete="off">
            <g:hiddenField name="id" value="${user.id}"/>
            <g:hiddenField name="version" value="${user.version}"/>
            <!-- User information -->
            <g:isOwner owner="${user}">
                <div class="panel panel-primary top-margin-50">
                    <div class="panel-heading">
                        <h3>${message(code: 'user.edit.title', default: 'Update user information')}</h3>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-4" style="display: inline-block">
                                <label>${message(code:'user.edit.username', default: 'Enter a new username :')}</label> <br/>
                                <label>${message(code:'user.edit.email', default: 'Enter a new email :')}</label> <br/>
                                <label>${message(code: 'user.edit.password', default: 'Enter a new password')}</label> <br/>
                            </div>
                            <div class="col-md-6" style="display: inline-block">
                                <input name="${usernameParameter ?: 'username'}" type="text" id="username" pattern="^[\w]{3,16}$" autofocus="autofocus"
                                       placeholder="${user.username}"/> <br />
                                <input name="${emailParameter ?: 'email'}" type="email" id="email"
                                       placeholder="${user.email}"/> <br />
                                <input name="${passwordParameter ?: 'password'}" type="password" id="password"/> <br />
                            </div>
                        </div>
                        <hr/>
                        <div class="row text-center">
                            <button type="submit" class="btn btn-warning text-center">
                                <strong>
                                    ${message(code: 'user.update.button', default: 'Update the user')}
                                </strong>
                            </button>
                        </div>
                    </div>
                </div>
            </g:isOwner>

            <!-- Admin Options -->
            <sec:ifAllGranted roles="ROLE_ADMIN">
                <div class="panel panel-danger top-margin-50">
                    <div class="panel-heading">
                        <h3>${message(code: 'user.admin.title')}</h3>
                    </div>
                    <div class="panel-body">
                        <h4 class="text-center">${message(code: 'user.role.title', default: 'Choose a role')}</h4>
                        <div class="row text-center top-margin-50">
                            <div class="col-xs-6">
                                <input type="checkbox" name="ROLE_USER">
                                <label>${message(code: 'user.role.user', default: 'User')}</label>
                            </div>
                            <div class="col-xs-6">
                                <input type="checkbox" name="ROLE_ADMIN">
                                <label>${message(code: 'user.role.admin', default: 'Administrator')}</label>
                            </div>
                        </div>
                        <hr/>
                        <div class="row text-center">
                            <div class="col-xs-6">
                                <button type="submit" class="btn btn-warning text-center">
                                    <strong>
                                        ${message(code: 'user.update.button', default: 'Update the user')}
                                    </strong>
                                </button>
                            </div>
                            <div class="col-xs-6">
                                <button type="button" class="btn btn-danger text-center">
                                    <span class="glyphicon glyphicon-minus"></span>
                                    <span><strong>
                                        ${message(code: 'user.delete.button')}
                                    </strong></span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            </sec:ifAllGranted>
        </g:form>
    </sec:ifLoggedIn>

    <!-- Questions -->
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3>${message(code: 'user.question.title', default: 'Questions asked')}</h3>
        </div>
        <div class="panel-body">
            <table class="table">
                <thead>
                    <tr>
                        <th class="text-center">Votes</th>
                        <th class="text-center">Question</th>
                        <th class="text-center">Tags</th>
                    </tr>
                </thead>
                <tbody>
                    <g:each in="${user.questions}" var="question">
                        <tr class="text-center">
                            <td>${question.vote}</td>
                            <td>
                                <g:link controller="question" action="show" id="${question.id}">
                                ${question.title}
                                </g:link>
                            </td>
                            <td>
                                <g:each in="${question.tags}" var="tag">
                                    <g:link controller="tag" action="show" id="${tag.id}">
                                        <div class="label label-default right-margin-10">
                                            ${tag.name}
                                        </div>
                                    </g:link>
                                </g:each>
                            </td>
                        </tr>
                    </g:each>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Answers -->
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3>${message(code:'user.answer.title', default:'Answers')}</h3>
        </div>
        <div class="panel-body">
            <table class="table">
                <thead>
                    <tr>
                        <th class="text-center">Votes answers</th>
                        <th class="text-center">Votes question</th>
                        <th class="text-center">Question</th>
                        <th class="text-center">Tags</th>
                    </tr>
                </thead>
                <tbody>
                <g:each in="${user.answers}" var="answer">
                    <tr class="text-center">
                        <td>${answer.vote}</td>
                        <td>${answer.question.vote}</td>
                        <td>
                            <g:link controller="question" action="show" id="${answer.question.id}">
                                ${answer.question.title}
                            </g:link>
                        </td>
                        <td>
                            <g:each in="${answer.question.tags}" var="tag">
                                <g:link controller="tag" action="show" id="${tag.id}">
                                    <div class="label label-default right-margin-10">
                                        ${tag.name}
                                    </div>
                                </g:link>
                            </g:each>
                        </td>
                    </tr>
                </g:each>
                </tbody>
            </table>
        </div>
    </div>

    <!-- Badges -->
    <div class="panel panel-info">
        <div class="panel-heading">
            <h3>${message(code: 'user.badges.title')}</h3>
        </div>
        <div class="panel-body">
            <g:each in="${user.badges}" var="badge">
                <span class="label label-success right-margin-10">
                    ${badge.name}
                </span>
            </g:each>
        </div>
    </div>
</div>
</body>
</html>
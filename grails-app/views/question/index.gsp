<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <!--
        <g:set var="entityName" value="${message(code: 'question.label', default: 'Question')}" />
        -->
        <title><g:message code="question.list.title" default="Question List" /> - Stack Overflow</title>
    </head>
    <body>
        <div id="list-question" class="content scaffold-list" role="main">

            <div class="jumbotron">
                <h1 class="text-center">
                    ${message(code: 'question.home.title', default:'Question asked')}
                </h1>
            </div>

            <g:if test="${flash.message}">
                <div class="alert alert-warning text-center" role="status">${flash.message}</div>
            </g:if>

            <sec:ifLoggedIn>
                <div class="pull-right" style="margin-bottom: 20px">
                    <g:link controller="question" action="create" class="btn btn-success">
                        <span class="glyphicon glyphicon-plus"></span>
                        <span class="text-center">Ask a question</span>
                    </g:link>
                </div>
            </sec:ifLoggedIn>
            <div>
                <table class="table table-striped">
                    <tbody>
                        <g:each in="${questionList}" var="q">
                            <tr>
                                <td class="text">
                                    <g:if test="${q.resolved}">
                                        <h4>
                                            <label class="label label-success">
                                                <span class="glyphicon glyphicon-ok"/>
                                            </label>
                                        </h4>
                                    </g:if>
                                </td>
                                <td class="text-center">
                                    <h4><strong>${q.vote}</strong></h4>
                                </td>
                                <td class="text-center">
                                    <h4><g:link action="show" id="${q.id}">${q.title}</g:link></h4>
                                </td>
                                <td class="text-center">
                                    <g:each in="${q.tags}" var="tag">
                                        <h4 style="display:inline-block;">
                                            <g:link controller="tag" action="show" id="${tag.id}">
                                                <div class="label label-default right-margin-10">
                                                    ${tag.name}
                                                </div>
                                            </g:link>
                                        </h4>
                                    </g:each>
                                </td>
                                <td>
                                    <h4>
                                        <g:isOwner owner="${q.user}">
                                            <g:link class="btn btn-primary" action="edit" id="${q.id}">
                                                <span class="glyphicon glyphicon-pencil"/>
                                            </g:link>
                                        </g:isOwner>
                                    </h4>
                                </td>
                                <td>
                                    <h4>
                                        <sec:ifAllGranted roles="ROLE_ADMIN">
                                            <g:form action="delete" controller="question" method="delete">
                                                <g:hiddenField name="id" value="${q.id}"/>
                                                <button class="btn btn-danger">
                                                    <span class="glyphicon glyphicon-trash"/>
                                                </button>
                                            </g:form>
                                        </sec:ifAllGranted>
                                    </h4>
                                </td>
                            </tr>
                        </g:each>
                    </tbody>
                </table>
            </div>
            <div>
                <g:paginate total="${questionCount ?: 0}" />
            </div>
        </div>
    </body>
</html>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'feature.label', default: 'Feature')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
<body>
<div id="list-question" class="content scaffold-list" role="main">

    <g:if test="${flash.message}">
        <div class="alert alert-warning text-center" role="status">${flash.message}</div>
    </g:if>

    <div>
        <table class="table table-striped">
            <tbody>
            <g:each in="${featureList}" var="f">
                <tr>
                    <td class="text-center">
                        <h4><g:link action="show" id="${f.id}">${f.name}</g:link></h4>
                    </td>
                     <td>
                        <sec:ifAllGranted roles="ROLE_ADMIN">
                            <g:form controller="feature" action="toggle" id="${f.id}" method="PUT">
                                <g:if test="${f.enable}">
                                    <button type="submit" class="btn btn-success">
                                        Enabled
                                    </button>
                                </g:if>
                                <g:else>
                                    <button type="submit" class="btn btn-danger">
                                        Disabled
                                    </button>
                                </g:else>
                            </g:form>
                        </sec:ifAllGranted>
                    </td>
                </tr>
            </g:each>
            </tbody>
        </table>
    </div>
    <div>
        <g:paginate total="${featureCount ?: 0}" />
    </div>
</div>
</body>
</html>
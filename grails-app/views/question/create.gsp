<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'question.label', default: 'Question')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
    <div class="container">
        <div class="jumbotron">
            <h1 class="text-center"><g:message code="question.create.title"/></h1>
        </div>

        <g:if test="${flash.message}">
            <div class="alert alert-warning text-center" role="status">${flash.message}</div>
        </g:if>

        <g:hasErrors bean="${this.question}">
        <ul class="errors" role="alert">
            <g:eachError bean="${this.question}" var="error">
            <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
        </g:hasErrors>

        <g:form controller="question" action="addQuestion" method="post">
            <h3 class="text-center top-margin-50">${message(code:'question.label.title', default: 'Question title')}</h3>
            <div class="row text-center top-margin-50">
                <input name="${titleParameter ?: 'title'}" type="text" autofocus="autofocus" class="text-center big-textarea"
                    placeholder="${message(code:'question.label.title', default:'Question title')}" autocomplete="off"/>
            </div>
            <div class="row top-margin-50">
                <textarea name="${textParameter ?: 'text'}" class="big-textarea" rows="10" autocomplete="off"
                          placeholder="${message(code:'question.label.text', default: 'Describe your question here...')}"></textarea>
            </div>

            <!-- Tag choice -->
            <div class="row top-margin-50">
            <g:each in="${listTags}" var="tag">
                <div class="right-margin-10 col-xs-3">
                    <input type="checkbox" name="tags" value="${tag.id}">
                    <h3 style="display: inline-block">
                        <span class="label label-default right-margin-10">
                            ${tag.name}
                        </span>
                    </h3>
                </div>
            </g:each>
            </div>
            <div class="text-center top-margin-50">
                <button type="submit" class="btn btn-success">
                    <span class="glyphicon glyphicon-plus"></span>
                    <span class="text-center">${message(code:'question.button.new', default: 'Add a new question')}</span>
                </button>
            </div>
        </g:form>
    </div>
    </body>
</html>

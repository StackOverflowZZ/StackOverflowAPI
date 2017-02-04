<div class="row editable_post_block comment_edition">
    <div class="col-xs-1">
        <g:form controller="answer" action="upVote" method="PUT" resource="${comment}" >
            <h4 style="margin : 0px" class="text-center">
                <button type="submit" class="btn btn-default">
                    <span class="glyphicon glyphicon-triangle-top"></span>
                </button>
            </h4>
        </g:form>

        <h5 style="margin: 0px" class="text-center">
            <span>
                ${comment.vote}
            </span>
        </h5>
        <g:form controller="answer" action="downVote" method="PUT" resource="${comment}" >
            <h4 style="margin: 0px" class="text-center">
                <button type="submit" class="btn btn-default">
                    <span class="glyphicon glyphicon-triangle-bottom"></span>
                </button>
            </h4>
        </g:form>
    </div>
    <div class="col-xs-8">
        <!-- Display of the comment -->
        <g:form style="word-wrap: break-word" controller="comment" action="updateText" method="post" resource="${this.comment}" >
           <!-- <g:textField class="big-textarea editable_display editable_display_no_border" value="${comment.text}" name="text" readonly="readonly" /> -->
            <textArea class="big-textarea editable_display editable_display_no_border comment_edition" name="text" readonly="readonly" >${comment.text}</textArea>
            <span>
                <g:link controller="user" action="edit" id="${comment.user.id}">${comment.user.username}</g:link>
                <span> | </span>
                <g:formatDate date="${comment.created}" format="dd-MM-yyyy HH:mm:ss" />
            </span>
            <g:submitButton class="btn_to_display comment_edition hidden" name="editComment" value="${message(code: 'comment.button.label', default: 'Edit')}" />
        </g:form>
    </div>

    <div class="text-right">
        <!-- Edit area -->
        <g:isOwner owner="${comment.user}">
            <div class="btn btn-primary edit_btn comment_edition">
                <span class="glyphicon glyphicon-pencil" />
                <g:message code="comment.button.label" default="Edit" />
            </div>
            <g:form method="DELETE" controller="comment" action="delete" style="margin-top: 5px;">
                <g:hiddenField name="id" value="${comment.id}" />
                <button type="submit" class="btn btn-danger">
                    <span class="glyphicon glyphicon-trash"/>
                    <g:message code="default.button.delete" default="Delete" />
                </button>
            </g:form>
        </g:isOwner>
    </div>
</div>


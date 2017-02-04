<!doctype html>
<html>
    <head>
        <title>Page Not Found</title>
        <meta name="layout" content="main">
        <g:if env="development"><asset:stylesheet src="errors.css"/></g:if>
    </head>
    <body>
    <div class="jumbotron top-margin-50">
        <h1 class="text-center">Error: Page Not Found (404)</h1>
        <h3 class="text-center top-margin-50">Path: ${request.forwardURI}</h3>
    </div>
    </body>
</html>

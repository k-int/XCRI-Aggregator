<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>
        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="jquery-1.6.2.min" />
    </head>
    <body>
      <div id="container">
       <header>
        <g:applyLayout name="header" >
         <content tag="header">
          <g:pageProperty name="page.header" />
         </content>
        </g:applyLayout>
       </header>
       <div id="main">
        <g:layoutBody />
       </div>
       <footer>
        <g:applyLayout name="footer">
         <content tag="footer">
          <g:pageProperty name="page.footer" />
         </content>
        </g:applyLayout>
       </footer>
      </div>
    </body>
</html>

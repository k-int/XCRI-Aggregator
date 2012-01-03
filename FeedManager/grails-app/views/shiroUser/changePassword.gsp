<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'shiroUser.label', default: 'User')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <g:javascript>
        	$(document).ready(function()
			{	
				$('.users').addClass('active');
			});
        </g:javascript>
    </head>
    <body> 
        <h1>Change Password</h1>               
        <g:form method="post" >
        <g:hiddenField name="id" value="${userInstance?.id}" />
        <g:hiddenField name="version" value="${userInstance?.version}" />
        <ul>
			<li class="value">
				<label for="username">Password</label>
				<g:textField name="password" class="large"/>
			</li>
			<li class="value">
				<label for="name">Confirm Password</label>
				<g:textField name="password_confirm" class="large"/>
			</li>
			<li>
				<g:actionSubmit class="save button-link button-link-positive" action="savePassword" value="${message(code: 'default.button.save.label', default: 'Save')}" />
				<g:link action="show" id="${userInstance.id}" class="button-link">${message(code: 'default.button.cancel.label', default: 'Cancel')}</g:link>
			</li>
    	</ul>
    	</g:form>
    </body>
</html>

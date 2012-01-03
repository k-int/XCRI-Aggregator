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
        <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
        <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
        </g:if>
        <g:hasErrors bean="${userInstance}">
        <div class="errors">
            <g:renderErrors bean="${userInstance}" as="list" />
        </div>
        </g:hasErrors>                   
        <g:form method="post" >
        <g:hiddenField name="id" value="${userInstance?.id}" />
        <g:hiddenField name="version" value="${userInstance?.version}" />
        <ul>
			<li class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
				<label for="username"><g:message code="shiroUser.username.label" default="Username" /></label>
				<g:textField name="username" value="${fieldValue(bean: userInstance, field: 'username')}" class="large"/>
			</li>
			<li class="value ${hasErrors(bean: userInstance, field: 'name', 'errors')}">
				<label for="name"><g:message code="shiroUser.name.label" default="Name" /></label>
				<g:textField name="name" value="${fieldValue(bean: userInstance, field: 'name')}" class="large"/>
			</li>
			<li class="value ${hasErrors(bean: userInstance, field: 'email', 'errors')}">
				<label for="email"><g:message code="shiroUser.email.label" default="Email" /></label>
				<g:textField name="email" value="${fieldValue(bean: userInstance, field: 'email')}" class="large"/>
			</li>
			<li class="value ${hasErrors(bean: userInstance, field: 'institution', 'errors')}">
				<label for="institution"><g:message code="shiroUser.institution.label" default="Institution" /></label>
				<g:textField name="institution" value="${fieldValue(bean: userInstance, field: 'institution')}" class="large"/>
			</li>
			<li class="value ${hasErrors(bean: userInstance, field: 'verified', 'errors')}">
				<label for="verified"><g:message code="shiroUser.verified.label" default="Verified" /></label>
				<g:select name="verified" from="${[true, false]}" value="${fieldValue(bean: userInstance, field: 'verified')}" />
			</li>
			<li class="value ${hasErrors(bean: userInstance, field: 'active', 'errors')}">
				<label for="active"><g:message code="shiroUser.active.label" default="Active" /></label>
				<g:select name="active" from="${[true, false]}" value="${fieldValue(bean: userInstance, field: 'active')}" />
			</li>
			<li>
				<g:actionSubmit class="save button-link button-link-positive" action="save" value="${message(code: 'default.button.save.label', default: 'Save')}" />
				<g:link action="show" id="${userInstance.id}" class="button-link">${message(code: 'default.button.cancel.label', default: 'Cancel')}</g:link>
			</li>
    	</ul>
    	</g:form>
    </body>
</html>

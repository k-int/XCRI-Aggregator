<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'shiroUser.label', default: 'User')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <g:javascript>
        	$(document).ready(function()
			{	
				$('.users').addClass('active');
			});
        </g:javascript>
    </head>
    <body>
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
      		<div class="message">${flash.message}</div>
      	</g:if>
		<table>
			<tbody>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.id.label" default="Id" /></td>                
         			<td valign="top" class="value">${fieldValue(bean: userInstance, field: "id")}</td>     
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.username.label" default="Username" /></td>
	         		<td valign="top" class="value">${fieldValue(bean: userInstance, field: "username")}</td>   
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.name.label" default="Name" /></td>
					<td valign="top" class="value">${fieldValue(bean: userInstance, field: "name")}</td>    
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.email.label" default="Email" /></td> 
					<td valign="top" class="value">${fieldValue(bean: userInstance, field: "email")}</td>  
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.institution.label" default="Institution" /></td> 
					<td valign="top" class="value">${fieldValue(bean: userInstance, field: "institution")}</td>  
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.verified.label" default="Verified" /></td> 
					<td valign="top" class="value"><g:img dir="images/table" file="${userInstance.verified}.png" /></td>  
				</tr>
				<tr class="prop">
					<td valign="top" class="name"><g:message code="shiroUser.active.label" default="Activated" /></td> 
					<td valign="top" class="value"><g:img dir="images/table" file="${userInstance.active}.png" /></td>  
				</tr>
			</tbody>
		</table>
		<g:form>
			<g:hiddenField name="id" value="${userInstance?.id}" />
			<g:actionSubmit class="edit button-link button-link-positive" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" />
			<g:actionSubmit class="edit button-link button-link-positive" action="changePassword" value="${message(code: 'default.button.change.password.label', default: 'Change Password')}" />
			<g:actionSubmit class="delete button-link" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
            <g:link action="list" class="button-link">${message(code: 'default.button.cancel.label', default: 'Cancel')}</g:link>
		</g:form>
    </body>
</html>
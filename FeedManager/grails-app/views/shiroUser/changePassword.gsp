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
        <g:form method="post" name="passwordForm" >
        <g:hiddenField name="id" value="${userInstance?.id}" />
        <g:hiddenField name="version" value="${userInstance?.version}" />
        <ul>
			<li class="value">
				<label for="username">Password</label>
				<g:passwordField name="password" class="large"/>
			</li>
			<li class="value">
				<label for="name">Confirm Password</label>
				<g:passwordField name="password_confirm" class="large"/>
			</li>
			<li>
				<g:actionSubmit class="save button-link button-link-positive" action="savePassword" value="${message(code: 'default.button.save.label', default: 'Save')}" />
				<g:link action="show" id="${userInstance.id}" class="button-link">${message(code: 'default.button.cancel.label', default: 'Cancel')}</g:link>
			</li>
    	</ul>
    	</g:form>
    	<g:javascript>	
			$(document).ready(function()
			{
				$("#passwordForm").validate(
				{
					submitHandler: function(oForm) 
					{
						oForm.submit();
					},
			        rules: {
			            password: { required: true, minlength: 6 },
			            password_confirm: { required: true, minlength: 6, equalTo: "[name=password]" }
			        }			
				});			
			});
		</g:javascript>
    </body>  
</html>

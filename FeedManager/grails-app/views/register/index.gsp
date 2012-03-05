<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.register').addClass('active');
	});
	</g:javascript>
  </head>

  <body>
    <h1>Register</h1>
    <g:form method="post" action="processRegistration" name="registrationForm">
    	<ul>
    		<li><label for="username">Username</label><input name="username" type="text" class="medium"/></li>
    		<li><label for="new_name">Name</label><input name="new_name" type="text" class="medium"/></li>
    		<li><label for="new_institution">Institution</label><input name="new_institution" type="text" class="medium"/></li>
    		<li><label for="new_email">Email</label><input name="new_email" type="text" class="medium"/></li>
    		<li><label for="password">Password</label><input name="password" type="password" class="medium"/></li>
    		<li><label for="new_confirmation">Confirm Password</label><input name="new_confirmation" type="password" class="medium"/></li>    		
    		<li><input type="submit" class="button-link" value="Register"/></li>
    	</ul>
    </g:form>
     <g:javascript>	
		$(document).ready(function()
		{
			$("#registrationForm").validate(
			{
				submitHandler: function(oForm) 
				{
					oForm.submit();
				},
		        rules: {
		            username: { required: true, minlength: 5 },
		            new_name: { required: true },
		            new_email: { required: true, email: true },
		            password: { required: true, minlength: 6 },
		            new_confirmation: { required: true, minlength: 6, equalTo: "[name=password]" }
		        } 		
			});			
		});
	</g:javascript>
  </body>
</html>

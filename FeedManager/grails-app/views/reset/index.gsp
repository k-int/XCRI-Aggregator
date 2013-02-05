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
    <h1>Reset my Password</h1>
    <g:form method="post" action="processReset" name="resetForm">
    	<ul>
    		<li><label for="username">Username</label><input name="username" type="text" class="medium"/></li>
    		<li><label for="password">New Password</label><input name="password" type="password" class="medium"/></li>
    		<li><label for="new_confirmation">Confirm Password</label><input name="new_confirmation" type="password" class="medium"/></li>    		
    		
    		<li><input type="submit" class="button-link" value="Reset"/></li>
    	</ul>
    </g:form>
     <g:javascript>	
		$(document).ready(function()
		{
			$("#resetForm").validate(
			{
				submitHandler: function(oForm) 
				{
					oForm.submit();
				},
		        rules: {
		            username: { required: true, minlength: 5 },
		            password: { required: true, minlength: 6 },
		            new_confirmation: { required: true, minlength: 6, equalTo: "[name=password]" }
		        } 		
			});			
		});
	</g:javascript>
  </body>
</html>

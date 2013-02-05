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
  	<h1>Password Reset</h1>
  	<g:if test="${flash.message}">
  		<p>${flash.message}</p>
	</g:if>
	<g:else>
		<p>For some unknown reason the password reset procedure failed. Please try again later or contact us for more information.</p>
	</g:else>
  </body>
</html>

<html>
	<body>
		<p>Hello ${userInstance.username}, to finalise your password reset request, please click the link below.</p>
		<g:link controller="reset" action="reset" params="[id:userInstance.id]" absolute="true">Please change my password</g:link>
</body>
</html>

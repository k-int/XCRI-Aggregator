<html>
	<body>
		<p>Hello ${userInstance.name}, thankyou for registering to use the XCRI-CAP Aggregator, to confirm your email address and activate your account please click the link below.</p>
		<h2><g:link controller="register" action="verify" params="[id:userInstance.id]" absolute="true">Please activate my account</g:link></h2>
	</body>
</html>

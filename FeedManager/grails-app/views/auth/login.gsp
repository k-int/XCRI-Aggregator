<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
  <meta name="layout" content="main" />
  <title>Login</title>
   <g:javascript>
    $(document).ready(function()
	{	
		$('.login').addClass('active');
	});
	</g:javascript>
</head>
<body>
	<h1>Login</h1>
  <g:if test="${flash.message}">
    <div class="message">${flash.message}</div>
  </g:if>
  <g:form action="signIn">
    <input type="hidden" name="targetUri" value="${targetUri}" />
    <ul>
    	<li>
    		<label>Username</label>
        	<input type="text" name="username" value="${username}" class="medium" />
        </li>
        <li>
			<label>Password</label></td>
			<input type="password" name="password" value="" class="medium" />
		</li>
		<li>
          <label>Remember me?</label>
          <g:checkBox name="rememberMe" value="${rememberMe}" />
		</li>
		<li><input type="submit" value="Sign in" class="button-link" /></li>
                <li><g:link controller="reset" action="index" class="login"><span>Forgot your password?</span></g:link></li>
    </ul>
  </g:form>
</body>
</html>

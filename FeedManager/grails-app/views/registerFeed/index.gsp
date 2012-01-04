<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.feeds').addClass('active');
	});
	</g:javascript>
  </head>

  <body>
	<h1>Register Feed</h1>
    <g:form name="addFeedForm">
      <ul>
        <li><label for="url">URL</label><input name="url" type="text" class="large"></li>
        <li><label for="feedname">Feed Name</label><input name="feedname" type="text" class="large"></li>
        <li><input type="submit" class="button-link" value="Submit"/></li>
      </ul>
    </g:form>
    <g:javascript>	
		$(document).ready(function()
		{
			$("#addFeedForm").validate(
			{
				submitHandler: function(oForm) 
				{
					oForm.submit();
				},
		        rules: {
		            url: { required: true },
		            feedname: { required: true }
		        }			
			});			
		});
	 </g:javascript>
  </body>
</html>

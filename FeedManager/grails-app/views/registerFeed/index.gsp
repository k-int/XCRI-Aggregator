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
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${feed}">
        <div class="errors">
            <g:renderErrors bean="${feed}" as="list" />
        </div>
    </g:hasErrors>
    <g:form action="save" name="addFeedForm">
      <ul>
        <li><label for="dataProvider">Provider</label><input name="dataProvider" type="text" class="large" value="${feed?.dataProvider}"></li>
        <li><label for="baseurl">URL</label><input name="baseurl" type="text" class="large" value="${feed?.baseurl}"></li>
        <li><label for="feedname">Feed Name</label><input name="feedname" type="text" class="large" value="${feed?.feedname}"></li>
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
                    baseurl: { required: true },
                    feedname: { required: true },
                    dataProvider: { required: true }
                }           
            });         
        });
     </g:javascript>
  </body>
</html>

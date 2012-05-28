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
        <li title="The short code for this data feed. Currently only used internally, but may be used in URLs in the future"><label for="dataProvider">Short Code</label><input name="dataProvider" type="text" class="large" value="${feed?.dataProvider}"></li>
        <li title="The URL of the data feed to be harvested"><label for="baseurl">URL</label><input name="baseurl" type="text" class="large" value="${feed?.baseurl}"></li>
        <li title="The label that appears on the feed manager console"><label for="feedname">Feed Name</label><input name="feedname" type="text" class="large" value="${feed?.feedname}"></li>
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

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
    <g:javascript>
    $(document).ready(function()
    {   
        $('.edit').addClass('active');
    });
    </g:javascript>
  </head>

  <body>
    <h1>Edit ${feedInstance.feedname}</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${feedInstance}">
        <div class="errors">
            <g:renderErrors bean="${feedInstance}" as="list" />
        </div>
    </g:hasErrors>
    <g:form method="post" id="${feedInstance?.id}">
      <g:hiddenField name="id" value="${feedInstance?.id}" />
      <g:hiddenField name="version" value="${feedInstance?.version}" />
      <ul>
        <li><label for="dataProvider">Provider</label><g:textField name="dataProvider" value="${fieldValue(bean: feedInstance, field: 'dataProvider')}" class="large" /></li>
        <li><label for="baseurl">URL</label><g:textField name="baseurl" value="${fieldValue(bean: feedInstance, field: 'baseurl')}" class="large" /></li>
        <li><label for="feedname">Feed Name</label><g:textField name="feedname" value="${fieldValue(bean: feedInstance, field: 'feedname')}" class="large" /></li>
        <li>
            <g:actionSubmit class="save button-link button button-link-positive" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
            <g:link controller="feed" action="dashboard" id="${feedInstance?.id}" class="button-link"><span>Cancel</span></g:link>
        </li>
      </ul>
    </g:form>
    <g:javascript>  
        $(document).ready(function()
        {
            $("form").validate(
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

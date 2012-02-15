<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
   	<g:javascript>
    
    $(document).ready(function()
	{	
		$('.search').addClass('active');
		
		$('#searchForm').submit(function() 
		{
		    $('form .inline-spinner').show();
            return true;
        });
	});
  </g:javascript>
</head>	
  <body class="yui-skin-sam">
	<h1>Search ${feed.feedname}</h1>
    <g:form controller="feed" action="search" name="searchForm" id="${id}" method="GET">
      <ul>
     	<li>
     	  <label for="q">Title</label>
     	  <input name="q" type="text" class="large">
     	  <div class="inline-spinner" style="display:none;">Searching</div>
     	</li>
        <li>
            <input type="submit" class="button-link button-link-positive" value="Search"/>
        </li>
      </ul>
    </g:form>
  </body>
</html>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
   	<g:javascript>
    $(document).ready(function()
	{	
		$('.search').addClass('active');
	});
	</g:javascript>
  </head>

  <body>
	<h1>Search</h1>
    <g:form controller="feed" action="search" name="searchForm" id="${id}" method="GET">
      <ul>
     	<li><label for="q">Title</label><input name="q" type="text" class="large"></li>
        <li><input type="submit" class="button-link" value="Search"/></li>
      </ul>
    </g:form>
  </body>
</html>

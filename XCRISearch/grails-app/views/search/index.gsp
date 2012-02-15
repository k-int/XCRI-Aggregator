<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
    <g:javascript>
    $(document).ready(function()
    {   
        $('form').submit(function() 
        {
            $('.inline-spinner').show();
            return true;
        });
    });
    </g:javascript>
  </head>
  <body>
    <h1>Discover course information...</h1>
    <g:form action="index" method="get">
    	<ul>
    		<li>
    		  <label for="q">Keyword(s)</label>
    		  <input id="q" name="q" type="text" class="large"/>
    		  <div class="inline-spinner" style="display:none;">Searching</div>        
    		</li>
     		<li><input type="submit" class="button-link button-link-positive" value="Search"/></li>
    	</ul>
    </g:form>
  </body>
</html>

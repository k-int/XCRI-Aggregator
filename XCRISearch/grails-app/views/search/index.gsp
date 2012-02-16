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
        
        $('.adv-toggle').click(function(evt)
        {
            evt.preventDefault();
            $('.adv').toggle(); 
            $(this).text($(this).text() == 'Show advanced search' ? 'Hide advanced search' : 'Show advanced search');
            $(this).toggleClass('active');
        });
    });
    
    function updateCount(data)
    {
       //alert(JSON.stringify(data));
       
       $('.searchCounter li:first-child').text(data.hits);
    }
    
    function failCount(errorThrown)
    {
        $('.searchCounter li:first-child').text('0');
    }
    </g:javascript>
  </head>
  <body>
    <h1>Discover course information...</h1>
    <g:form action="index" method="get">
        <div class="searchCounter">
        <ul>
	        <li>
	            0
	        </li>
	        <li>
	            Matches
	        </li>
	    </ul>
	    </div>
    	<ul>
    		<li>
    		  <label for="q">Keyword(s)</label>
    		  <input id="q" name="q" type="text" class="large" value="${params.q}" onchange="${remoteFunction(action: 'count', params: '\'q=\' + this.value', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" onkeyup="${remoteFunction(action: 'count', params: '\'q=\' + this.value', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}"/>
    		  <div class="inline-spinner" style="display:none;">Searching</div>        
    		</li>
    		<li class="adv" style="display:none">
              <label for="qualification">Qualification</label>
              <g:select name="attendance" from="${['Any','Part Time','Full Time']}" value="${params.attendance ? params.attendance : 'Any'}" class="small"/>       
            </li>
    		<li class="adv" style="display:none">
              <label for="attendance">Attendance</label>
              <g:select name="attendance" from="${['Any','Part Time','Full Time']}" value="Any" class="small"/>       
            </li>
    		<li class="adv" style="display:none">
              <label for="order">Order by</label>
              <g:select name="order" from="${['distance']}" value="distance" class="small"/>       
            </li>
            <li class="adv" style="display:none">
              <label for="format">Display as</label>
              <g:select name="format" from="${['html','xml','json']}" value="html" class="small"/>    
            </li>
            <li>
              <label></label>
              <g:link href="#" class="adv-toggle">Show advanced search</g:link>  
            </li>
     		<li><input type="submit" class="button-link button-link-positive" value="Search"/></li>
    	</ul>
    </g:form>
  </body>
</html>

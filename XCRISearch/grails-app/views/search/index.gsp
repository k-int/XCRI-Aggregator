<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
    <g:javascript>
    $(document).ready(function() {   


        $('form').submit(function() 
        {
            $('.inline-spinner').show();
            return true;
        });

        $('.adv-toggle').click(function(evt)
        {
            evt.preventDefault();
            $('.adv').toggle('blind', 500); 
            $('#adv').val("true");

            $(this).parent().remove();

            // if ( $(this).text() == 'Show advanced search' ) {
            //   $(this).text('Hide advanced search')
              // Set hiden form field to true
            //   $('#adv').val("true");
            // }
            // else {
            //   $(this).text('Show advanced search')
            //   $('#adv').val("false");
            // }
            // $(this).toggleClass('active');
            // $(this).css('display,'none');
        });
        
        var CONTEXT_PATH = '<%= request.getContextPath()%>';
        
        $("#q").autocomplete(
        {
            source: function( request, response ) 
            {
                $.getJSON(CONTEXT_PATH + "/search/autocomplete", request, function(data) 
                {
                    response($.map(data.hits.hits, function(item) { return item.fields.title.value; }));
                });
            },
            select: function(event, ui) 
            { 
                $.getJSON(CONTEXT_PATH + '/search/count?q=' + ui.item.value + '&studyMode=' + $('select[name=studyMode] option:selected').val() + '&qualification=' + $('select[name=qualification] option:selected').val() + '&location=' + $('input[name=location]').val() + '&distance=' + $('select[name=distance] option:selected').val(), function(data) 
                {
                    updateCount(data);
                });
            },  
            minLength: 2
        });
    });
    
    function updateCount(data)
    {
        if(data.hits > 100)
        {
            $('.searchCounter li:first-child').removeClass('positive').addClass('negative');
        }
        else if(data.hits > 0)
        {
            $('.searchCounter li:first-child').removeClass('negative').addClass('positive');
        }
        else
        {
            $('.searchCounter li:first-child').removeClass('negative').removeClass('positive')
        }        
        
        $('.searchCounter li:first-child').text(data.hits);
    }
    
    function failCount(errorThrown)
    {
        $('.searchCounter li:first-child').text('0').removeClass('positive').removeClass('negative');
    }
    
    function getQString()
    {
        var query_str = 'q=' + $('input[name=q]').val().trim();
                
        if($('select[name=studyMode] option:selected').val() != '*')
        {
            query_str += '&studyMode=' + $('select[name=studyMode] option:selected').val();
        }
        if($('select[name=qualification] option:selected').val() != '*')
        {
             query_str += '&qualification=' + $('select[name=qualification] option:selected').val();
        }
        if($('input[name=location]').val() && $('input[name=location]').val().trim().length > 0)
        {
            query_str += '&location=' + $('input[name=location]').val();
            query_str += '&distance=' + $('select[name=distance] option:selected').val();
        }
        if($('select[name=provider] option:selected').val().trim().length > 0)
        {
            query_str += '&provider=' + $('select[name=provider] option:selected').val();
        }
        
        return  query_str;
    }
        
    </g:javascript>
  </head>
  <body>
    <h1>Discover course information...</h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <g:form action="index" method="get">
        <g:hiddenField name="adv" value="" id="adv"/>
        <div class="searchCounter default">
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
          <input id="q" name="q" type="text" class="large" value="${params.q}" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" onkeyup="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}"/>
          <div class="inline-spinner" style="display:none;">Searching</div>        
        </li>
                <g:if test="${params.adv==true}">
          <li class="adv">
                </g:if>
                <g:else>
          <li class="adv" style="display:none">
                </g:else>
              <label for="provider">Providers</label>
          <g:select name="provider" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.provider}" optionKey="value" optionValue="key" class="large"/>
        </li>
        <li class="adv" style="display:none">
              <label for="qualification">Qualification</label>
              <g:select name="qualification" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.qualification}" optionKey="value" optionValue="key" value="All" class="small"/>       
            </li>
        <li class="adv" style="display:none">
              <label for="studyMode">Attendance</label>
              <g:select name="studyMode" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.studyMode}" optionKey="value" optionValue="key" value="Any" class="small"/>       
            </li>
            <li class="adv" style="display:none">
              <label for="distance">Within</label>
              <g:select name="distance" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.distance}" optionKey="value" optionValue="key" value="default" class="small"/> <g:select name="dunit" from="${search_config.dunit}" optionKey="value" optionValue="key" value="Miles"/>
            </li>
        <li class="adv" style="display:none">
              <label for="order">Order by</label>
              <g:select name="order" from="${search_config.order}" optionKey="value" optionValue="key" value="default" class="small"/>       
            </li>
            <li class="adv" style="display:none">
              <label for="location">My postcode is</label>
              <input id="location" name="location" type="text" class="large">  
            </li>
            <li class="adv" style="display:none">
              <label for="format">Display as</label>
              <g:select name="format" from="${['html','xml','json']}" value="html" class="small"/>    
            </li>
            <li>
              <label></label>
              <g:link href="#" class="adv-toggle">Show advanced search</g:link>  
            </li>
            <li>
              <input type="submit" class="button-link button-link-positive" value="Search"/>
              <g:link class="button-link button-link-positive" controller="search" >reset</g:link>
            </li>
      </ul>
    </g:form>
  </body>
</html>

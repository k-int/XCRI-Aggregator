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
            $('.adv').toggle('blind', 500);
            $(this).text($(this).text() == 'Show advanced search' ? 'Hide advanced search' : 'Show advanced search');
            $(this).toggleClass('active');
        });
        
        var CONTEXT_PATH = '<%= request.getContextPath()%>';
        
        $("#q").autocomplete(
        {
            source: function( request, response ) 
            {
                $.getJSON(CONTEXT_PATH + "/search/autocomplete", request, function(data) 
                {
                    response($.map(data.hits.hits, function(item)
                                                   {
                                                        return item.fields.title.value;
                                                    }));
                });
            },
            select: function(event, ui) 
            { 
                //alert(JSON.stringify(ui.item));
            
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
        $('.searchCounter li:first-child').text('0').removeClass('positive').removeClass('negative')
    }
    
    function getQString()
    {
        return 'q=' + $('input[name=q]').val() + '&studyMode=' + $('select[name=studyMode] option:selected').val() + '&qualification=' + $('select[name=qualification] option:selected').val() + '&location=' + $('input[name=location]').val() + '&distance=' + $('select[name=distance] option:selected').val();
    }
    </g:javascript>
  </head>

  <body>
   <div class="searchForm">
   <g:form action="index" method="get">
      <div class="searchCounter default">
      <ul>
          <g:if test="${hits.totalHits > 100}">
             <li class="negative">${hits.totalHits}</li>
          </g:if>
          <g:elseif test="${hits.totalHits > 0}">
             <li class="positive">${hits.totalHits}</li>
          </g:elseif>
          <g:else>
             <li>${hits.totalHits}</li>
          </g:else>
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
       <li class="adv" style="display:none">
            <label for="qualification">Qualification</label>
            <g:select name="qualification" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.qualification}" optionKey="value" optionValue="key" value="${params.qualification ? params.qualification : 'All'}" class="small"/>       
       </li>
       <li class="adv" style="display:none">
            <label for="studyMode">Attendance</label>
            <g:select name="studyMode" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.studyMode}" optionKey="value" optionValue="key"  value="${params.studyMode ? params.studyMode : 'Any'}" class="small"/>       
       </li>
       <li class="adv" style="display:none">
            <label for="distance">Within</label>
            <g:select name="distance" onchange="${remoteFunction(action: 'count', params: 'getQString()', onSuccess: 'updateCount(data)', onFailure:'failCount(errorThrown)', method: 'GET' )}" from="${search_config.distance}" optionKey="value" optionValue="key" value="${params.distance ? params.distance : '100km'}" class="small"/>       
       </li>
       <li class="adv" style="display:none">
            <label for="order">Order by</label>
            <g:select name="order" from="${search_config.order}" optionKey="value" optionValue="key" value="${params.order ? params.order : 'distance'}" class="small"/>       
       </li>
       <li class="adv"  style="display:none">
            <label for="location">My location is</label>
            <input id="location" name="location" type="text" class="large" value="${params.location}">  
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
       </li>
     </ul>
    </g:form>
    </div>

    <h1>Search Results
      <g:if test="${place==true}">
        Near ${fqn}
      </g:if>
    </h1>

      <div class="paginateButtons">
        <g:if test="${params.int('offset')}">
         Showing Results ${params.int('offset') + 1} - ${hits.totalHits < (params.int('max') + params.int('offset')) ? hits.totalHits : (params.int('max') + params.int('offset'))} of ${hits.totalHits}
        </g:if>
        <g:elseif test="${hits.totalHits && hits.totalHits > 0}">
          Showing Results 1 - ${hits.totalHits < params.int('max') ? hits.totalHits : params.int('max')} of ${hits.totalHits}
        </g:elseif>
        <g:else>
          Showing ${hits.totalHits} Results
        </g:else>
        <span><g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${hits.totalHits}" /></span>
      </div>  
      
      <div class="facetFilter">
        <g:each in="${facets}" var="facet">
          <div>      
        <h1 class="open"><a href="">${facet.key == 'level' ? 'qualification level' : facet.key}</a></h1>
            <ul>
              <g:each in="${facet.value}" var="fe">
                      <% def ops = [:]
                    params.each 
                    {
                        ops."$it.key" = it.value
                    }
                  %>
                <g:if test="${params[facet.key] && params[facet.key].contains(fe.term.toString())}">  
                  <%   
                      def uniqueLink = []
                      uniqueLink.addAll(params."${facet.key}")
                      uniqueLink.remove(fe.term.toString())
                      ops."${facet.key}" = uniqueLink
                    %>
                  <li>
                    <g:link class="active" params='${ops}' title="${fe.display}">
                    <span>
                      <g:if test="${fe.display.length() > 16}"> 
                        ${fe.display.substring(0,13)}...
                      </g:if>
                      <g:else>
                        ${fe.display}
                      </g:else>
                    </span>
                    <span> ${fe.count}</span>
                    </g:link>
                  </li>
                </g:if>
                <g:else>
                <li>
                   <% 
                     def uniqueLink = ["${fe.term}"]
                     uniqueLink.addAll(params."${facet.key}")                                 
                     ops."${facet.key}" = uniqueLink
                    %>
                  <g:link params='${ops}' title="${fe.display}">
                    <span>
                      <g:if test="${fe.display.length() > 18}"> 
                        ${fe.display.substring(0,15)}...
                      </g:if>
                      <g:else>
                        ${fe.display}
                      </g:else>
                    </span>
                    <span> ${fe.count}</span>
                  </g:link>
                </li>
                </g:else>  
              </g:each>
            </ul>
          </div>
        </g:each>
      </div>

      <div id="resultsarea">
        <ul>
        <g:each in="${hits}" var="crs">

            <g:if test="${crs.source.imageuri?.length() > 0}">
              <img src="${crs.source.imageuri}" style="float:right" />
            </g:if>

            <li class="result">
                <h3>
                  <g:link controller="course" action="index" id="${crs.source._id}">
                    <g:if test="${crs.source.title?.length() > 0}">${crs.source.title}</g:if>
                    <g:else>Missing course title.</g:else>
                  </g:link> via 
                  <g:if test="${crs.source.provtitle?.length() > 0}">${crs.source.provtitle}</g:if>
                  <g:else>Missing Provider Name (${crs.source.provid})</g:else>
                  <span class="h-link-small">&lt;<g:link controller="course" action="index" id="${crs.source._id}" target="_blank" params="[format:'json']">JSON</g:link>&gt;</span>
                  <span class="h-link-small">&lt;<g:link controller="course" action="index" id="${crs.source._id}" target="_blank" params="[format:'xml']">XML</g:link>&gt;</span>
                </h3>
                <ul>
                  <g:if test="${crs.source.description?.length() > 0}"> 
                    <li>Qualification: ${crs.source.qual?.title}, ${crs.source.qual?.level}, ${crs.source.qual?.awardedBy}</li>
                    <li>
                      <g:if test="${crs.source.description?.length() > 500}"> 
                      ${crs.source.description.substring(0,300)}<g:link controller="course" action="index" id="${crs.source._id}">...</g:link>
                      </g:if>
                      <g:else>
                        ${crs.source.description}
                      </g:else>
                    </li> 
                  </g:if>

            <li>Subjects: 
              <g:each in="${crs.source.subject}" var="subject">
                <g:link controller="search" action="index" params='[q:"subjectKw:${subject}"]'>${subject}</g:link>&nbsp;
              </g:each>
            </li>
  
              <li>Course Link: <a href="${crs.source.url}">${crs.source.url}</a></li> 

              <g:if test="${place==true}">
                <li>Distance: ${crs.sortValues[0].round(2)}km</li>
              </g:if>

          </ul>
        </li>
        </g:each>
        </ul>
      </div>
      <g:if test="${hits.totalHits > params.int('max')}">
      <div class="paginateButtons paginate-bottom">
        <g:if test="${params.int('offset')}">
         Showing Results ${params.int('offset') + 1} - ${hits.totalHits < (params.int('max') + params.int('offset')) ? hits.totalHits : (params.int('max') + params.int('offset'))} of ${hits.totalHits}
        </g:if>
        <g:elseif test="${hits.totalHits && hits.totalHits > 0}">
          Showing Results 1 - ${hits.totalHits < params.int('max') ? hits.totalHits : params.int('max')} of ${hits.totalHits}
        </g:elseif>
        <g:else>
          Showing ${hits.totalHits} Results
        </g:else>
        <span><g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${hits.totalHits}" /></span>
      </div> 
      </g:if>
      <g:javascript>
        $(document).ready(function()
    {
      $('.facetFilter h1 a').click(function(evt)
      {
        evt.preventDefault();
        
        var clicked = $(this).parent();
        
        clicked.next('ul').toggle('blind', 500, function(){ clicked.toggleClass('open'); });
      });
    });
      </g:javascript>
  </body>
</html>

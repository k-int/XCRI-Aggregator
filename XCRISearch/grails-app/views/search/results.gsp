<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
  </head>

  <body>
   <div class="searchForm">
   <g:form action="index" method="get">
     <ul>
       <li><label for="q">Keyword(s)</label><input id="q" name="q" type="text" class="large" value="${params.q}" /></li>
       <li><input type="submit" class="button-link button-link-positive" value="Search"/></li>
     </ul>
    </g:form>
    </div>

    <h1>Search Results</h1>

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
        <h1 class="open"><a href="">${facet.key}</a></h1>
            <ul>
              <g:each in="${facet.value}" var="fe">
                      <% def ops = [:]
                params.each 
                    {
                ops."$it.key" = it.value
                    }
                  %>
                <g:if test="${params.(facet.key) && params.(facet.key).contains(fe.term)}">  
                  <%   
                              def uniqueLink = []
                              uniqueLink.addAll(params."${facet.key}")
                              uniqueLink.remove(fe.term)
                      ops."${facet.key}" = uniqueLink
                    %>
                  <li>
                    <g:link class="active" params='${ops}'>
                    <span>
                      <g:if test="${fe.term.length() > 16}"> 
                        ${fe.term.substring(0,13)}...
                      </g:if>
                      <g:else>
                        ${fe.term}
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
                  <g:link params='${ops}'>
                    <span>
                      <g:if test="${fe.term.length() > 18}"> 
                        ${fe.term.substring(0,15)}...
                      </g:if>
                      <g:else>
                        ${fe.term}
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
                  <g:link controller="course" action="index" id="${crs.source._id}">${crs.source.title}</g:link> via 
                  <g:if test="${crs.source.provtitle?.length() > 0}">${crs.source.provtitle}</g:if>
                  <g:else>Missing Provider Name (${crs.source.provid})</g:else>
                </h3>
                <ul>
                  <g:if test="${crs.source.description?.length() > 0}"> 
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
        <li>Qualification: ${crs.source.qual?.title} ${crs.source.qual?.level} ${crs.source.qual?.awardedBy}</li>
                </ul>
                <g:if test="${params.debug==true}">
                  <pre>For debugging, json follows<br/>${crs?.source}</pre>
                </g:if>
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

<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
  </head>

  <body>
   <g:form action="index" method="get">
     <ul>
       <li><label for="q">Keyword(s)</label><input id="q" name="q" type="text" class="large" value="${params.q}" /></li>
       <li><input type="submit" class="button-link button-link-positive" value="Search"/></li>
     </ul>
    </g:form>

    <h1>Search Results</h1>

    <ul>
      <div class="paginateButtons">
        <g:if test="${params.int('offset')}">
         Showing Results ${params.int('offset') + 1} - ${resultsTotal < (params.int('max') + params.int('offset')) ? resultsTotal : (params.int('max') + params.int('offset'))} of ${resultsTotal}
        </g:if>
        <g:elseif test="${resultsTotal && resultsTotal > 0}">
          Showing Results 1 - ${resultsTotal < params.int('max') ? resultsTotal : params.int('max')} of ${resultsTotal}
        </g:elseif>
        <g:else>
          Showing ${resultsTotal} Results
        </g:else>
        <span><g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${resultsTotal}" /></span>
      </div>  

      <div id="resultsarea">
        <g:each in="${searchresult?.hits}" var="crs">
          <div>

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
                      ${crs.source.description.substring(0,500)}<g:link controller="course" action="index" id="${crs.source._id}">...</g:link>
                      </g:if>
                      <g:else>
                        ${crs.source.description}
                      </g:else>
                    </li> 
                  </g:if>
<li>Subjects: <g:each in="${crs.source.subject}" var="subject"><g:link controller="search" action="index">${subject}</g:link>&nbsp;</g:each></li>
<li>Course Link: <a href="${crs.source.url}">${crs.source.url}</a></li>
<li>Qualification: ${crs.source.qual?.title} ${crs.source.qual?.level} ${crs.source.qual?.awardedBy}</li>
                </ul>
                <g:if test="${params.debug==true}">
                  <pre>For debugging, json follows<br/>${crs?.source}</pre>
                </g:if>
            </li>
          </div>
        </g:each>
      </div>

      <div id="facets">
        <ul>
          <g:each in="${facets}" var="facet">
            <li>${facet.key}
              <ul>
                <g:each in="${facet.value.entries}" var="fe">
                  <li>${fe.term} : ${fe.count}</li>
                </g:each>
              </ul>
            </li>
          </g:each>
        </ul>
      </div><!--id="facets"-->

    </ul>
  </body>
</html>

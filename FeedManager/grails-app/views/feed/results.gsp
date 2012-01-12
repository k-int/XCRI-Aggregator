<!doctype html>
<html>
  <head>
    <meta name="layout" content="feedlayout"/>
    <title>Search front page</title>
  </head>
  <body>
	<h1>Search Results</h1>
	<div class="paginateButtons">
		<g:if test="${params.int('offset')}">
		   	Showing Results ${params.int('offset') + 1} - ${searchresult.hits.totalHits < (params.int('max') + params.int('offset')) ? searchresult.hits.totalHits : (params.int('max') + params.int('offset'))} of ${searchresult.hits.totalHits}
		</g:if>
		<g:elseif test="${searchresult.hits.totalHits && searchresult.hits.totalHits > 0}">
			Showing Results 1 - ${searchresult.hits.totalHits < params.int('max') ? searchresult.hits.totalHits : params.int('max')} of ${searchresult.hits.totalHits}
		</g:elseif>
		<g:else>
			Showing ${searchresult.hits.totalHits} Results
		</g:else>
		<span><g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${searchresult.hits.totalHits}" /></span>
	</div>
	<ul>	
	<g:each in="${searchresult?.hits}" var="crs">
	  <li class="result">
	      <h3>${crs.source.title}</h3>
	      <ul>
	      	<li><span style="font-style:italic">${crs.source.identifier}</span></li>
	      	<li>${crs.source.subject == "[]" ? "" : crs.source.subject}</li>
	      	<li>${crs.source.description}</li>
	      </ul>
	  </li>
	</g:each>
	</ul>

  </body>
</html>

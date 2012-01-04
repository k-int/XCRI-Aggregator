<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
  </head>
  <body>
	<h1>Search Results</h1>
	<p class="result">
	<g:img dir="images/icons" file="subject.png"/><span>Subject</span>
	<g:img dir="images/icons" file="identifier.png"/><span>Identifier</span>
	<g:img dir="images/icons" file="link.png"/><span>URL</span>
	</p>
	<ul>
	<g:each in="${searchresult?.hits}" var="crs">
	  <li class="result">
	      <h3><g:link controller="course" action="index" id="${crs.source._id}">${crs.source.title}</g:link> via ${crs.source.provtitle} (${crs.source.provid})</h3>
	     <!--  <i>${crs.source.url}</i>-->
	      <ul>
	      	<li><g:img dir="images/icons" file="subject.png"/><span><g:join in="${crs.source.subject}" delimiter=", "/></span></li>
	        <li><g:img dir="images/icons" file="identifier.png"/><span>${crs.source.identifier}</span></li>
	        <li><g:img dir="images/icons" file="link.png"/><span>${crs.source.url}</span></li>
	      </ul>
	      <pre>For debugging, json follows<br/>${crs?.source}</pre>
	  </li>
	</g:each>
	</ul>

  </body>
</html>

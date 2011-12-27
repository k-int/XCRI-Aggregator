<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
  </head>
  <body>
Search results page

<ul>
<g:each in="${searchresult?.hits}" var="crs">
  <li>
    <div class="result">
      <h3><g:link controller="course" action="index" id="${crs.source._id}">${crs.source.title}</g:link></h3>
      <i>${crs.source.url}</i>
      <p>
        Subjects: <g:join in="${crs.source.subject}" delimiter=", "/><br/>
        Identifier: ${crs.source.identifier}<br/>
        URL: ${crs.source.url}<br/>
        <pre>For debugging, json follows<br/>
        ${crs?.source}
        </pre>
      </p>
    </div>
  </li>
</g:each>
</ul>

  </body>
</html>

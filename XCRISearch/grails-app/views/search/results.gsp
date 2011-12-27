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
      <h3><a href="${crs.source.url}">${crs.source.title}</a></h3>
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

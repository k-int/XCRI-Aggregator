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
    ${crs?.source}
  </li>
</g:each>
</ul>

  </body>
</html>

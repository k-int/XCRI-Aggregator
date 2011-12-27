<!doctype html>
<html>
  <head>
    <meta name="layout" content="main"/>
    <title>Search front page</title>
  </head>
  <body>
    Discover course information...
    <g:form action="index" type="get">
      <label for="coursetitle">Course Title:</label><input id="coursetitle" name="title" type="text"/>
      <label for="coursedescription">Course Description:</label><input id="coursedescription" name="description" type="text"/>
      <label for="freetext">Anywhere:</label><input id="freetext" name="freetext" type="text"/>
      <input type="submit"/>
    </g:form>
  </body>
</html>

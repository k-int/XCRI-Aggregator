<html>
<head>
</head>
<body>
  <h1>XCRI Search</h1>
  <g:form action="index" method="get">
    Course Name: <input type="text" name="coursename"/></br>
    <input type="submit"/>
  </g:form>

  <g:if test="${hits}">
    Search found ${hits.hits.total} records <br/>
    <table border="1">
      <tr><th>Score</th><th>Course Title</th><th>Course Provider</th></tr>
      <g:each in="${hits.hits.hits}" var="hit">
        <tr>
          <td>${hit?.score}</td>
          <td>${hit?._source?.title}</td>
          <td>${hit?._source?.provtitle}</td>
        </tr>
      </g:each>
    </table>
  </g:if>
</body>
</html>

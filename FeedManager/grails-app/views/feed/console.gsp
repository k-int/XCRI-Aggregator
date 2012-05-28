<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.console').addClass('active');
	});
	</g:javascript>
  </head>

  <body>

    <h1>Console for XCRI Data Feed ${feed?.feedname}</h1>

    <table>
      <tr>
        <th>Timestamp</th>
        <th>Message Type</th>
        <th>Logging Level</th>
        <th>Details</th>
      </tr>
      <g:each in="${lastlog?.eventLog}" var="entry">
        <g:if test="${entry.type=='msg'}">
          <tr>
            <td><g:formatDate format="dd MMM HH:mm:ss" date="${entry.ts}"/></td>
            <td>${entry.type}</td>
            <td>${entry.lvl}</td>
            <td>${entry.msg}</td>
          </tr>
        </g:if>
      </g:each>
    </table>

  </body>
</html>

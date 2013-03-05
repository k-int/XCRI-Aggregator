<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
  <g:javascript>
    function filterLogs()
    {
    var error_level = $('#logs-filter option:selected').val();

    if(error_level && error_level.length > 0)
    {
    $('tr.error-level-' + error_level).show();
    $('tr:not(.error-level-' + error_level + ')').hide();
    }
    else
    {
    $('tr').show();
    }
    }

    $(document).ready(function()
    {	
    $('.console').addClass('active');
    });
  </g:javascript>
</head>

<body>

  <h1>Console for XCRI Data Feed ${feed?.feedname}</h1>
  Filter Logging Level
  <select id="logs-filter" onchange="filterLogs()">
    <option value="">All</option>
    <option value="info">Info</option>
    <option value="warn">Warn</option>
    <option value="error">Error</option>
  </select>
  <table>
    <tr>
      <th>Timestamp</th>
      <th>Message Type</th>
      <th>Logging Level</th>
      <th>Details</th>
    </tr>
    <g:each in="${lastlog?.eventLog}" var="entry">
      <g:if test="${entry.type=='msg'}">
        <tr class="error-level-${entry.lvl}">
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

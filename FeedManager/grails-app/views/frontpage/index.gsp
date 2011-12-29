<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  </head>

  <body>
Course Data Feed Manager

All Feeds Feeds

<table>
  <tr>
    <th>Feed ID</th>
    <th>Feed Name</th>
    <th>Active?</th>
    <th>Feed Type</th>
    <th>Status</th>
    <th>Last Run</th>
    <th>Next Scheduled Harvest</th>
  </tr>
  <tr>
    <th colspan="7">Status message</th>
  </tr>

  <g:each in="${feeds}" var="feed">
    <tr>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.id}</g:link></td>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.feedname}</g:link></td>
      <td>${feed.active}</td>
      <td>${feed.feedtype}</td>
      <td>${feed.status}</td>
      <td>${feed.lastCheck}</td>
      <td>${feed.lastCheck+feed.checkInterval}</td>
    </tr>
    <tr>
      <td colspan="7">${feed.statusMessage}</td>
    </tr>
  </g:each>
</table>

  </body>
</html>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
  </head>

  <body>
User Home Page

<g:link controller="registerFeed" action="index">Register New Feed</g:link>

Your Feeds

<table>
  <tr>
    <th>Feed ID</th>
    <th>Feed Name</th>
    <th>Feed Type</th>
    <th>Status</th>
    <th>Last Run</th>
    <th>Next Scheduled Harvest</th>
  </tr>
  <g:each in="${user_feeds}" var="feed">
    <tr>
      <td><g:link controller="feed" action="index" id="${feed.id}">${feed.id}</g:link></td>
      <td><g:link controller="feed" action="index" id="${feed.id}">${feed.feedname}</g:link></td>
      <td>${feed.feedtype}</td>
      <td></td>
      <td></td>
      <td></td>
    </tr>
  </g:each>
</table>

  </body>
</html>

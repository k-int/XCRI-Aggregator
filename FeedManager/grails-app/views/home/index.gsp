<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.feeds').addClass('active');
	});
	</g:javascript>
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
    <th>Actions</th>
  </tr>
  <g:each in="${user_feeds}" var="feed">
    <tr>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.id}</g:link></td>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.feedname}</g:link></td>
      <td>${feed.feedtype}</td>
      <td>${feed.status}</td>
      <td></td>
      <td></td>
      <td>
        <g:if test="${feed.status in [1,3,4]}">
          <g:link controller="feed" action="collect" id="${feed.id}">Collect Now</g:link>
        </g:if>
    </tr>
  </g:each>
</table>

  </body>
</html>

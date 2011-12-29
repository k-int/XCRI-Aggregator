<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="main" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.home').addClass('active');
		$('img[title]').qtip(
		{
			position: 
			{
    			my: 'top center',  // Position my top right...
    			at: 'bottom center', // at the top center of...
 			},
 			style:
 			{
 				classes: 'ui-tooltip-rounded ui-tooltip-dark'
 			},
 			hide: 
 			{
      			fixed: true
   			}
		});
	});
	</g:javascript>
  </head>

  <body>
<h1>Course Data Feed Manager - All Feeds</h1>
<table>
	<thead>
	  <tr>
	    <th>ID</th>
	    <th>Name</th>
	    <th>Active?</th>
	    <th>Type</th>
	    <th>Status</th>
	    <th>Last Harvest</th>
	    <th>Next Harvest</th>
	    <th>Status message</th>
	  </tr>
	  </thead>
	  <tbody>
  <g:each in="${feeds}" var="feed">
    <tr>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.id}</g:link></td>
      <td><g:link controller="feed" action="dashboard" id="${feed.id}" class="ellipsis-overflow">${feed.feedname}</g:link></td>
      <td>${feed.active}</td>
      <td>${feed.feedtype}</td>
      <td>${feed.status}</td>
      <td><g:formatDate format="dd-MM-yyyy" date="${feed.lastCheck}"/></td>
      <td><g:formatDate format="dd-MM-yyyy" date="${feed.lastCheck+feed.checkInterval}"/></td>
      <td><g:img dir="images/table" file="info32.png" title="${feed.statusMessage}"/></td>
    </tr>
    </tbody>
    <tfoot>
    </tfoot>
  </g:each>
</table>
  </body>
</html>

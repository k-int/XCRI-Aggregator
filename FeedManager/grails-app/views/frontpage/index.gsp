<%@ page import="groovy.time.*" %>
<%@ page import="com.k_int.feedmanager.utils.DurationFormatter" %>
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
<h1>Course Data Feed Manager</h1>
<div class="paginateButtons">
<g:if test="${params.int('offset')}">
   	Showing Feeds ${params.int('offset') + 1} - ${feedsTotal < (params.int('max') + params.int('offset')) ? feedsTotal : (params.int('max') + params.int('offset'))} of ${feedsTotal}
</g:if>
<g:elseif test="${feedsTotal && feedsTotal > 0}">
	Showing Feeds 1 - ${feedsTotal < params.int('max') ? feedsTotal : params.int('max')} of ${feedsTotal}
</g:elseif>
<g:else>
	Showing ${feedsTotal} Feeds
</g:else>
<g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${feedsTotal}" /></div>
<table>
	<thead>
	  <tr>
	    <th>ID</th>
	    <th>Name</th>
	    <th>Active</th>
	    <th>Type</th>
	    <th>Last Harvest</th>
	    <th>Next Harvest</th>
	    <th>Status</th>
	  </tr>
	  </thead>
	  <tbody>
	  <g:each in="${feeds}" var="feed">
	    <tr>
	      <td><g:link controller="feed" action="dashboard" id="${feed.id}">${feed.id}</g:link></td>
	      <td><g:link controller="feed" action="dashboard" id="${feed.id}" class="ellipsis-overflow">${feed.feedname}</g:link></td>
	      <td><g:img dir="images/table" file="${feed.active}.png" class="centered" /></td>
	      <td>${feed.feedtype}</td>
	      <g:if test="${feed.lastCheck}">
	     	 <td><g:formatDate format="dd MMM HH:mm" date="${feed.lastCheck}"/></td>
	      </g:if>
      	  <g:else>
      		<td>Never</td>
          </g:else>
	      <g:if test="${feed.lastCheck && feed.checkInterval}">
	      	<td>${use(DurationFormatter){TimeCategory.minus(new Date(feed.lastCheck+feed.checkInterval), new Date()).toString()}}</td>
	      </g:if>
	      <g:else>
	      	<td>Unknown</td>
	      </g:else>
	      <td><g:img dir="images/table" file="status-${feed.status}.png" title="${feed.statusMessage}" class="centered"/></td>
	    </tr>
	   </g:each>
	   </tbody>
     </table>
  </body>
</html>

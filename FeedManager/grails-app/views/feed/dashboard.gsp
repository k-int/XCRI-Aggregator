<%@ page import="groovy.time.*" %>
<%@ page import="com.k_int.feedmanager.utils.DurationFormatter" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta name="layout" content="feedlayout" />
    <g:javascript>
    $(document).ready(function()
	{	
		$('.dashboard').addClass('active');
	});
	</g:javascript>
  </head>

  <body>
    <h1>${feed.feedname}</h1>
    <p><g:if test="${feed.resourceIdentifier && feed.resourceIdentifier.length() > 0}">
      Feed resource identifier is ${feed.resourceIdentifier}, this feed is searchable.
    </g:if>
    <g:else>
      This feed is not searchable.
    </g:else>
    </p><br/>
    <g:if test="${feed?.lastCollect}"><p>Last ran on <g:formatDate format="dd MMM HH:mm" date="${feed.lastCollect}"/></p></g:if>
    <br/>
    <g:if test="${feed?.lastCheck && feed?.checkInterval}"><p>The next collection is due in ${use(DurationFormatter){TimeCategory.minus(new Date(feed?.lastCheck+feed?.checkInterval), new Date()).toString()}}</p></g:if>
    <br/>
    <p>This feed is 
    <g:if test="${(feed.publicationStatus != null) && (feed.publicationStatus == 1)}">Published. You can withdraw the records by clicking <a href="">here</a></g:if>
    <g:else>not published. You can make these records active by clicking <a href="">here</a></g:else></p>
    
  </body>
</html>

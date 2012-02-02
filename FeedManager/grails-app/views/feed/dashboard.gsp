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
    <g:if test="${feed.resourceIdentifier}!=null">
      Feed resource identifier is ${feed.resourceIdentifier}  this feed is searchable
    </g:if>
    <g:else>
      feed not searchable
    </g:else>

    Feed was last collectedon ${feed.lastCollect} and the feed is
    <g:if test="${(feed.publicationStatus != null) && (feed.publicationStatus == 1)}">Published. You can withdraw the records by clicking <a href="">here</a></g:if>
    <g:else>Not published. You can make these records active by clicking <a href="">here</a></g:else>
    
  </body>
</html>

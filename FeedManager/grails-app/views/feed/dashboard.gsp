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
<g:if test="${flash.message}">
  <div class="message">${flash.message}</div>
  <div class="message">For more help, try the <a href="https://github.com/k-int/XCRI-Aggregator/wiki/FAQ">FAQ Page</a></div>
</g:if>
<br/>
<table class="vertical">
  <tr>
    <th title="Short code - Used internally, and in the minting of some short-form URLs">Short Code</th>
    <td>${feed.dataProvider}</td>
  </tr>
  <tr>
    <th title="The URL of the DataFeed to harvest">URL</th>
    <td>${feed.baseurl}</td>
  </tr>
  <g:if test="${feed.resourceIdentifier && feed.resourceIdentifier.length() > 0}">
    <tr title="collection status of this data feed: if collection has been successful, the date and time of last collection are displayed; if unsuccessful, please follow the guidance provided here">
      <th>Harvested</th>
      <td>Yes<g:if test="${feed.lastCollect}">, last ran on <g:formatDate format="dd MMMM HH:mm" date="${feed.lastCollect}"/></g:if>
    </td>     
    </tr>
    <tr title="collection status of this data feed: if collection has been successful, the date and time of last collection are displayed; if unsuccessful, please follow the guidance provided here">
      <th>Next Check</th>
      <td>
    <g:if test="${feed.lastCheck && feed.checkInterval}">
      <g:formatDate format="dd MMM HH:mm" date="${(feed.lastCheck + feed.checkInterval)}"/> <span style="color:gray">(${use(DurationFormatter){TimeCategory.minus(new Date(feed.lastCheck+feed.checkInterval), new Date()).toString()}})</span></g:if>
    </td>
    </tr>
    <tr>
      <th>Check Interval</th>
      <td title="How often this feed is checked for updates">
    <g:if test="${feed.checkInterval}">
${use(DurationFormatter){TimeCategory.minus(new Date(new Date().getTime() + feed.checkInterval), new Date()).toString()}}
    </g:if>
    </td>
    </tr>
    <tr title="This is the identifier extracted from your XCRI Feed. It is used by the aggregator to detect changes. If you want the aggregator to update your records, rather than simply add new ones each year, it is important that you correctly form your XCRI documents">
      <th>Resource Identifier</th>
      <td>${feed.resourceIdentifier}</td>
    </tr>
    <tr>
      <th title="Tells you if records have actually been harvested from the remote XCRI source and are ready to be reviewed. If so, those records will be searchable in the feed manager for you to verify, but are not made visible in the public interface until you publish them.">Searchable</th>
      <td>
  ${feed.totalRecords > 0 ? 'Yes' : 'No'}, there are ${feed.totalRecords} records
      </td>
    </tr>

    <tr>
      <th title="">Publication Status</th>
      <td>
    <g:if test="${(feed.publicationStatus != null)}">
      <g:if test="${feed.publicationStatus==0}">
        private (unpublished). Request publication by clicking <g:link controller="feed" action="publish" id="${id}">here</g:link>
      </g:if>
      <g:if test="${feed.publicationStatus==1}">
        pending publication, to cancel and return to private (unpublished), click <g:link controller="feed" action="publish" id="${id}">here</g:link>
      </g:if>
      <g:if test="${feed.publicationStatus==2}">
        public (published). Request withdrawal from the public search interface by clicking <g:link controller="feed" action="publish" id="${id}">here</g:link>
      </g:if>
      <g:if test="${feed.publicationStatus==3}">
        pending removal from the public search interface. To cancel and return to "Published", click <g:link controller="feed" action="publish" id="${id}">here</g:link>
      </g:if>
    </g:if>
    </td>
    </tr>
  </g:if>
  <g:else>
    <tr>
      <th title="collection status of this data feed: if collection has been successful, the date and time of last collection are displayed; if unsuccessful, please follow the guidance provided here">Harvested</th>
      <td>
        This data feed has not been collected yet. If you registered it recently, please check back here for updates (these may take a few minutes or hours). If you registered it more than 24 hours ago, it is likely that collection has been prevented by errors. Please look at the <g:link controller="feed" action="console" id="${params.id}">Console Page</g:link> For more information.</td>
    </tr>
    <tr>
      <th title="This is the identifier extracted from your XCRI Feed. It is used by the aggregator to detect changes. If you want the aggregator to update your records, rather than simply add new ones each year, it is important that you correctly form your XCRI documents">Resource Identifier</th>
      <td>This feed will have an identifier once a harvest has been completed</td>
    </tr>
    <tr>
      <th  title="Tells you if records have actually been harvested from the remote XCRI source and are ready to be reviewed. If so, those records will be searchable in the feed manager for you to verify, but are not made visible in the public interface until you publish them.">Searchable</th>
      <td>
        No
      </td>
    </tr>
  </g:else>
</table>  
<g:link controller="feed" action="delete" id="${feed.id}" class="button-link" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"><span class="deletion"></span> Delete</g:link>
<g:if test="${feed.status in [1,3,4]}">
  <g:link controller="feed" action="collect" id="${feed.id}" class="button-link button-link-positive"><span class="collect"></span> Collect</g:link>
</g:if>
<br/><br/>
</body>
</html>

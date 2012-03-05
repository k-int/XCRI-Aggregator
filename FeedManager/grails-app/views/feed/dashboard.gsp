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
    </g:if>
    <br/>
    <table class="vertical">
        <tr>
            <th>Short Code</th>
            <td>${feed.dataProvider}</td>
        </tr>
        <tr>
            <th>URL</th>
            <td>${feed.baseurl}</td>
        </tr>
        <tr>
            <th>Type</th>
            <td>${feed.feedtype}</td>
        </tr>
        <g:if test="${feed.resourceIdentifier && feed.resourceIdentifier.length() > 0}">
	        <tr>
	            <th>Harvested</th>
	            <td>Yes<g:if test="${feed.lastCollect}">, last ran on <g:formatDate format="dd MMMM HH:mm" date="${feed.lastCollect}"/></g:if></td>     
	        </tr>
	        <tr>
                <th>Last Check</th>
                <td><g:if test="${feed.lastCheck}"><g:formatDate format="dd MMM HH:mm" date="${feed.lastCheck}"/></g:if><g:else>Never</g:else></td>
            </tr>
	        <tr>
	            <th>Next Check</th>
	            <td>
	                <g:if test="${feed.lastCheck && feed.checkInterval}">
	                <g:formatDate format="dd MMM HH:mm" date="${(feed.lastCheck + feed.checkInterval)}"/> <span style="color:gray">(${use(DurationFormatter){TimeCategory.minus(new Date(feed.lastCheck+feed.checkInterval), new Date()).toString()}})</span></g:if>
	            </td>
	        </tr>
	        <tr>
                <th>Check Interval</th>
                <td>
                    <g:if test="${feed.checkInterval}">
                    ${use(DurationFormatter){TimeCategory.minus(new Date(new Date().getTime() + feed.checkInterval), new Date()).toString()}}
                    </g:if>
                </td>
            </tr>
	        <tr>
                <th>Resource Identifier</th>
                <td>${feed.resourceIdentifier}</td>
            </tr>
	        <tr>
                <th>Searchable</th>
                <td>
                    ${feed.totalRecords > 0 ? 'Yes' : 'No'}, there are ${feed.totalRecords} records
                </td>
            </tr>
            <tr>
                <th>Publication Status</th>
                <td>
                <g:if test="${(feed.publicationStatus != null)}">
			      <g:if test="${feed.publicationStatus==0}">
			        private (unpublished). To make it public, mark the feed as 'to be published' by clicking <g:link controller="feed" action="publish" id="${id}">Here</g:link>
			      </g:if>
			      <g:if test="${feed.publicationStatus==1}">
			        pending publication, to cancel and return to private (unpublished), click <g:link controller="feed" action="publish" id="${id}">Here</g:link>
			      </g:if>
			      <g:if test="${feed.publicationStatus==2}">
			        public (published). To mark the courses from this feed for withdrawl from the public search interface click <g:link controller="feed" action="publish" id="${id}">Here</g:link>
			      </g:if>
			      <g:if test="${feed.publicationStatus==3}">
			        pending removal from the public search interface. To cancel and return to "Published", click <g:link controller="feed" action="publish" id="${id}">Here</g:link>
			      </g:if>
                </g:if>
                </td>
            </tr>
        </g:if>
        <g:else>
	        <tr>
	            <th>Harvested</th>
	            <td>This feed has not been harvested yet. If this is a newly registered feed it should be picked up within the next 5 minutes. If this is an old feed you should check for errors which might be preventing the harvest process.</td>
	        </tr>
	        <tr>
                <th>Resource Identifier</th>
                <td>This feed will have an identifier once a harvest has been completed</td>
            </tr>
	        <tr>
                <th>Searchable</th>
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

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
	    
	    $('img[title]:not(.complex-msg)').qtip(
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
	    
	    $('.complex-msg').qtip(
        {
           content: 
           {
              text: function(api) 
              {
                // Retrieve content from custom attribute of the selector element.
                return splitMessage($(this).attr('title'));
              }
           },
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
  
    
    function splitMessage(fullString)
    {
        var messages = fullString.split("/");    
          
        var tip = $('<dl class="parallel"></dl>');
           
        for(var i = 0; i < messages.length; ++i)
        {   
            var oKey = messages[i].split(':')[0].trim();
            var oValue = messages[i].split(':')[1].trim();
      
            if(oKey != null && oValue != null && oKey != '' && oValue != '' && oKey != 'null' && oValue != 'null')
            {
                tip.append('<dt>' + oKey + '</dt>')
                   .append('<dd>' + oValue + '</dd>');
            }
        }
        return tip;   
    }
  </g:javascript>
  </head>

  <body>
<h1>XCRI Data Feed Manager</h1>
<div class="paginateButtons">
  <g:if test="${params.int('offset')}">
       Showing Feeds <em>${params.int('offset') + 1} - ${feedsTotal < (params.int('max') + params.int('offset')) ? feedsTotal : (params.int('max') + params.int('offset'))}</em> of <em>${feedsTotal}</em>
  </g:if>
  <g:elseif test="${feedsTotal && feedsTotal > 0}">
    Showing Feeds <em>1 - ${feedsTotal < params.int('max') ? feedsTotal : params.int('max')}</em> of <em>${feedsTotal}</em>
  </g:elseif>
  <g:else>
    Showing <em>${feedsTotal}</em> Feeds
  </g:else>
  <span><g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${feedsTotal}" /></span>
</div>
<table>
  <thead>
    <tr>
      <th>Name</th>
      <th>Active</th>
      <th>Last Check</th>
      <th>Next Check</th>
      <th>Public</th>
      <th>Records</th>
      <th>Status</th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${feeds}" var="feed">
      <tr>
        <td class="ellipsis-overflow">${feed.feedname}</td>
        <td><g:img dir="images/table" file="${feed.active}.png" class="centered" /></td>
        <td>
          <g:if test="${feed.lastCheck}"><g:formatDate format="dd MMM HH:mm" date="${feed.lastCheck}"/></g:if>
          <g:else>Never</g:else>
        </td>
        <td>
          <g:if test="${feed?.lastCheck && feed?.checkInterval}"><g:formatDate format="dd MMM HH:mm" date="${(feed.lastCheck + feed.checkInterval)}"/></g:if>
          <g:else>---</g:else>
        </td>
        <td>
          <g:if test="${feed.publicationStatus == 2 || feed.publicationStatus == 3}"><g:img dir="images/table" file="true.png"/></g:if>
          <g:else><g:img dir="images/table" file="false.png"/></g:else>
        </td>
        <td>
         ${feed.totalRecords}
        </td>
        <td>
            <g:if test="${feed.status == 3 && feed.statusMessage?.find(/Code:\-?[1-9]/)}">
                <g:link controller="feed" action="console" id="${feed.id}">
                    <g:img dir="images/table" file="error.png" class="centered complex-msg" title="${feed.statusMessage}"/>
                </g:link>
            </g:if>
            <g:elseif test="${feed.statusMessage?.find(/Code:\-?[0-9]/)}">
                <g:img dir="images/table" file="status-${feed.status}.png" class="centered complex-msg" title="${feed.statusMessage}"/> 
            </g:elseif>
            <g:else> 
                <g:img dir="images/table" file="status-${feed.status}.png" class="centered" title="${feed.statusMessage}"/>       
            </g:else>
        </td>
      </tr>
     </g:each>
     </tbody>
     </table>
  </body>
</html>

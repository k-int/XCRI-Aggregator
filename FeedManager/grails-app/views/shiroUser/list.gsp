<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'shiroUser.label', default: 'ShiroUser')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
        <g:javascript>
        	$(document).ready(function()
			{	
				$('.users').addClass('active');
			});
        </g:javascript>
    </head>
    <body>
        	<h1>User Administration</h1>
        	<g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
			<g:form method="get" action="list" name="list">
				<ul>
					<li><label for="name">Username</label><input type="text" name="username" id="username" class="large" value="${params.username}"/></li>
					<li><label for="username">Name</label><input type="text" name="name" id="name" class="large" value="${params.name}"/></li>
					<li><label for="email">Email</label><input type="text" name="email" id="email" class="large" value="${params.email}"/></li>
					<li><label for="institution">Institution</label><input type="text" name="institution" id="institution" class="large" value="${params.institution}"/></li>
					<li><input type="submit" value="Search" class="button-link button-link-positive"/></li>
				</ul>
			</g:form>
			<br/>
			<div class="paginateButtons">
            <g:if test="${params.int('offset')}">
		    	Showing Users ${params.int('offset') + 1} - ${usersTotal < (params.int('max') + params.int('offset')) ? usersTotal : (params.int('max') + params.int('offset'))} of ${usersTotal}
			</g:if>
			<g:elseif test="${usersTotal && usersTotal > 0}">
				Showing Users 1 - ${usersTotal < params.int('max') ? usersTotal : params.int('max')} of ${usersTotal}
			</g:elseif>
			<g:else>
				Showing ${usersTotal} Users
			</g:else>
          	</div>
            <div class="list">
                <table>
                    <thead>
                        <tr>                    
                            <g:sortableColumn property="id" title="${message(code: 'shiroUser.id.label', default: 'Id')}" />
                            <g:sortableColumn property="username" title="${message(code: 'shiroUser.username.label', default: 'Username')}" />
                            <g:sortableColumn property="name" title="${message(code: 'shiroUser.name.label', default: 'Name')}" />
                            <g:sortableColumn property="email" title="${message(code: 'shiroUser.email.label', default: 'Email')}" />
                            <g:sortableColumn property="institution" title="${message(code: 'shiroUser.institution.label', default: 'Institution')}" />
                            <g:sortableColumn property="verified" title="${message(code: 'shiroUser.verified.label', default: 'Verified')}" />
                            <g:sortableColumn property="active" title="${message(code: 'shiroUser.active.label', default: 'Activated')}" />
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${usersList}" status="i" var="userInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}"> 
                            <td><g:link action="show" id="${userInstance.id}" class="show">${fieldValue(bean: userInstance, field: "id")}</g:link></td>
                            <td>${fieldValue(bean: userInstance, field: "username")}</td>
                            <td>${fieldValue(bean: userInstance, field: "name")}</td>
                            <td>${fieldValue(bean: userInstance, field: "email")}</td>
                            <td>${fieldValue(bean: userInstance, field: "institution")}</td>
                            <td><g:img dir="images/table" file="${userInstance.verified}.png" class="centered" /></td>
                            <td><g:img dir="images/table" file="${userInstance.active}.png"  class="centered"/></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate params="${params}" next="&nbsp;" prev="&nbsp;" maxsteps="1" total="${usersTotal}" />
            </div>
    </body>
</html>

<%@ page import="org.strotmann.plz.Sucher" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sucher.label', default: 'Sucher')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-sucher" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.sucher.label"/></g:link></li>
			</ul>
		</div>
		<div id="list-sucher" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="plz" title="${message(code: 'postleitzahl.plz.label')}" />
					
						<g:sortableColumn property="ort" title="${message(code: 'postleitzahl.ort.label')}" />
						
						<g:sortableColumn property="strasse" title="${message(code: 'postleitzahl.strasse.label')}" />
						
						<g:sortableColumn property="hnrVon" title="${message(code: 'strasse.hnrVon.label')}" />
						
						<g:sortableColumn property="hnrBis" title="${message(code: 'strasse.hnrBis.label')}" />
								
					</tr>
				</thead>
				<tbody>
				<g:each in="${sucherInstanceList}" status="i" var="sucherInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<g:if test="${sucherInstance?.mitStrassen && !sucherInstance?.strasse}">
							<td><g:link action="list" params="[postleitzahl:sucherInstance.postleitzahl]" id="${sucherInstance.id}">${fieldValue(bean: sucherInstance, field: "postleitzahl")}</g:link></td>
						</g:if>
						<g:else>
							<td>${fieldValue(bean: sucherInstance, field: "postleitzahl")}</td>
						</g:else>
						<td>${fieldValue(bean: sucherInstance, field: "ort")}</td>
						
						<td>${fieldValue(bean: sucherInstance, field: "strasse")}</td>
						
						<td>${fieldValue(bean: sucherInstance, field: "hnrVon")}</td>
						
						<td>${fieldValue(bean: sucherInstance, field: "hnrBis")}</td>
											
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${sucherInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
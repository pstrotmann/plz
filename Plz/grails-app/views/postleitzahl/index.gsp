
<%@ page import="org.strotmann.plz.Postleitzahl" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'postleitzahl.label', default: 'Postleitzahl')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-postleitzahl" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-postleitzahl" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="plz" title="${message(code: 'postleitzahl.plz.label', default: 'Plz')}" />
					
						<g:sortableColumn property="osmId" title="${message(code: 'postleitzahl.osmId.label', default: 'Osm Id')}" />
					
						<g:sortableColumn property="ort" title="${message(code: 'postleitzahl.ort.label', default: 'Ort')}" />
					
						<g:sortableColumn property="bundesland" title="${message(code: 'postleitzahl.bundesland.label', default: 'Bundesland')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${postleitzahlInstanceList}" status="i" var="postleitzahlInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${postleitzahlInstance.id}">${fieldValue(bean: postleitzahlInstance, field: "plz5")}</g:link></td>
					
						<td>${fieldValue(bean: postleitzahlInstance, field: "osmId")}</td>
					
						<td>${fieldValue(bean: postleitzahlInstance, field: "ort")}</td>
					
						<td>${fieldValue(bean: postleitzahlInstance, field: "bundeslandKlar")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${postleitzahlInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

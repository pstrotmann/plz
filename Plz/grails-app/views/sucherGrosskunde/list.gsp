<%@ page import="org.strotmann.plz.SucherGrosskunde" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sucherGrosskunde.label', default: 'SucherGrosskunde')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-sucherGrosskunde" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.sucherGrosskunde.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-sucherGrosskunde" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="plz" title="${message(code: 'postleitzahl.plz.label')}" />
					
						<g:sortableColumn property="ort" title="${message(code: 'postleitzahl.ort.label')}" />
						
						<g:sortableColumn property="strasse" title="${message(code: 'postleitzahl.grosskunde.label')}" />
					</tr>
				</thead>
				<tbody>
				<g:each in="${sucherGrosskundeInstanceList}" status="i" var="sucherGrosskundeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td>${fieldValue(bean: sucherGrosskundeInstance, field: "postleitzahl")}</td>
						
						<td>${fieldValue(bean: sucherGrosskundeInstance, field: "ort")}</td>
						
						<td>${fieldValue(bean: sucherGrosskundeInstance, field: "grosskunde")}</td>
												
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${sucherGrosskundeInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
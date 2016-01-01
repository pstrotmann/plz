
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
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
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
					
						<g:sortableColumn property="postleitzahl" title="${message(code: 'sucherGrosskunde.postleitzahl.label', default: 'Postleitzahl')}" />
					
						<g:sortableColumn property="ort" title="${message(code: 'sucherGrosskunde.ort.label', default: 'Ort')}" />
					
						<g:sortableColumn property="grosskunde" title="${message(code: 'sucherGrosskunde.grosskunde.label', default: 'Grosskunde')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${sucherGrosskundeInstanceList}" status="i" var="sucherGrosskundeInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${sucherGrosskundeInstance.id}">${fieldValue(bean: sucherGrosskundeInstance, field: "postleitzahl")}</g:link></td>
					
						<td>${fieldValue(bean: sucherGrosskundeInstance, field: "ort")}</td>
					
						<td>${fieldValue(bean: sucherGrosskundeInstance, field: "grosskunde")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${sucherGrosskundeInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

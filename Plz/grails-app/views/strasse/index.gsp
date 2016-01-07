
<%@ page import="org.strotmann.plz.Strasse" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'strasse.label', default: 'Strasse')}" />
		<title><g:message code="default.list.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#list-strasse" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="list-strasse" class="content scaffold-list" role="main">
			<h1><g:message code="default.list.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
			<thead>
					<tr>
					
						<g:sortableColumn property="strasse" title="${message(code: 'strasse.strasse.label', default: 'Strasse')}" />
					
						<g:sortableColumn property="hnrVon" title="${message(code: 'strasse.hnrVon.label', default: 'Hnr Von')}" />
					
						<g:sortableColumn property="hnrBis" title="${message(code: 'strasse.hnrBis.label', default: 'Hnr Bis')}" />
					
						<th><g:message code="strasse.plz.label" default="Plz" /></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${strasseInstanceList}" status="i" var="strasseInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${strasseInstance.id}">${fieldValue(bean: strasseInstance, field: "strasse")}</g:link></td>
					
						<td>${fieldValue(bean: strasseInstance, field: "hausNrVon")}</td>
					
						<td>${fieldValue(bean: strasseInstance, field: "hausNrBis")}</td>
					
						<td>${fieldValue(bean: strasseInstance, field: "postleitzahl")}</td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${strasseInstanceCount ?: 0}" />
			</div>
		</div>
	</body>
</html>

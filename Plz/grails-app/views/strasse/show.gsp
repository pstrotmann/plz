
<%@ page import="org.strotmann.plz.Strasse" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'strasse.label', default: 'Strasse')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-strasse" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-strasse" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list strasse">
			
				<g:if test="${strasseInstance?.strasse}">
				<li class="fieldcontain">
					<span id="strasse-label" class="property-label"><g:message code="strasse.strasse.label" default="Strasse" /></span>
					
						<span class="property-value" aria-labelledby="strasse-label"><g:fieldValue bean="${strasseInstance}" field="strasse"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.hnrVon}">
				<li class="fieldcontain">
					<span id="hnrVon-label" class="property-label"><g:message code="strasse.hnrVon.label" default="Hnr Von" /></span>
					
						<span class="property-value" aria-labelledby="hnrVon-label"><g:fieldValue bean="${strasseInstance}" field="hnrVon"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.zusVon}">
				<li class="fieldcontain">
					<span id="zusVon-label" class="property-label"><g:message code="strasse.zusVon.label" default="Zus Von" /></span>
					
						<span class="property-value" aria-labelledby="zusVon-label"><g:fieldValue bean="${strasseInstance}" field="zusVon"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.hnrBis}">
				<li class="fieldcontain">
					<span id="hnrBis-label" class="property-label"><g:message code="strasse.hnrBis.label" default="Hnr Bis" /></span>
					
						<span class="property-value" aria-labelledby="hnrBis-label"><g:fieldValue bean="${strasseInstance}" field="hnrBis"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.zusBis}">
				<li class="fieldcontain">
					<span id="zusBis-label" class="property-label"><g:message code="strasse.zusBis.label" default="Zus Bis" /></span>
					
						<span class="property-value" aria-labelledby="zusBis-label"><g:fieldValue bean="${strasseInstance}" field="zusBis"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.plz}">
				<li class="fieldcontain">
					<span id="plz-label" class="property-label"><g:message code="strasse.plz.label" default="Plz" /></span>
					
						<span class="property-value" aria-labelledby="plz-label"><g:link controller="postleitzahl" action="show" id="${strasseInstance?.plz?.id}">${strasseInstance?.plz?.encodeAsHTML()}</g:link></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:strasseInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${strasseInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

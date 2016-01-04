
<%@ page import="org.strotmann.plz.Postleitzahl" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'postleitzahl.label', default: 'Postleitzahl')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-postleitzahl" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-postleitzahl" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list postleitzahl">
			
				<g:if test="${postleitzahlInstance?.plz}">
				<li class="fieldcontain">
					<span id="plz-label" class="property-label"><g:message code="postleitzahl.plz.label" default="Plz" /></span>
					
						<span class="property-value" aria-labelledby="plz-label"><g:fieldValue bean="${postleitzahlInstance}" field="plz5"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${postleitzahlInstance?.grosskunde}">
				<li class="fieldcontain">
					<span id="bundesland-label" class="property-label"><g:message code="postleitzahl.grosskunde.label" default="Großkunde" /></span>
					
						<span class="property-value" aria-labelledby="grosskunde-label"><g:fieldValue bean="${postleitzahlInstance}" field="grosskunde"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${postleitzahlInstance?.osmId}">
				<li class="fieldcontain">
					<span id="osmId-label" class="property-label"><g:message code="postleitzahl.osmId.label" default="Osm Id" /></span>
					
						<span class="property-value" aria-labelledby="osmId-label"><g:fieldValue bean="${postleitzahlInstance}" field="osmId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${postleitzahlInstance?.ort}">
				<li class="fieldcontain">
					<span id="ort-label" class="property-label"><g:message code="postleitzahl.ort.label" default="Ort" /></span>
					
						<span class="property-value" aria-labelledby="ort-label"><g:fieldValue bean="${postleitzahlInstance}" field="ort"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${postleitzahlInstance?.bundesland}">
				<li class="fieldcontain">
					<span id="bundesland-label" class="property-label"><g:message code="postleitzahl.bundesland.label" default="Bundesland" /></span>
					
						<span class="property-value" aria-labelledby="bundesland-label"><g:fieldValue bean="${postleitzahlInstance}" field="bundeslandKlar"/></span>
					
				</li>
				</g:if>
				
			
			</ol>
			<g:form url="[resource:postleitzahlInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${postleitzahlInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

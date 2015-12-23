
<%@ page import="org.strotmann.plz.Sucher" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sucher.label', default: 'Sucher')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-sucher" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-sucher" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list sucher">
			
				<g:if test="${sucherInstance?.strasse}">
				<li class="fieldcontain">
					<span id="strasse-label" class="property-label"><g:message code="sucher.strasse.label" default="Strasse" /></span>
					
						<span class="property-value" aria-labelledby="strasse-label"><g:fieldValue bean="${sucherInstance}" field="strasse"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.hausnummer}">
				<li class="fieldcontain">
					<span id="hausnummer-label" class="property-label"><g:message code="sucher.hausnummer.label" default="Hausnummer" /></span>
					
						<span class="property-value" aria-labelledby="hausnummer-label"><g:fieldValue bean="${sucherInstance}" field="hausnummer"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.zusatz}">
				<li class="fieldcontain">
					<span id="zusatz-label" class="property-label"><g:message code="sucher.zusatz.label" default="Zusatz" /></span>
					
						<span class="property-value" aria-labelledby="zusatz-label"><g:fieldValue bean="${sucherInstance}" field="zusatz"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.postleitzahl}">
				<li class="fieldcontain">
					<span id="postleitzahl-label" class="property-label"><g:message code="sucher.postleitzahl.label" default="Postleitzahl" /></span>
					
						<span class="property-value" aria-labelledby="postleitzahl-label"><g:fieldValue bean="${sucherInstance}" field="postleitzahl"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.ort}">
				<li class="fieldcontain">
					<span id="ort-label" class="property-label"><g:message code="sucher.ort.label" default="Ort" /></span>
					
						<span class="property-value" aria-labelledby="ort-label"><g:fieldValue bean="${sucherInstance}" field="ort"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:sucherInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${sucherInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

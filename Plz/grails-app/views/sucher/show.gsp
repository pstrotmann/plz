
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
			<fieldset class="buttons">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="search" action="create"><g:message code="default.sucher.label"/></g:link></li>
			</ul>
			</fieldset>
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
						
				<g:if test="${sucherInstance?.hnrBis}">
				<li class="fieldcontain">
					<span id="hnrBis-label" class="property-label"><g:message code="sucher.hnrBis.label" default="Hnr Bis" /></span>
					
						<span class="property-value" aria-labelledby="hnrBis-label"><g:fieldValue bean="${sucherInstance}" field="hnrBis"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.hnrVon}">
				<li class="fieldcontain">
					<span id="hnrVon-label" class="property-label"><g:message code="sucher.hnrVon.label" default="Hnr Von" /></span>
					
						<span class="property-value" aria-labelledby="hnrVon-label"><g:fieldValue bean="${sucherInstance}" field="hnrVon"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.ortsteil}">
				<li class="fieldcontain">
					<span id="ortsteil-label" class="property-label"><g:message code="sucher.ortsteil.label" default="Ortsteil" /></span>
					
						<span class="property-value" aria-labelledby="ortsteil-label"><g:fieldValue bean="${sucherInstance}" field="ortsteil"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.strasseId}">
				<li class="fieldcontain">
					<span id="strasseId-label" class="property-label"><g:message code="sucher.strasseId.label" default="Strasse Id" /></span>
					
						<span class="property-value" aria-labelledby="strasseId-label"><g:fieldValue bean="${sucherInstance}" field="strasseId"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.zusBis}">
				<li class="fieldcontain">
					<span id="zusBis-label" class="property-label"><g:message code="sucher.zusBis.label" default="Zus Bis" /></span>
					
						<span class="property-value" aria-labelledby="zusBis-label"><g:fieldValue bean="${sucherInstance}" field="zusBis"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherInstance?.zusVon}">
				<li class="fieldcontain">
					<span id="zusVon-label" class="property-label"><g:message code="sucher.zusVon.label" default="Zus Von" /></span>
					
						<span class="property-value" aria-labelledby="zusVon-label"><g:fieldValue bean="${sucherInstance}" field="zusVon"/></span>
					
				</li>
				</g:if>
			
			</ol>
			
			<g:each in="${sucherInstance?.adrParts}" var="adrPart">
				<p>${adrPart}</p>
			</g:each>
				
			<g:form url="[resource:sucherInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

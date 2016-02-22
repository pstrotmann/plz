
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
			
				<g:if test="${strasseInstance?.hausNrVon}">
				<li class="fieldcontain">
					<span id="hnrVon-label" class="property-label"><g:message code="strasse.hnrVon.label" default="Hausnr Von" /></span>
					
						<span class="property-value" aria-labelledby="hnrVon-label"><g:fieldValue bean="${strasseInstance}" field="hausNrVon"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.hausNrBis}">
				<li class="fieldcontain">
					<span id="hnrBis-label" class="property-label"><g:message code="strasse.hnrBis.label" default="Hausnr Bis" /></span>
					
						<span class="property-value" aria-labelledby="hnrBis-label"><g:fieldValue bean="${strasseInstance}" field="hausNrBis"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${strasseInstance?.postleitzahl}">
				<li class="fieldcontain">
					<span id="plz-label" class="property-label"><g:message code="strasse.plz.label" default="Plz" /></span>
					
						<span class="property-value" aria-labelledby="plz-label"><g:fieldValue bean="${strasseInstance}" field="postleitzahl"/></span>
					
				</li>
				</g:if>
				
				<g:if test="${strasseInstance?.ortsteil}">
				<li class="fieldcontain">
					<span id="ortsteil-label" class="property-label"><g:message code="strasse.ortsteil.label" default="Ort/Ortsteil" /></span>
					
						<span class="property-value" aria-labelledby="ortsteil-label"><g:fieldValue bean="${strasseInstance}" field="ortsteil.name"/></span>
					
				</li>
				<li class="fieldcontain">
					<span id="liegtIn-label" class="property-label"><g:message code="strasse.liegtIn.label" default="liegt in" /></span>
					
						<span class="property-value" aria-labelledby="liegtIn-label"><g:fieldValue bean="${strasseInstance}" field="ortsteil.liegtIn"/></span>
					
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

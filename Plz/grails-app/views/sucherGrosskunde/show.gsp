
<%@ page import="org.strotmann.plz.SucherGrosskunde" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'sucherGrosskunde.label', default: 'SucherGrosskunde')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
		<a href="#show-sucherGrosskunde" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
		<div class="nav" role="navigation">
			<ul>
				<li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
				<li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				<li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
			</ul>
		</div>
		<div id="show-sucherGrosskunde" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list sucherGrosskunde">
			
				<g:if test="${sucherGrosskundeInstance?.postleitzahl}">
				<li class="fieldcontain">
					<span id="postleitzahl-label" class="property-label"><g:message code="sucherGrosskunde.postleitzahl.label" default="Postleitzahl" /></span>
					
						<span class="property-value" aria-labelledby="postleitzahl-label"><g:fieldValue bean="${sucherGrosskundeInstance}" field="postleitzahl"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherGrosskundeInstance?.ort}">
				<li class="fieldcontain">
					<span id="ort-label" class="property-label"><g:message code="sucherGrosskunde.ort.label" default="Ort" /></span>
					
						<span class="property-value" aria-labelledby="ort-label"><g:fieldValue bean="${sucherGrosskundeInstance}" field="ort"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${sucherGrosskundeInstance?.grosskunde}">
				<li class="fieldcontain">
					<span id="grosskunde-label" class="property-label"><g:message code="sucherGrosskunde.grosskunde.label" default="Grosskunde" /></span>
					
						<span class="property-value" aria-labelledby="grosskunde-label"><g:fieldValue bean="${sucherGrosskundeInstance}" field="grosskunde"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form url="[resource:sucherGrosskundeInstance, action:'delete']" method="DELETE">
				<fieldset class="buttons">
					<g:link class="edit" action="edit" resource="${sucherGrosskundeInstance}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>

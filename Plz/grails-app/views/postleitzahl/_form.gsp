<%@ page import="org.strotmann.plz.Postleitzahl" %>



<div class="fieldcontain ${hasErrors(bean: postleitzahlInstance, field: 'plz', 'error')} required">
	<label for="plz">
		<g:message code="postleitzahl.plz.label" default="Plz" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="plz" type="number" value="${postleitzahlInstance.plz}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: postleitzahlInstance, field: 'osmId', 'error')} ">
	<label for="osmId">
		<g:message code="postleitzahl.osmId.label" default="Osm Id" />
		
	</label>
	<g:field name="osmId" type="number" value="${postleitzahlInstance.osmId}" />

</div>

<div class="fieldcontain ${hasErrors(bean: postleitzahlInstance, field: 'ort', 'error')} required">
	<label for="ort">
		<g:message code="postleitzahl.ort.label" default="Ort" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="ort" required="" value="${postleitzahlInstance?.ort}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postleitzahlInstance, field: 'bundesland', 'error')} required">
	<label for="bundesland">
		<g:message code="postleitzahl.bundesland.label" default="Bundesland" />
		<span class="required-indicator">*</span>
	</label>
	<g:select name="bundesland" from="${postleitzahlInstance.constraints.bundesland.inList}" value="${postleitzahlInstance?.bundesland}" valueMessagePrefix="bundesland.category"/>

</div>

<div class="fieldcontain ${hasErrors(bean: postleitzahlInstance, field: 'grosskunde', 'error')} ">
	<label for="grosskunde">
		<g:message code="postleitzahl.grosskunde.label" default="Großkunde" />
		
	</label>
	<g:textField name="grosskunde" value="${postleitzahlInstance.grosskunde}" />

</div>


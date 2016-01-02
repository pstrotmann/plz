<%@ page import="org.strotmann.plz.SucherGrosskunde" %>

<div class="fieldcontain ${hasErrors(bean: sucherGrosskundeInstance, field: 'ort', 'error')} ">
	<label for="ort">
		<g:message code="sucherGrosskunde.ort.label" default="Ort" />
		
	</label>
	<g:textField name="ort" value="${sucherGrosskundeInstance?.ort}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: sucherGrosskundeInstance, field: 'grosskunde', 'error')} ">
	<label for="grosskunde">
		<g:message code="sucherGrosskunde.grosskunde.label" default="Grosskunde" />
		
	</label>
	<g:textField name="grosskunde" value="${sucherGrosskundeInstance?.grosskunde}"/>

</div>


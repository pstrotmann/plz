<%@ page import="org.strotmann.plz.PlzFilter" %>



<div class="fieldcontain ${hasErrors(bean: plzFilterInstance, field: 'ort', 'error')} required">
	<label for="ort">
		<g:message code="plzFilter.ort.label" default="Ort" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="ort" required="" value="${plzFilterInstance?.ort}"/>

</div>


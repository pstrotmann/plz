<%@ page import="org.strotmann.plz.StrFilter" %>



<div class="fieldcontain ${hasErrors(bean: strFilterInstance, field: 'plz', 'error')} required">
	<label for="plz">
		<g:message code="strFilter.plz.label" default="Plz" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="plz" type="number" value="${strFilterInstance.plz}" required=""/>

</div>


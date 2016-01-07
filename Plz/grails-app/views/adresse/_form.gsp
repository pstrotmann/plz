<%@ page import="org.strotmann.plz.Adresse" %>



<div class="fieldcontain ${hasErrors(bean: adresseInstance, field: 'ort', 'error')} required">
	<label for="ort">
		<g:message code="adresse.ort.label" default="Ort" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="ort" required="" value="${adresseInstance?.ort}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: adresseInstance, field: 'hnr', 'error')} ">
	<label for="hnr">
		<g:message code="adresse.hnr.label" default="Hnr" />
		
	</label>
	<g:textField name="hnr" value="${adresseInstance?.hnr}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: adresseInstance, field: 'plz', 'error')} required">
	<label for="plz">
		<g:message code="adresse.plz.label" default="Plz" />
		<span class="required-indicator">*</span>
	</label>
	<g:field name="plz" type="number" value="${adresseInstance.plz}" required=""/>

</div>

<div class="fieldcontain ${hasErrors(bean: adresseInstance, field: 'str', 'error')} required">
	<label for="str">
		<g:message code="adresse.str.label" default="Str" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="str" required="" value="${adresseInstance?.str}"/>

</div>


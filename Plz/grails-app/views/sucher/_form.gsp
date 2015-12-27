<%@ page import="org.strotmann.plz.Sucher" %>



<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'strasse', 'error')} ">
	<label for="strasse">
		<g:message code="sucher.strasse.label" default="Strasse" />
		
	</label>
	<g:textField name="strasse" value="${sucherInstance?.strasse}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'hausnummer', 'error')} ">
	<label for="hausnummer">
		<g:message code="sucher.hausnummer.label" default="Hausnummer" />
		
	</label>
	<g:field name="hausnummer" type="number" value="${sucherInstance.hausnummer}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'postleitzahl', 'error')} ">
	<label for="postleitzahl">
		<g:message code="sucher.postleitzahl.label" default="Postleitzahl" />
		
	</label>
	<g:field name="postleitzahl" type="number" value="${sucherInstance.postleitzahl}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'ort', 'error')} ">
	<label for="ort">
		<g:message code="sucher.ort.label" default="Ort" />
		
	</label>
	<g:textField name="ort" value="${sucherInstance?.ort}"/>

</div>


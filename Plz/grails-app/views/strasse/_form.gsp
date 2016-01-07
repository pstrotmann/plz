<%@ page import="org.strotmann.plz.Strasse" %>



<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'strasse', 'error')} required">
	<label for="strasse">
		<g:message code="strasse.strasse.label" default="Strasse" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="strasse" required="" value="${strasseInstance?.strasse}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'hnrVon', 'error')} ">
	<label for="hnrVon">
		<g:message code="strasse.hnrVon.label" default="Hnr Von" />
		
	</label>
	<g:textField name="hausnrVon" value="${strasseInstance.hausNrVon}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'hnrBis', 'error')} ">
	<label for="hnrBis">
		<g:message code="strasse.hnrBis.label" default="Hnr Bis" />
		
	</label>
	<g:textField name="hausnrBis" value="${strasseInstance.hausNrBis}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'plz', 'error')} required">
	<label for="plz">
		<g:message code="strasse.plz.label" default="Plz" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="plz" name="plz.plz" from="${org.strotmann.plz.Postleitzahl.sortiertNachPlz}" optionKey="id" required="" value="${strasseInstance?.plz}" class="many-to-one"/>

</div>


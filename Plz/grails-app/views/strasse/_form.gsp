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
	<g:field name="hnrVon" type="number" value="${strasseInstance.hnrVon}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'zusVon', 'error')} ">
	<label for="zusVon">
		<g:message code="strasse.zusVon.label" default="Zus Von" />
		
	</label>
	<g:textField name="zusVon" value="${strasseInstance?.zusVon}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'hnrBis', 'error')} ">
	<label for="hnrBis">
		<g:message code="strasse.hnrBis.label" default="Hnr Bis" />
		
	</label>
	<g:field name="hnrBis" type="number" value="${strasseInstance.hnrBis}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'zusBis', 'error')} ">
	<label for="zusBis">
		<g:message code="strasse.zusBis.label" default="Zus Bis" />
		
	</label>
	<g:textField name="zusBis" value="${strasseInstance?.zusBis}"/>

</div>

<div class="fieldcontain ${hasErrors(bean: strasseInstance, field: 'plz', 'error')} required">
	<label for="plz">
		<g:message code="strasse.plz.label" default="Plz" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="plz" name="plz.id" from="${org.strotmann.plz.Postleitzahl.sortiertNachPlz}" optionKey="id" required="" value="${strasseInstance?.plz?.id}" class="many-to-one"/>

</div>


<%@ page import="org.strotmann.plz.Sucher" %>
<%@ page import="org.strotmann.plz.Postleitzahl" %>


<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'ort', 'error')} ">
	<label for="ort">
		<g:message code="sucher.ort.label" default="Ort" />
		
	</label>
<%--	<g:textField name="ort" value="${sucherInstance?.ort}" />--%>
		<g:remoteField name="ort" value="${sucherInstance?.ort}" onchange="ortChanged(this.value);"/>
<%--	<g:select name="ort" from="${Postleitzahl.orte}" value="${sucherInstance?.ort}" valueMessagePrefix="sucher.ort" noSelection="['': '']"/>--%>
	<span id="subContainer"></span>
</div>

<script>
     function ortChanged(ort) {
         <g:remoteFunction controller="sucher" action="ortChanged"
             update="subContainer"
             params="'ort='+ort"/>
     }
</script>

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




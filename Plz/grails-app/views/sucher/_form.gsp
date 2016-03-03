<%@ page import="org.strotmann.plz.Sucher" %>

<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'ort', 'error')} ">
	<label for="ort">
		<g:message code="sucher.ort.label" default="Ort" />
		
	</label>
		<g:remoteField name="ort" value="${sucherInstance?.ort}" onchange="ortChanged(this.value);"/>
	<span id="subOrt"></span>
</div>

<script>
     function ortChanged(ort) {
         <g:remoteFunction controller="sucher" action="ortChanged"
             update="subOrt"
             params="'ort='+ort"/>
     }
</script>

<div class="fieldcontain ${hasErrors(bean: sucherInstance, field: 'strasse', 'error')} ">
	<label for="strasse">
		<g:message code="sucher.strasse.label" default="Strasse" />
		
	</label>
		<g:remoteField name="strasse" value="${sucherInstance?.strasse}" onchange="strChanged(this.value);"/>
	<span id="subStr"></span>
</div>

<script>
     function strChanged(str) {
         <g:remoteFunction controller="sucher" action="strChanged"
             update="subStr"
             params="'str='+str"/>
     }
</script>

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




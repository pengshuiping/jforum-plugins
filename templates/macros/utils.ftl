<#-- ********************************************** -->
<#-- Replace the @ and "." chars with another thing -->
<#-- ********************************************** -->
<#macro convertEmail email>
	<#assign e = email.split("@")/>
	<script language="javascrip"'>
		document.write('<A href="mailto:');
		document.write('${e[0]}');
		document.write('&#64;');
		document.write('${e[1]}');
		document.write('">');
	</script>
	<img src="${contextPath}/templates/${templateName}/images/icon_email.gif" border=0></A>
</#macro>
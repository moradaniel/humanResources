<#import "/WEB-INF/views/spring.ftl" as spring />

<#macro required on=true >
	<#if on>
		<span style="color:red">&nbsp;*</span>
	</#if>
</#macro>

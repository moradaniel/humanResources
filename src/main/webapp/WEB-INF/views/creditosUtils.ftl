
<#function canIngresarAscenderBorrarMovimientoPorUsuario account  >
	<#-- if ( account?exists) >
		<#if (account.name == 'usuario20' ||
			account.name == 'usuario15') ||
			account.name == 'usuario32' ||
			account.name == 'usuario27' || 
			account.name == 'usuario19'
		>
		 	<#return true>
		 </#if>
	</#if -->

	<#return true>
</#function>


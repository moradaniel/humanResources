
<#import "/WEB-INF/views/spring.ftl" as spring />


<div id="reportSetup">


	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Reporte Creditos de Reparticion:  ${reparticion.nombre}</h1>
	</div>
	
	<form id="setupReportForm" name="setupReportForm" 
			action="${requestContext.contextPath}/reports/creditos" 
			method="post"
			target="_blank">


	<table border="0">
	
		<tr>
			<td colspan="3">
				<input class="button" type="submit" value="Generar Reporte" />
			</td>
		</tr>
	</table>
</form>
	
</div>	


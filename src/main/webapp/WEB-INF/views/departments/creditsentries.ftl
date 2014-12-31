<#import "/WEB-INF/views/spring.ftl" as spring />
<#import "/WEB-INF/views/creditosUtils.ftl" as creditosUtils />


<div id="creditsEntriesDetails">
<p>
	<#if !currentDepartment?exists>
		No ha seleccionado reparticion.
	<#else>
		
		<#assign departmentsUrl= requestContext.contextPath+"/departments">
	
	
	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Reparticion:  ${currentDepartment.name}</h1>
	</div>
	<!-- end page-heading -->
	
	
	<form id="cambioMultipleEstadoMovimientoForm" name="cambioMultipleEstadoMovimientoForm" action="${requestContext.contextPath}/departments/creditsentries/processCambiarMultipleEstadoMovimiento" method="post">
	
	<#if canAccountChangeCreditsEntryStatusOfPeriod>
		<input id="saveButton" name="saveButton" class="button" type="submit" value="Guardar" />
	</#if>
	
	<table border="0" width="100%" cellpadding="0" cellspacing="0" id="content-table">
	<tr>
		<th rowspan="3" class="sized"><img src="${requestContext.contextPath}/resources/images/admin/shared/side_shadowleft.jpg" width="20" height="300" alt="" /></th>
		<th class="topleft"></th>
		<td id="tbl-border-top">&nbsp;</td>
		<th class="topright"></th>
		<th rowspan="3" class="sized"><img src="${requestContext.contextPath}/resources/images/admin/shared/side_shadowright.jpg" width="20" height="300" alt="" /></th>
	</tr>
	<tr>
		<td id="tbl-border-left"></td>
		<td>
		<!--  start content-table-inner ...................................................................... START -->
		<div id="content-table-inner">
		
		<!-- boton reporte de creditos -->
			<div id="table-actions">
				<#if (showReportGenerationButton)>
					<!-- p class="buttoniseUs" -->
						<a href="${requestContext.contextPath}/reports/reportSetup" class="btn btn-default btn-xs" role="button">Generar Reporte</a>

					<!-- /p -->
				<#else>
					<#if notAllowedReasons?has_content>
						<div id="message-red">
						
							<table border="0" width="100%" cellpadding="0" cellspacing="0">
								<tr>
									<td class="red-left"><@spring.message "msg.cannotGenerateReport" /></td>
								</tr>
								
								<#list notAllowedReasons as reason>
									<tr>
										<td class="red-left"><@spring.message ("msg."+reason) /></td>
									</tr>
								</#list>
								
							</table>
						</div>
					</#if>	
				</#if>	
			</div>

		
			<!--  start table-content  -->
			<#-- div id="table-content" -->
			
				<#-- table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table" -->
				<table class="table table-bordered table-striped">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th>Periodo</th>
						<th>Apellido y Nombre</th>
						<th>Tipo Movimiento</th>
						<th>Estado</th>
						<th>Fecha Inicio</th>
						<th>Fecha Fin</th>
						<th>Creditos</th>
						<th>Categoria Actual</th>
						<th>Categoria Propuesta</th>
						<th>Codigo Centro</th>
						<th>Nombre Centro</th>
						<th>Codigo Sector</th>
						<th>Nombre Sector</th>
						<th>Tramo</th>
						<th>Agrupamiento</th>
						<th>Observaciones</th>
						<th>Accion</th>
						
					</tr>
				

		    <#list creditsEntries as entry>
		        <#assign trStyle= "" >
			   	<#if (entry_index % 2) == 0>
			   		<#assign trStyle= "alternate-row" >
			   	</#if>	
			   	
			   	
		    	<#-- assign mostrarActionBorrarMovimiento = false / -->
		    	<#-- if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account) -->

			    		<#assign mostrarActionBorrarMovimiento = entry.canBeDeleted />
	
				<#-- /if -->
				

				<tr class="${trStyle}">
					<td>${entry.creditsEntry.creditsPeriod.name}</td>
					<td>${entry.creditsEntry.employment.person.apellidoNombre}</td>
					<td>${entry.creditsEntry.creditsEntryType}</td>
					<td>
						<#if entry.canAccountChangeCreditsEntryStatus>
							<@spring.formSingleSelect "cambiosMultiplesEstadoMovimientosForm.movimientos[${entry_index}].grantedStatus", grantedStatuses, "" />
							<@spring.formHiddenInput "cambiosMultiplesEstadoMovimientosForm.movimientos[${entry_index}].id" />	
						<#else>
							${entry.creditsEntry.grantedStatus}
						</#if>	
					</td>
					<td>${entry.creditsEntry.employment.startDate!""}</td>
					<td>${entry.creditsEntry.employment.endDate!""}</td>
					<td>${entry.creditsEntry.numberOfCredits}</td>
					
					<td>${entry.currentCategory?default("")}</td>
					
					<td>${entry.proposedCategory?default("")}</td>
					
					<td>${entry.creditsEntry.employment.subDepartment.codigoCentro}</td>
					<td>${entry.creditsEntry.employment.subDepartment.nombreCentro}</td>
					<td>${entry.creditsEntry.employment.subDepartment.codigoSector}</td>
					<td>${entry.creditsEntry.employment.subDepartment.nombreSector}</td>
					
					<td>
						<#if entry.creditsEntry.employment.occupationalGroup?exists >
							${entry.creditsEntry.employment.occupationalGroup.name} - ${entry.creditsEntry.employment.occupationalGroup.code}
						</#if>
					</td>
							
					<td>
						<#if entry.creditsEntry.employment.occupationalGroup?exists && entry.creditsEntry.employment.occupationalGroup.parentOccupationalGroup?exists>
							${entry.creditsEntry.employment.occupationalGroup.parentOccupationalGroup.name} - ${entry.creditsEntry.employment.occupationalGroup.parentOccupationalGroup.code}
						</#if>
					</td>
					
					<td>${entry.creditsEntry.notes?default("")}</td>
					<td>
						<#if mostrarActionBorrarMovimiento>
							<a href="${departmentsUrl}/department/creditsentries/${entry.creditsEntry.id}/delete" class="ajaxLink">Borrar Movimiento</a>
						</#if>
						
						<#if entry.canAccountChangeCreditsEntryStatus>
							<#-- a href="${departmentsUrl}/creditsentries/${entry.creditsEntry.id}/setupFormCambiarEstadoMovimientoCreditos" class="ajaxLink">Cambiar Estado Movimiento</a -->
						</#if>
					</td>
				</tr>
			</#list>
			<#if !creditsEntries?has_content>
				<tr>
					<td colspan="5">No se encontraron movimientos</td>
				</tr>
			</#if>
			

				</table>
				<!--  end product-table................................... --> 
			
			
			
			</div> 
			<!--  end content-table  -->
			
				<div class="clear"></div>
		 
		</div>
		<!--  end content-table-inner ............................................END  -->
		</td>
		<td id="tbl-border-right"></td>
	</tr>
	<tr>
		<th class="sized bottomleft"></th>
		<td id="tbl-border-bottom">&nbsp;</td>
		<th class="sized bottomright"></th>
	</tr>
	</table>
	</form>
	<div class="clear">&nbsp;</div
	 
	</#if>
</p>
</div>

<#-- script>
	$(function() {
		$("input:submit, a, button", ".buttoniseUs").button();
	});
</script -->

<script>
	$(function() {
		$(document).ready(function() { 
			Utils.blockUIonClick($('#saveButton'));
		});
	});
</script>
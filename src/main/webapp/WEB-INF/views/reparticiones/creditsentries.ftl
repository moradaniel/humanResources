<#import "/WEB-INF/views/spring.ftl" as spring />
<#import "/WEB-INF/views/creditosUtils.ftl" as creditosUtils />


<div id="movimientoDetails">
<p>
	<#if !reparticion?exists>
		No ha seleccionado reparticion.
	<#else>
		
		<#assign reparticionesUrl= requestContext.contextPath+"/reparticiones">
	
	
		<!--  start page-heading -->
	<div id="page-heading">
		<h1>Reparticion:  ${reparticion.nombre}</h1>
	</div>
	<!-- end page-heading -->
	
	
	<form id="cambioMultipleEstadoMovimientoForm" name="cambioMultipleEstadoMovimientoForm" action="${requestContext.contextPath}/reparticiones/creditsentries/processCambiarMultipleEstadoMovimiento" method="post">
	
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
					<p class="buttoniseUs">
						<a href="${requestContext.contextPath}/reports/reportSetup">Generar Reporte</a>
					</p>
				<#else>
					<#if hasPermissionToGenerateReport?exists>
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
			<div id="table-content">
			
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th class="table-header-repeat line-left"><a href="">Periodo</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Apellido y Nombre</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Condicion Agente</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Tipo Movimiento</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Estado</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Fecha Inicio</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Fecha Fin</a></th>
						<th class="table-header-repeat line-left "><a href="">Creditos</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Categoria Actual</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Categoria Propuesta</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Centro</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Centro</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Tramo</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Agrupamiento</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Observaciones</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
						
					</tr>
				

		    <#list creditsEntries as entry>
		        <#assign trStyle= "" >
			   	<#if (entry_index % 2) == 0>
			   		<#assign trStyle= "alternate-row" >
			   	</#if>	
			   	
			   	
		    	<#assign mostrarActionBorrarMovimiento = false />
		    	<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>

			    		<#assign mostrarActionBorrarMovimiento = entry.canAccountBorrarMovimiento/>
	
				</#if>
				

				<tr class="${trStyle}">
					<td>${entry.creditsEntry.creditsPeriod.name}</td>
					<td>${entry.creditsEntry.employment.person.apellidoNombre}</td>
					<td>${entry.creditsEntry.employment.person.condition?default("")}</td>
					<td>${entry.creditsEntry.creditsEntryType}</td>
					<td>
						<#if entry.canAccountChangeCreditsEntryStatus>
							<@spring.formSingleSelect "cambiosMultiplesEstadoMovimientosForm.movimientos[${entry_index}].grantedStatus", grantedStatuses, "" />
							<@spring.formHiddenInput "cambiosMultiplesEstadoMovimientosForm.movimientos[${entry_index}].id" />	
						<#else>
							${entry.creditsEntry.grantedStatus}
						</#if>	
					</td>
					<td>${entry.creditsEntry.employment.fechaInicio!""}</td>
					<td>${entry.creditsEntry.employment.fechaFin!""}</td>
					<td>${entry.creditsEntry.cantidadCreditos}</td>
					
					<td>${entry.currentCategory?default("")}</td>
					
					<td>${entry.proposedCategory?default("")}</td>
					
					<td>${entry.creditsEntry.employment.centroSector.codigoCentro}</td>
					<td>${entry.creditsEntry.employment.centroSector.nombreCentro}</td>
					<td>${entry.creditsEntry.employment.centroSector.codigoSector}</td>
					<td>${entry.creditsEntry.employment.centroSector.nombreSector}</td>
					
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
					
					<td>${entry.creditsEntry.observaciones?default("")}</td>
					<td>
						<#if mostrarActionBorrarMovimiento>
							<a href="${reparticionesUrl}/reparticion/creditsentries/${entry.creditsEntry.id}/borrar" class="ajaxLink">Borrar Movimiento</a>
						</#if>
						
						<#if entry.canAccountChangeCreditsEntryStatus>
							<#-- a href="${reparticionesUrl}/creditsentries/${entry.creditsEntry.id}/setupFormCambiarEstadoMovimientoCreditos" class="ajaxLink">Cambiar Estado Movimiento</a -->
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

<script>
	$(function() {
		$("input:submit, a, button", ".buttoniseUs").button();
	});
</script>

<script>
	$(function() {
		$(document).ready(function() { 
			Utils.blockUIonClick($('#saveButton'));
		});
	});
</script>
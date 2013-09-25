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
	
	
	<form id="cambioMultipleEstadoMovimientoForm" name="cambioMultipleEstadoMovimientoForm" action="${requestContext.contextPath}/reparticiones/movimientos/processCambiarMultipleEstadoMovimiento" method="post">
	
	<#if canAccountCambiarEstadoMovimiento>
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
				<#if (creditosDisponiblesSegunSolicitadoPeriodoActual >= 0)>
					<p class="buttoniseUs">
						<a href="${requestContext.contextPath}/reports/buildSetupPage">Generar Reporte</a>
					</p>
				<#else>
					<div id="message-red">
						<table border="0" width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td class="red-left">No se puede generar el reporte de creditos teniendo saldo negativo.</td>
							</tr>
						</table>
					</div>
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
						<th class="table-header-repeat line-left minwidth-1"><a href="">Observaciones</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
						
					</tr>
				

		    <#list movimientos as movimiento>
		        <#assign trStyle= "" >
			   	<#if (movimiento_index % 2) == 0>
			   		<#assign trStyle= "alternate-row" >
			   	</#if>	
			   	
			   	
		    	<#assign mostrarActionBorrarMovimiento = false />
		    	<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>

			    		<#assign mostrarActionBorrarMovimiento = movimiento.canAccountBorrarMovimiento/>
	
				</#if>
				

				<tr class="${trStyle}">
					<td>${movimiento.movimientoCreditos.creditsPeriod.name}</td>
					<td>${movimiento.movimientoCreditos.empleo.agente.apellidoNombre}</td>
					<td>${movimiento.movimientoCreditos.empleo.agente.condicion?default("")}</td>
					<td>${movimiento.movimientoCreditos.tipoMovimientoCreditos}</td>
					<td>
						<#if movimiento.canAccountCambiarEstadoMovimiento>
							<@spring.formSingleSelect "cambiosMultiplesEstadoMovimientosForm.movimientos[${movimiento_index}].grantedStatus", grantedStatuses, "" />
							<@spring.formHiddenInput "cambiosMultiplesEstadoMovimientosForm.movimientos[${movimiento_index}].id" />	
						<#else>
							${movimiento.movimientoCreditos.grantedStatus}
						</#if>	
					</td>
					<td>${movimiento.movimientoCreditos.empleo.fechaInicio!""}</td>
					<td>${movimiento.movimientoCreditos.empleo.fechaFin!""}</td>
					<td>${movimiento.movimientoCreditos.cantidadCreditos}</td>
					
					<td>${movimiento.categoriaActual?default("")}</td>
					
					<td>${movimiento.categoriaPropuesta?default("")}</td>
					
					<td>${movimiento.movimientoCreditos.empleo.centroSector.codigoCentro}</td>
					<td>${movimiento.movimientoCreditos.empleo.centroSector.nombreCentro}</td>
					<td>${movimiento.movimientoCreditos.empleo.centroSector.codigoSector}</td>
					<td>${movimiento.movimientoCreditos.empleo.centroSector.nombreSector}</td>
					<td>${movimiento.movimientoCreditos.observaciones?default("")}</td>
					<td>
						<#if mostrarActionBorrarMovimiento>
							<a href="${reparticionesUrl}/reparticion/movimientos/${movimiento.movimientoCreditos.id}/borrar" class="ajaxLink">Borrar Movimiento</a>
						</#if>
						
						<#if movimiento.canAccountCambiarEstadoMovimiento>
							<#-- a href="${reparticionesUrl}/movimientos/${movimiento.movimientoCreditos.id}/setupFormCambiarEstadoMovimientoCreditos" class="ajaxLink">Cambiar Estado Movimiento</a -->
						</#if>
					</td>
				</tr>
			</#list>
			<#if !movimientos?has_content>
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
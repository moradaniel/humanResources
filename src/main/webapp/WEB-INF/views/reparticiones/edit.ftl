
<#import "/WEB-INF/views/spring.ftl" as spring />
<#import "/WEB-INF/views/creditosUtils.ftl" as creditosUtils />


<div id="empleoResults">
<#if !empleosActivos?has_content>
	<p>No se encontraron Agentes. Por favor cambie el criterio de busqueda.</p>
<#else>

	<#assign empleosUrl= requestContext.contextPath+"/empleos">

		<!--  start page-heading -->
	<div id="page-heading">
		<h1>Reparticion:  ${reparticion.nombre}</h1>
	</div>
	<!-- end page-heading -->
	
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
			<div id="table-actions">
				<p class="buttoniseUs">
					<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>
						<a href="${requestContext.contextPath}/empleos/ingresarPropuestaAgenteForm">Ingresar Agente</a>
					</#if>
				</p>
			</div>	
		
			<!--  start table-content  -->
			<div id="table-content">
			
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th class="table-header-repeat line-left minwidth-1"><a href="">Apellido y Nombre</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Condicion Agente</a>	</th>
						<th class="table-header-repeat line-left "><a href="">Categoria</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Centro</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Centro</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
	
					</tr>
									

				<#list empleosActivos as empleoActivo>
					<#assign trStyle= "" >
					<#if (empleoActivo_index % 2) == 0>
				    	<#assign trStyle= "alternate-row" >
				    </#if>	
				    
				    	<tr class="${trStyle}">
							<td>${empleoActivo.agente.apellidoNombre}</td>
							<td>${empleoActivo.agente.condicion?default("")}</td>
							<td>${empleoActivo.categoria.codigo}</td>
							<td>${empleoActivo.centroSector.codigoCentro}</td>
							<td>${empleoActivo.centroSector.nombreCentro}</td>
							<td>${empleoActivo.centroSector.codigoSector}</td>
							<td>${empleoActivo.centroSector.nombreSector}</td>
							<#-- td>${empleo.address}</td>
							<td>${empleo.city}, ${empleo.state}, ${empleo.country}</td>
							<td>${empleo.zip}</td-->
							<td>
								
								<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>
									<#if !empleoActivo.agente.hasMovimientosAscensoPendientes() >
										<a href="${empleosUrl}/${empleoActivo.id}/cambiarCategoria" class="ajaxLink">Cambiar Categoria</a>
									</#if>
								</#if>
								<#--	&nbsp;&nbsp;&nbsp;
								<a href="${empleosUrl}/${empleo.id}/baja" class="ajaxLink">Baja</a> -->
							
							</td>
						</tr>
				    
				
			</#list>
			<#if !empleosActivos?has_content>
				<tr>
					<td colspan="5">No se encontraron agentes</td>
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
	<div class="clear">&nbsp;</div
	
</#if>
</div>	

<script>
	$(function() {
		$("input:submit, a, button", ".buttoniseUs").button();
	});
</script>

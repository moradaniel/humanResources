
<#import "/WEB-INF/views/spring.ftl" as spring />
<#import "/WEB-INF/views/creditosUtils.ftl" as creditosUtils />


<div id="employmentResults">
<#if !activeEmployments?has_content>
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
						<#if canAccountProposeNewEmployment>
							<a href="${requestContext.contextPath}/empleos/proposeNewEmploymentForm">Ingresar Agente</a>
						</#if>
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
						<th class="table-header-repeat line-left "><a href="">Tramo</a></th>
						<th class="table-header-repeat line-left "><a href="">Agrupamiento</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Centro</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Centro</a></th>
						<th class="table-header-repeat line-left "><a href="">Codigo Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Nombre Sector</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
	
					</tr>
									

				<#list activeEmployments as activeEmployment>
					<#assign trStyle= "" >
					<#if (activeEmployment_index % 2) == 0>
				    	<#assign trStyle= "alternate-row" >
				    </#if>	
				    
				    	<tr class="${trStyle}">
							<td>${activeEmployment.employment.person.apellidoNombre}</td>
							<td>${activeEmployment.employment.person.condicion?default("")}</td>
							<td>${activeEmployment.employment.category.code}</td>
							
							
							<td>
								<#if activeEmployment.employment.occupationalGroup?exists >
									${activeEmployment.employment.occupationalGroup.name} - ${activeEmployment.employment.occupationalGroup.code}
								<#else>
									<span class="error-text">Error Falta asignar Tramo</span>								
								</#if>
								
							</td>
							
							<td>
								<#if activeEmployment.employment.occupationalGroup?exists && activeEmployment.employment.occupationalGroup.parentOccupationalGroup?exists>
									${activeEmployment.employment.occupationalGroup.parentOccupationalGroup.name}
								</#if>
							</td>
							
							<td>${activeEmployment.employment.centroSector.codigoCentro}</td>
							<td>${activeEmployment.employment.centroSector.nombreCentro}</td>
							<td>${activeEmployment.employment.centroSector.codigoSector}</td>
							<td>${activeEmployment.employment.centroSector.nombreSector}</td>
							<td>
							
							<#if activeEmployment.employment.occupationalGroup?exists >
								<#if creditosUtils.canIngresarAscenderBorrarMovimientoPorUsuario(account)>
									<#if activeEmployment.canAccountPromotePerson >
										<a href="${empleosUrl}/${activeEmployment.employment.id}/promotePerson" class="ajaxLink">Ascender Agente</a>
									</#if>
								</#if>
							</#if>
								<#--	&nbsp;&nbsp;&nbsp;
								<a href="${empleosUrl}/${empleo.id}/baja" class="ajaxLink">Baja</a> -->
							
							</td>
						</tr>
				    
				
			</#list>
			<#if !activeEmployments?has_content>
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

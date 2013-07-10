
<div id="reparticionDetails">

	<#if !reparticion?exists>
		No ha seleccionado reparticion.
	<#else>
	 
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
		
			<!--  start table-content  -->
			<div id="table-content">
			
				<table border="0" width="100%" cellpadding="0" cellspacing="0" id="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Por Carga Inicial</a>	</th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Por Baja</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Por Ingresos o Ascensos</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Disponibles</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
					</tr>
					
					<tr>
						<td>${creditosPorCargaInicialDeReparticion}</td>
						<td>${creditosPorBaja}</td>
						<td>${creditosPorIngresosOAscensos}</td>
						<td>${creditosDisponibles}</td>
						<td>						
						<a href="showEmpleos" class="ajaxLink">Ver Agentes Activos</a>
						<br/>
						<a href="showMovimientos" class="ajaxLink">Ver Movimientos de Credito</a>
						</td>
					</tr>

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
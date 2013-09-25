
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
			
				<h2>Periodo 2013</h2>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th class="table-header-repeat line-left minwidth-1"><a href="">Saldo Disponible al Inicio de 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Acreditados Por Bajas durante 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Consumidos Por Ingresos o Ascensos(Solicitados) durante 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Consumidos Por Ingresos o Ascensos(Otorgados) durante 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Disponibles segun Solicitado durante 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Disponibles segun Otorgado durante 2013</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
					</tr>
					
					<tr>
						<td>${creditosDisponiblesInicioPeriodoActual?default("0")}</td>
						<td>${creditosAcreditadosPorBajaDurantePeriodoActual?default("0")}</td>
						<td>${creditosConsumidosPorIngresosOAscensosSolicitadosPeriodoActual?default("0")}</td>
						<td>${creditosPorIngresosOAscensosOtorgadosPeriodoActual?default("0")}</td>
						<td>${creditosDisponiblesSegunSolicitadoPeriodoActual?default("0")}</td>
						<td>${creditosDisponiblesSegunOtorgadoPeriodoActual?default("0")}</td>
						<td>						
						<a href="showEmpleos" class="ajaxLink">Ver Agentes Activos</a>
						<br/>
						<a href="showMovimientos/2013" class="ajaxLink">Ver Movimientos de Credito</a>
						</td>
					</tr>

				</table>
				
				<h2>Historicos 2012</h2>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos por Carga Inicial 2012</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Acreditados Por Bajas durante 2012</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Consumidos Por Ingresos o Ascensos Otorgados 2012</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Saldo de Creditos al Final del Periodo 2012</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th>
					</tr>
					
					<tr>
						<td>${creditosAcreditadosPorCargaInicial2012?default("0")}</td>
						<td>${creditosAcreditadosPorBajas2012?default("0")}</td>
						<td>${creditosConsumidosPorIngresosOAscensosOtorgados2012?default("0")}</td>
						<td>${saldoCreditosAlFinalPeriodo2012?default("0")}</td>
						<td><a href="showMovimientos/2012" class="ajaxLink">Ver Movimientos de Credito</a></td>
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
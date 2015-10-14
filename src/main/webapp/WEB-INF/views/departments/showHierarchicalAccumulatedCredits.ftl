
<div id="departmentDetails">

	<#if !currentDepartment?exists>
		No ha seleccionado reparticion.
	<#else>
	 
	 	<!--  start page-heading -->
	<div id="page-heading">
		<h1>Reparticion:  ${currentDepartment.name}</h1>
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

			
				<h2>Creditos retenidos 2014</h2>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Saldo Disponible al Inicio de ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Acreditados Por Bajas durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Retenidos durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Total Creditos Disponibles al Inicio de ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Consumidos Por Ingresos o Ascensos(Solicitados) durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Consumidos Por Ingresos o Ascensos(Otorgados) durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Disponibles segun Solicitado durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Creditos Disponibles segun Otorgado durante ${currentPeriodSummaryData.year}</a></th>
						<th class="table-header-repeat line-left minwidth-1"><a href="">Accion</a></th -->
					</tr>
					
					<#list departmentsList as departmentData>
    					<tr>
    						<td>${departmentData?default("")}</td>
    						<#-- td>${currentPeriodSummaryData.creditosAcreditadosPorBajaDurantePeriodo?default("0")}</td>
    						<td>${currentPeriodSummaryData.retainedCredits?default("0")}</td>
    						<td>${currentPeriodSummaryData.totalAvailableCredits?default("0")}</td>
    						<td>${currentPeriodSummaryData.creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo?default("0")}</td>
    						<td>${currentPeriodSummaryData.creditosPorIngresosOAscensosOtorgadosPeriodo?default("0")}</td>
    						<td>${currentPeriodSummaryData.creditosDisponiblesSegunSolicitadoPeriodo?default("0")}</td>
    						<td>${currentPeriodSummaryData.creditosDisponiblesSegunOtorgadoPeriodo?default("0")}</td>
    						<td>						
    						<a href="showEmployments" class="ajaxLink">Ver Agentes Activos</a>
    						<br/>
    						<a href="showCreditEntries/${currentPeriodSummaryData.year}" class="ajaxLink">Ver Movimientos de Credito</a>
    						</td -->
    					</tr>
                    </#list>
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
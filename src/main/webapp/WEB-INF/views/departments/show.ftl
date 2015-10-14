
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

			
				<h2>Periodo ${currentPeriodSummaryData.year}</h2>
				<#-- table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table" -->
				<table class="table table-bordered">
					<tr>
						<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
						<th>Saldo Disponible al Inicio de ${currentPeriodSummaryData.year}</th>
						<th>Creditos Acreditados Por Bajas durante ${currentPeriodSummaryData.year}</th>
						<th>Creditos Retenidos durante ${currentPeriodSummaryData.year}</th>
						<th>Total Creditos Disponibles al Inicio de ${currentPeriodSummaryData.year}</th>
						<th>Creditos Consumidos Por Ingresos o Ascensos(Solicitados) durante ${currentPeriodSummaryData.year}</th>
						<th>Creditos Consumidos Por Ingresos o Ascensos(Otorgados) durante ${currentPeriodSummaryData.year}</th>
						<th>Creditos Disponibles segun Solicitado durante ${currentPeriodSummaryData.year}</th>
						<th>Creditos Disponibles segun Otorgado durante ${currentPeriodSummaryData.year}</th>

						<th>Accion</th>
					</tr>
					
					<tr>
						<td>${currentPeriodSummaryData.creditosDisponiblesInicioPeriodo?default("0")}</td>
						<td>${currentPeriodSummaryData.creditosAcreditadosPorBajaDurantePeriodo?default("0")}</td>
						<td>${currentPeriodSummaryData.retainedCredits?default("0")}</td>
						<td>${currentPeriodSummaryData.totalAvailableCredits?default("0")}</td>
						<td>${currentPeriodSummaryData.creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo?default("0")}</td>
						<td>${currentPeriodSummaryData.creditosPorIngresosOAscensosOtorgadosPeriodo?default("0")}</td>
						<td>${currentPeriodSummaryData.creditosDisponiblesSegunSolicitadoPeriodo?default("0")}</td>
						<td>${currentPeriodSummaryData.creditosDisponiblesSegunOtorgadoPeriodo?default("0")}</td>
		                    				
						<td>						
						<a href="showEmployments" class="ajaxLink">Ver Agentes Activos</a>   
						<br/>
						<#if account?exists && account.hasPermissions("Manage_CreditsEntries", "READ")>
						  <a href="showCreditEntries/${currentPeriodSummaryData.year}" class="ajaxLink">Ver Movimientos de Credito</a>
                        </#if>
						</td>
					</tr>

				</table>
				
				<table class="table table-bordered">
                    <tr>
                        <th>Ajuste Debito ${currentPeriodSummaryData.year}</th>
                        <th>Ajuste Credito ${currentPeriodSummaryData.year}</th>
                        <th>Reasignacion de Retencion ${currentPeriodSummaryData.year}</th>
                        
                        <th>Accion</th>
                    </tr>
                    
                    <tr>
                        <td>${currentPeriodSummaryData.totalCreditosReparticionAjustes_Debito?default("0")}</td>
                        <td>${currentPeriodSummaryData.totalCreditosReparticionAjustes_Credito?default("0")}</td>
                        <td>${currentPeriodSummaryData.totalCreditosReparticion_ReasignadosDeRetencion_Periodo?default("0")}</td>
                    </tr>

                </table>
				
				
				
				
				<#list historicPeriodsSummaryData as historicPeriodSummaryData>
					<h2>Historicos ${historicPeriodSummaryData.year}</h2>
					<#-- table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table" -->
					<table class="table table-bordered">
						<tr>
							<#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->
							<th>Creditos por Carga Inicial ${historicPeriodSummaryData.year}</th>
							<th>Saldo Disponible al Inicio de ${historicPeriodSummaryData.year}</th>
							<th>Creditos Acreditados Por Bajas durante ${historicPeriodSummaryData.year}</th>
							<th>Creditos Retenidos durante ${historicPeriodSummaryData.year}</th>
							<th>Creditos Consumidos Por Ingresos o Ascensos Otorgados ${historicPeriodSummaryData.year}</th>

							<th>Saldo de Creditos al Final del Periodo ${historicPeriodSummaryData.year}</th>
							<th>Accion</th>
						</tr>
						
						<tr>
							<td>${historicPeriodSummaryData.creditosAcreditadosPorCargaInicial?default("0")}</td>
							<td>${historicPeriodSummaryData.creditosDisponiblesInicioPeriodo?default("0")}</td>
							<td>${historicPeriodSummaryData.creditosAcreditadosPorBajas?default("0")}</td>
							<td>${historicPeriodSummaryData.creditosRetenidosPorBajas?default("0")}</td>
							<td>${historicPeriodSummaryData.creditosConsumidosPorIngresosOAscensosOtorgados?default("0")}</td>

							<td>${historicPeriodSummaryData.saldoCreditosAlFinalPeriodo?default("0")}</td>
                            <#if account?exists && account.hasPermissions("Manage_CreditsEntries", "READ")>
							 <td><a href="showCreditEntries/${historicPeriodSummaryData.year}" class="ajaxLink">Ver Movimientos de Credito</a></td>
                            </#if>
						</tr>
	
					</table>

                    <table class="table table-bordered">
                        <tr>
                            <th>Ajuste Debito ${historicPeriodSummaryData.year}</th>
                            <th>Ajuste Credito ${historicPeriodSummaryData.year}</th>
                            <th>Reasignacion de Retencion ${historicPeriodSummaryData.year}</th>
                            <th>Accion</th>
                        </tr>
                        
                        <tr>
                            <td>${historicPeriodSummaryData.totalCreditosReparticionAjustes_Debito?default("0")}</td>
                            <td>${historicPeriodSummaryData.totalCreditosReparticionAjustes_Credito?default("0")}</td>
                            <td>${historicPeriodSummaryData.totalCreditosReparticion_ReasignadosDeRetencion?default("0")}</td>
                        </tr>
    
                    </table>
				</#list>
				
				
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
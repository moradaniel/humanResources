

<div id="departmentResults">
<#if !currentPeriodDepartmentsSummaryData?has_content>
    <p>No se encontraron reparticiones. Por favor cambie el criterio de busqueda.</p>
<#else>

    <#assign departmentsUrl= requestContext.contextPath+"/departments">
    
    <!--  start page-heading -->
    <div id="page-heading">
        <h1>Resumen de Creditos de Reparticiones</h1>
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
            
                <#-- table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
                    <tr>
                        <th class="table-header-repeat line-left minwidth-1"><a href="">Nombre</a>  </th>
                        <th class="table-header-repeat line-left minwidth-1"><a href="">Acciones</a></th>
                    </tr>
                
                
                    <#list currentPeriodDepartmentsSummaryData as departmentSummaryData>
                        <#assign trStyle= "" >
                        <#if (departmentSummaryData_index % 2) == 0>
                            <#assign trStyle= "alternate-row" >
                        </#if>  
                        
                        <tr class="${trStyle}">
                            <td>${departmentSummaryData.department.name} </td>

                            <td><a href="${departmentsUrl}/${departmentSummaryData.department.id}" class="ajaxLink">Ver Reparticion</a></td>
                        </tr>
                    </#list>
                </table -->
                
                <table class="table table-bordered">
                    <tr>
                        <#-- th class="table-header-check"><a id="toggle-all" ></a> </th -->        
                        <th>Codigo</th>
                        <th>Reparticion</th>
                        <th>Saldo Disponible al Inicio del Periodo</th>
                        <th>Creditos Acreditados Por Bajas durante el Periodo</th>
                        <th>Creditos Retenidos durante el Periodo</th>
                        <th>Total Creditos Disponibles al Inicio del Periodo</th>
                        <th>Creditos Consumidos Por Ingresos o Ascensos(Solicitados) durante el Periodo</th>
                        <th>Creditos Consumidos Por Ingresos o Ascensos(Otorgados) durante el Periodo</th>
                        <th>Creditos Disponibles segun Solicitado durante el Periodo</th>
                        <th>Creditos Disponibles segun Otorgado durante el Periodo</th>
                    </tr>

                    <#list currentPeriodDepartmentsSummaryData as currentPeriodSummaryData>
                    
                        <tr>
                            <td>${currentPeriodSummaryData.department.code?default("")}</td>
                            <td>${currentPeriodSummaryData.department.name?default("")}</td>
                            <td>${currentPeriodSummaryData.creditosDisponiblesInicioPeriodo?default("0")}</td>
                            <td>${currentPeriodSummaryData.creditosAcreditadosPorBajaDurantePeriodo?default("0")}</td>
                            <td>${currentPeriodSummaryData.retainedCredits?default("0")}</td>
                            <td>${currentPeriodSummaryData.totalAvailableCredits?default("0")}</td>
                            <td>${currentPeriodSummaryData.creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo?default("0")}</td>
                            <td>${currentPeriodSummaryData.creditosPorIngresosOAscensosOtorgadosPeriodo?default("0")}</td>
                            <td>${currentPeriodSummaryData.creditosDisponiblesSegunSolicitadoPeriodo?default("0")}</td>
                            <td>${currentPeriodSummaryData.creditosDisponiblesSegunOtorgadoPeriodo?default("0")}</td>
  
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


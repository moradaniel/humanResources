
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
			<#-- div id="table-content">

			
				<h2>Creditos retenidos 2014</h2>
				<table border="0" width="100%" cellpadding="0" cellspacing="0" class="product-table">
					<tr>
					</tr>
					
					<#list departmentsList as departmentData>
    					<tr>
    						<td>${departmentData?default("")}</td>

    					</tr>
                    </#list>
				</table>
				
				
			
			</div -->
			
			
			<#-- ui-grid -->
            <h2>Creditos retenidos 2020</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2020" class="myGrid"></div>
            </div>

			<h2>Creditos retenidos 2019</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2019" class="myGrid"></div>
            </div>
            
			<h2>Creditos retenidos 2018</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2018" class="myGrid"></div>
            </div>

            <h2>Creditos retenidos 2017</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2017" class="myGrid"></div>
            </div>
            
            <h2>Creditos retenidos 2016</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2016" class="myGrid"></div>
            </div>
            
            <h2>Creditos retenidos 2015</h2>
            <div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2015" class="myGrid"></div>
            </div>
            
            <h2>Creditos retenidos 2014</h2>
			<div ng-controller="HierarchicalRetainedCreditsGridCtrl">
              <div ui-grid="gridOptions_2014" class="myGrid"></div>
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
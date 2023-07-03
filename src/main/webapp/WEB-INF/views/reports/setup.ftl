


<#import "/WEB-INF/views/spring.ftl" as spring />

<#import "/WEB-INF/views/reports/ReportMacros.ftl" as rm />

<#function showSinglePeriodSelector reportCode=''>
                 
        <#if ('EmployeeAdditionsPromotionsReport' == reportCode?default("") ||
                'CreditsEntriesReport' == reportCode?default("") ||
                'ResumenDeSaldosDeCreditosDeReparticionesReport' == reportCode?default("")  )>
                <#return true>
         </#if>     

         <#return false>
         
</#function>


<#function showFormatSelector reportCode=''>
                 
        <#if ( reportCode?has_content )>
               <#return true>
         </#if>     

         <#return false>
         
</#function>




<script type="text/javascript">

    function soloNumeros(evt){
        var charCode = (evt.which) ? evt.which : event.keyCode
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
          return false;
        }
        return true;
    }
            
	function process(action){
     	//account_hotels.onSubmit();

		//check if the user has selected a report
		var hasSelectedReport = ($('#reportCode').val().length) > 0;

		
		if(!hasSelectedReport){
			return false;
		}

		document.setupReportForm.action = action;
   		if (action == 'reportSetup') {
     		document.setupReportForm.target="_self";
			document.setupReportForm.submit();
		}
		else {
			document.setupReportForm.target="_blank";
			//if (validate(document.setupReportForm.startDate,document.setupReportForm.stopDate,document.setupReportForm.hotelCodes,document.setupReportForm.reportCode.options[document.setupReportForm.reportCode.options.selectedIndex]) == true) {
				/*if((document.setupReportForm.reportCode.value=='EmployeeAdditionsPromotionsReport') 
					&& document.setupReportForm.selectedOutputFormat.value=='XLS'){
					document.setupReportForm.action= 'ReportRunExcel';
				}*/
				

				
				document.setupReportForm.submit();
			//}else {
			//	return false;
			//}
		}
	}
</script>


<div class="container-fluid">
  <div class="row"> <!-- BEGIN row div -->
  
 <!-- BEGIN Report setup -->
 <div id="reportSetup" class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                    <div class="panel panel-info">
                        <div class="panel-heading">
                            <div class="panel-title">Reportes</div>
                        </div>  
                        <div class="panel-body" >
                        
                            <form id="setupReportForm" name="setupReportForm" 
                                action="${requestContext.contextPath}/reports/runReport" 
                                method="post"
                                target="_blank"
                                class="form-horizontal" role="form"
                                >
                            

                                <div class="form-group">
                                    <label for="reportCode" class="col-md-3 control-label">Tipo Reporte</label>
                                    <div class="col-md-9">
                                        
                                        <select id="reportCode" name="reportCode" class="form-control" >
                                            <option value=""><@spring.message "msg.pleaseSelectReport"/></option>
                                            <#list availableReports?values?sort_by("reportTitle") as report>
                                                <#-- if canRunReport(currentAccountRoles report.reportCode ) -->
                                                    <#assign selected="">
                                                    <#if reportCode?exists && (reportCode == report.reportCode)>
                                                        <#assign selected="selected">
                                                    </#if>
                                                    <option value="${report.reportCode}" ${selected?default("")}><@spring.message ("msg."+report.reportCode) /></option>
                                                <#-- /#if -->
                                            </#list>
                                        </select>
                        
                        
                                    </div>
                                </div>
                        
                        
                               <#if showSinglePeriodSelector(reportCode)>
                                                       
                                    <div class="form-group">
                                        <label for="selectedPeriodName" class="col-md-3 control-label">Periodo</label>
                                        <div class="col-md-9">
                                            
                                                <select name="selectedPeriodName"  class="form-control">
                                                    <option value="2023" selected>2023</option>
                                                    <option value="2022">2022</option>
                                                    <option value="2021">2021</option>
                                                    <option value="2020">2020</option>
                                                    <option value="2019">2019</option>
                                                    <option value="2018">2018</option>
                                                    <option value="2017">2017</option>
                                                    <option value="2016">2016</option>
                                                    <option value="2015">2015</option>
                                                    <option value="2014">2014</option>
                                                </select>
                            
                            
                                        </div>
                                    </div>

                                </#if>


                               <#if showFormatSelector(reportCode)>

                                    <div class="form-group">
                                        <label for="selectedOutputFormat" class="col-md-3 control-label">Formato</label>
                                        <div class="col-md-9">
                                            
                                                <select name="selectedOutputFormat"  class="form-control">
                                                        <#if ('EmployeeAdditionsPromotionsReport' == reportCode?default("") )>
                                                                <option value="PDF" selected>pdf</option>
                                                         </#if>     
                                                         
                                                         <#if ('CreditsEntriesReport' == reportCode?default("")  )>
                                                                <option value="XLS" selected>xls</option>
                                                         </#if>  
                                                         
                                                         <#if ('ResumenDeSaldosDeCreditosDeReparticionesReport' == reportCode?default("")  )>
                                                                <option value="XLS" selected>xls</option>
                                                         </#if> 
                                                         
                                                         <#if ('SolicitudCreditosReparticionReport' == reportCode?default("") )>
                                                                <option value="XLS" selected>xls</option>
                                                         </#if>   
                                                         
                                                         
                                                </select>
                            
                            
                                        </div>
                                    </div>
                                    
                                </#if>


                                <div class="form-group">
                                    <!-- Button -->                                        
                                    <div class="col-md-offset-3 col-md-9">
                                        <button type="button" class="btn btn-primary" onclick="process('runReport');"><i class="icon-hand-right"></i> &nbsp; <@spring.message "msg.run"/></button>
                                    </div>
                                </div>
                                
                                
                                
                                
                            </form>
                            
                         </div> <!-- END  div class="panel-body"  -->
                    </div> <!-- div class="panel panel-info" -->

               
               
                
         </div>  <!-- div id="reportSetup" -->

  </div><!-- END Report setup -->
</div> <!-- END Row -->


           <#-- BEGIN row div >
          <div class="row"> 
          
               <div class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                            <div class="panel panel-info">
                                    <div class="panel-heading">
                                        <div class="panel-title">
                                            Resumen de Creditos
                                        </div>
                                    </div>  
                                    <div class="panel-body" >
            
                                    <div ng-controller="ChildCtrl as child">
                                    
                                            <form name="myForm"
                                                sf-schema="child.schema" 
                                                sf-form="child.form" 
                                                sf-model="child.selectedReportOptions"
                                                ng-submit="child.sendPost()" >
                                            </form>
                                        
                                    </div>
                              </div>
                 </div>
          </div> <!-- END Row -->      
            
        
         <#-- div class="row"> <!-- BEGIN row div >
          <div class="mainbox col-md-6 col-md-offset-3 col-sm-8 col-sm-offset-2">
                <div class="panel panel-info">
                        <div class="panel-heading">
                            <div class="panel-title">
                                Resumen de Creditos 2
                            </div>
                        </div>  
                        <div class="panel-body" >
                        
                        <div ng-controller="Child2Ctrl as child">
                        
                                <form name="myForm"
                                    sf-schema="child.schema" 
                                    sf-form="child.form" 
                                    sf-model="child.selectedReportOptions"
                                    ng-submit="child.sendPost()" >
                                </form>
                            
                        </div>
                  </div>
            </div>
    
    </div> <!-- END Row -->
    
    </div><!-- END  div class="container-fluid" -->

<script type="text/javascript">
        
        $(function() {
            
            
             $('#reportCode').change(function () {
                process('reportSetup');
              });
        });

    </script>
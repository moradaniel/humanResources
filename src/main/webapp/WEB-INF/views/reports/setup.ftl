<#import "/WEB-INF/views/spring.ftl" as spring />

<#import "/WEB-INF/views/reports/ReportMacros.ftl" as rm />


<script type="text/javascript">

	function process(action){
     	//account_hotels.onSubmit();

		//check if the user has selected a report
		var hasSelectedReport = ($('#reportCode').val().length) > 0;

		
		if(!hasSelectedReport){
			return false;
		}

		document.reportForm.action = action;
   		if (action == 'ReportSetup') {
     		document.reportForm.target="_self";
			document.reportForm.submit();
		}
		else {
			document.reportForm.target="_blank";
			if (validate(document.reportForm.startDate,document.reportForm.stopDate,document.reportForm.hotelCodes,document.reportForm.reportCode.options[document.reportForm.reportCode.options.selectedIndex]) == true) {
				if((document.reportForm.reportCode.value=='Employee_Additions_Promotions_Report') 
					&& document.reportForm.outputFormat.value=='XLS'){
					document.reportForm.action= 'ReportRunExcel';
				}
				document.reportForm.submit();
			}else {
				return false;
			}
		}
	}
</script>

<div id="reportSetup">


	
	<form id="setupReportForm" name="setupReportForm" 
			action="${requestContext.contextPath}/reports/runReport" 
			method="post"
			target="_blank"

			>
			
		

			<table id="report">
				<tr>
					<td class="label">
						<@spring.message "msg.reportName"/>:
					</td>
					<td class="right">
						<select id="reportCode" name="reportCode" <#-- onchange="return process('ReportSetup');"--> >
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
						<@rm.required />
						<#-- @crs_util.popHelp helpId="Reports_Management_Reports"/ -->
					</td>
				</tr>
			</table>
	
		<table border="0">
		
			<tr>
				<td colspan="3">
					<input class="button" type="submit" value="<@spring.message "msg.run"/>" onclick="return process('ReportRun');" />
				</td>
			</tr>
		</table>
	</form>
	
</div>	


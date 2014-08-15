package org.dpi.web.reporting;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.web.reporting.CanGenerateReportResult.ReasonCodes;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.dpi.web.reporting.parameters.EmployeeAdditionsPromotionsReportParameters;
import org.dpi.web.reporting.parameters.JasperReportDescriptor;
import org.dpi.web.reporting.parameters.ReportDescriptor;
import org.janux.bus.security.Account;
import org.springframework.stereotype.Service;

@Service("reportService")
public class ReportServiceImpl implements ReportService
{

	
	public enum ManagementReports{
		Employee_Additions_Promotions_Report,
		Credits_Entries_Report
	}
	
	private Map<String,ReportDescriptor> reportDescriptors;
	
	private Map<String,Class> reportParamClasses;
	
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;

	
	private String reservationViewUrl;
	
	//TODO move this to database
	// hardcoded list of available reports
	private static final JasperReportDescriptor descriptors[] =
	{
		new JasperReportDescriptor(ManagementReports.Employee_Additions_Promotions_Report.name(),"Employee Additions Promotions Report","Employee_Additions_Promotions_Report")
		//,new JasperReportDescriptor(ManagementReports.Credits_Entries_Report.name(),"Credits Entries Report","Credits_Entries_Report")

	};
	
    /** log variable */
    private static Log log = LogFactory.getLog(ReportServiceImpl.class);
    /** for database updates and retrievals */
   // private ReportServiceDao dao;
    
    public ReportServiceImpl()
    {
    	reportDescriptors = new HashMap<String,ReportDescriptor>();
    	reportParamClasses = new HashMap<String,Class>();
    	
    	for (int i = 0; i < descriptors.length; i++)
    	{
    		reportDescriptors.put(descriptors[i].getReportCode(), descriptors[i]);
    	}
    	
    	reportParamClasses.put(ManagementReports.Employee_Additions_Promotions_Report.name(), EmployeeAdditionsPromotionsReportParameters.class);
    	// reportParamClasses.put(ManagementReports.Credits_Entries_Report.name(), CreditsEntriesReportParameters.class);
    }
    
    public AbstractReportParameters getNewReportParameters(ReportDescriptor descriptor)
    {
    	Class paramClass = reportParamClasses.get(descriptor.getReportCode());
    	AbstractReportParameters params = null;
    	
    	try
    	{
			params = (AbstractReportParameters) paramClass.newInstance();
		} 
    	catch (InstantiationException e)
    	{
			log.error(e);
		} catch (IllegalAccessException e)
		{
			log.error(e);
		}
		
		return params;
    }
    
    /*public ReportServiceDao getReportServiceDao()
	{
		return dao;
	}
	public void setReportServiceDao(ReportServiceDao dao)
	{
		this.dao = dao;
	}*/
	
	
	public Map<String,ReportDescriptor> getAvailableReports()
	{
		return reportDescriptors;
	}

	
	public CanGenerateReportResult canGenerateReport(String reportCode, Account account, Long departmentId) {
	
		CanGenerateReportResult canGenerateReportResult = new CanGenerateReportResult();
		
		canGenerateReportResult.setHasPermissions( account.hasPermissions(reportCode, "READ") );
		
	
		if(reportCode.equals(ManagementReports.Employee_Additions_Promotions_Report.name())) {
			if(creditsPeriodService.getCurrentCreditsPeriod().getStatus()!=Status.Active){
				canGenerateReportResult.addReasonCode(ReasonCodes.closedCreditsPeriod.name());
			}else {
				Long creditosDisponiblesSegunSolicitadoPeriodoActual = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodService.getCurrentCreditsPeriod().getId(),departmentId);
				if(creditosDisponiblesSegunSolicitadoPeriodoActual < 0) {
					canGenerateReportResult.addReasonCode(ReasonCodes.negativeBalance.name());
				}
			}
		}
		
		return canGenerateReportResult;
	}
	

}


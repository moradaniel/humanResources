package org.dpi.web.reporting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentController;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.dpi.web.reporting.parameters.AbstractReportParameters.OutputFormat;
import org.dpi.web.reporting.parameters.CreditsEntriesReportParameters;
import org.dpi.web.reporting.parameters.EmployeeAdditionsPromotionsReportParameters;
import org.dpi.web.reporting.parameters.JasperReportDescriptor;
import org.dpi.web.reporting.parameters.ReportDescriptor;
import org.dpi.web.reporting.parameters.UserReportParameters;
import org.dpi.web.reporting2.ReportParameters;
import org.dpi.web.reporting2.ReportParametersFactory;
import org.dpi.web.reporting2.ReportService.Reports;
import org.dpi.web.reporting2.ReportUtils;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles and retrieves download request
 */
@Controller
@RequestMapping("/reports")
public class ReportController implements ApplicationContextAware{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	ApplicationContext applicationContext;

	@Resource(name="downloadService")
	private DownloadService downloadService;

	@Resource(name = "creditsEntryService")
	private CreditsEntryService creditsEntryService;

	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;

	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	@Resource(name = "reportService")
	private ReportService reportService;
	
	//@Autowired
	@Resource(name = "messageSource")
	private MessageSource messageSource;

	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
	//@RequestMapping(value = "/reportSetup", method = RequestMethod.GET)
	@RequestMapping(value = "/reportSetup")
	public ModelAndView getReportSetupPage(HttpServletRequest request ) {
		logger.debug("Received request to show Report Setup page");

		Map<String, Object> model = new HashMap<String, Object>();

		Map<String, ReportDescriptor> allAvailableReports =	ReportUtils.getAvailableReports();
		
		

		String selectedReportCode = request.getParameter(ReportConstants.KEY_REPORT_CODE);

		if (!StringUtils.isEmpty(selectedReportCode)) {
		    
			// TODO: customize depending on which report
/*			JasperReportDescriptor reportDescriptor =
					(JasperReportDescriptor) allAvailableReports.get(selectedReportCode);

			AbstractReportParameters reportParams =
					reportService.getNewReportParameters(reportDescriptor);
*/

		}
		
		

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currenUser = (Account)accountObj;
        final Department department = DepartmentController.getCurrentDepartment(request);
        
        if (department != null){
        
    		Map<String, ReportDescriptor> availableReportsForUser = new HashMap<>();
    		for (Map.Entry<String, ReportDescriptor> entry : allAvailableReports.entrySet()) {
    		    String reportCode = entry.getKey();
    		    //System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
    		    CanGenerateReportResult canGenerateReportResult = getReportService(Reports.valueOf(reportCode)).canGenerateReport(reportCode, currenUser, department.getId());
    		      if(canGenerateReportResult.canGenerateReport()==true) {
    		          availableReportsForUser.put(reportCode, entry.getValue());
    		      }
    		}
    		
    		model.put(ReportConstants.KEY_AVAILABLE_REPORTS, availableReportsForUser);
    		model.put(ReportConstants.KEY_REPORT_CODE, selectedReportCode);
    		
        }

		return new ModelAndView("reports/setup", model);
		//return "reports/setup";
	}

	/**
	 * Retrieves the download file
	 * 
	 * @return
	 */
	@RequestMapping(value = "/runReport", method = RequestMethod.POST)
	public void runReport(HttpServletRequest request 
			, HttpServletResponse response
			, UserReportParameters userParams
			) throws ServletException, IOException,
			ClassNotFoundException, SQLException {

		logger.debug("Received request to download a report");
		
		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		// get the current reparticion in the session
		final Department department = DepartmentController.getCurrentDepartment(request);
		CanGenerateReportResult canGenerateReportResult = reportService.canGenerateReport(userParams.getReportCode(), currenUser, department.getId());
		
		if(canGenerateReportResult.canGenerateReport()==false) {
			//TODO return the reason codes
			return;
		}

		//Map<String, Object> model = new HashMap<String, Object>();
		Map<String, ReportDescriptor> reports =
				reportService.getAvailableReports();

		JasperReportDescriptor reportDescriptor =
				(JasperReportDescriptor) reports.get(userParams.getReportCode());

		AbstractReportParameters reportParams =
				reportService.getNewReportParameters(reportDescriptor);

		this.massageParams(userParams, reportParams, request);



		//reportParamMap.put(ReportConstants.KEY_REPORT_PARAMS, reportParams);



		downloadService.download(reportParams, response);
	}

	private void massageParams(UserReportParameters userParams,
			AbstractReportParameters reportParams, HttpServletRequest request) {




		if (reportParams instanceof EmployeeAdditionsPromotionsReportParameters) {

			prepareEmployeeAdditionsPromotionsReportParameters(userParams, reportParams, request);
		}
		else 
		if(reportParams instanceof CreditsEntriesReportParameters) {
			prepareCreditsEntriesReportParameters(userParams, reportParams, request);
		}


	}


	private void prepareEmployeeAdditionsPromotionsReportParameters(
			UserReportParameters userParams, AbstractReportParameters reportParams,
			HttpServletRequest request) {

		EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters = (EmployeeAdditionsPromotionsReportParameters) reportParams;

		//employeeAdditionsPromotionsReportParameters.setOutputFormat(userParams.getOutputFormat());
		employeeAdditionsPromotionsReportParameters.setOutputFormat(OutputFormat.PDF);
		
	    Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    Account currenUser = (Account)accountObj;
	        
		employeeAdditionsPromotionsReportParameters.setGeneratedByUser(currenUser);

		// get the current reparticion in the session
		final Department department = DepartmentController.getCurrentDepartment(request);
		employeeAdditionsPromotionsReportParameters.addDepartmentIds(department.getId());

		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.setName(userParams.getSelectedPeriodName());
		employeeAdditionsPromotionsReportParameters.addCreditPeriodIds(creditsPeriodService.find(creditsPeriodQueryFilter).get(0).getId());


	}
	
	
	private void prepareCreditsEntriesReportParameters(
			UserReportParameters userParams, AbstractReportParameters reportParams,
			HttpServletRequest request) {

		CreditsEntriesReportParameters creditsEntriesReportParameters = (CreditsEntriesReportParameters) reportParams;

		//employeeAdditionsPromotionsReportParameters.setOutputFormat(userParams.getOutputFormat());
		creditsEntriesReportParameters.setOutputFormat(OutputFormat.XLS);

		// get the current reparticion in the session
		/*final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);
		creditsEntriesReportParameters.addReparticion(reparticion.getId());*/

		creditsEntriesReportParameters.addCreditPeriodIds(creditsPeriodService.getCurrentCreditsPeriod().getId());


	}
	/*
	@RequestMapping(value = "/downloadCSV", method = RequestMethod.GET, produces = "application/csv")
	public void demo(HttpServletResponse response) throws IOException {
	    List<String> names = new ArrayList<String>();
	    names.add("First Name");
	    names.add("Second Name");
	    names.add("Third Name");
	    names.add("Fourth Name");
	    BufferedWriter writer = new BufferedWriter(response.getWriter());
	    try {
	        response.setHeader("Content-Disposition", "attachment; filename=\"test.csv\"");
	        for (String name : names) {
	            writer.write(name);
	            writer.write(",");
	        }
	        writer.newLine();
	    } catch (IOException ex) {
	    } finally {
	        writer.flush();
	        writer.close();
	    }
	}*/
	
	/*@RequestMapping(value = "/events/excel", method = RequestMethod.GET)
    public void getEventsAsExcel(HttpServletResponse response) {*/
	    @RequestMapping(value = "/runReportAngular" , method = RequestMethod.POST)
	    public /*@ResponseBody Person */void save(@RequestBody ReportParameters reportParameters,HttpServletResponse response) {
/*
	       Person person=personService.savedata(jsonString);
	       return person;
	    */

        try {

            //response.setHeader("Content-disposition", "attachment; filename=test.xls");
            //response.getOutputStream().write(excelService.exportEventsToCSV());
           // response.getOutputStream().write(exportEventsToCSV());
            
            //build response header
            if(reportParameters.getSelectedOutputFormat()==org.dpi.web.reporting2.AbstractReportParameters.OutputFormat.XLS) {
                response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getReportCode()+".xls");    
            }else
              if(reportParameters.getSelectedOutputFormat()==org.dpi.web.reporting2.AbstractReportParameters.OutputFormat.PDF) {
                response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getReportCode()+".pdf");    
            }
            
            Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account currenUser = (Account)accountObj;
                
            reportParameters.setGeneratedByUser(currenUser);
            
            
            String serviceReportName = reportParameters.getReportCode()+"Service";
            
            org.dpi.web.reporting2.ReportService reportService = (org.dpi.web.reporting2.ReportService)applicationContext.getBean(serviceReportName);
            
            ByteArrayOutputStream generatedReportAsByteArrayOutputStream = reportService.generate(ReportParametersFactory.buildReportParameters(reportParameters)/*, response.getOutputStream()*/);
            /*
            ReportGenerator reportGenerator = ReportGeneratorFactory.getGenerator(reportParameters);
            
            //build response header
            if(reportParameters.getOutputFormat()==ReportParameters.OutputFormat.XLS) {
                response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getFileName()+".xls");    
            }
            
            
            reportGenerator.generate(reportParameters, response.getOutputStream());
            
            */
            
            /*
            Workbook wb = exportEventsToCSV();
            response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getFileName()+".xls");
            wb.write( response.getOutputStream() );*/

        } catch (Exception ex) {
            // TODO Auto-generated catch block
            throw new RuntimeException(ex);
        }
    }
	    
	    @RequestMapping(value = "/runReport2", method = RequestMethod.POST)
	    public void runReport2(HttpServletRequest request 
	            , HttpServletResponse response
	            , ReportParameters reportParameters
	            ) throws ServletException, IOException,
	    ClassNotFoundException, SQLException {

	        try {


	            logger.debug("Received request to generate a report");

	            Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	            Account currenUser = (Account)accountObj;
	            // get the current reparticion in the session
	            final Department department = DepartmentController.getCurrentDepartment(request);
	            CanGenerateReportResult canGenerateReportResult = reportService.canGenerateReport(reportParameters.getReportCode(), currenUser, department.getId());

	            if(canGenerateReportResult.canGenerateReport()==false) {
	                //TODO return the reason codes
	                return;
	            }

	            String reportFileName = messageSource.getMessage("msg."+reportParameters.getReportCode(),null, new Locale("es", "AR"));
	            //remove white spaces
	            reportFileName = reportFileName.replaceAll("\\s+","_");
	            
	            if(reportParameters.getSelectedOutputFormat()==org.dpi.web.reporting2.AbstractReportParameters.OutputFormat.XLS) {
	                response.setHeader("Content-disposition", "attachment; filename="+reportFileName+".xls");    
	            }else
	                if(reportParameters.getSelectedOutputFormat()==org.dpi.web.reporting2.AbstractReportParameters.OutputFormat.PDF) {
	                    response.setHeader("Content-disposition", "attachment; filename="+reportFileName+".pdf");    
	                }
	            
	            reportParameters.setGeneratedByUser(currenUser);
	            reportParameters.addDepartmentIds(department.getId());

	            ByteArrayOutputStream generatedReportAsByteArrayOutputStream = getReportService(Reports.valueOf(reportParameters.getReportCode())).generate(ReportParametersFactory.buildReportParameters(reportParameters)/*, response.getOutputStream()*/);
	            
	            response.setContentLength(generatedReportAsByteArrayOutputStream.size());
	            generatedReportAsByteArrayOutputStream.writeTo(response.getOutputStream());
	            
	            response.getOutputStream().flush();

	        } catch (Exception ex) {
	            throw new RuntimeException(ex);
	        }

	    }

	    /**
	     * 
	     * @param reportCode
	     * @return
	     */
	    org.dpi.web.reporting2.ReportService getReportService(Reports reportCode){
	        
	        String serviceReportName = reportCode.name()+"Service";

            org.dpi.web.reporting2.ReportService reportService = (org.dpi.web.reporting2.ReportService)applicationContext.getBean(serviceReportName);
            return reportService;
	    }
	    

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
	


}

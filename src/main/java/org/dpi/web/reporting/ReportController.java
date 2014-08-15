package org.dpi.web.reporting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsManagement.CreditsManagerService;
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
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles and retrieves download request
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

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

	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
	@RequestMapping(value = "/reportSetup", method = RequestMethod.GET)
	public ModelAndView getReportSetupPage(HttpServletRequest request ) {
		logger.debug("Received request to show Report Setup page");

		Map<String, Object> model = new HashMap<String, Object>();

		Map<String, ReportDescriptor> availableReports =
				reportService.getAvailableReports();

		String selectedReportCode = request.getParameter(ReportConstants.KEY_REPORT_CODE);

		if (selectedReportCode != null && selectedReportCode.length() > 0)
		{
			// TODO: customize depending on which report
			JasperReportDescriptor reportDescriptor =
					(JasperReportDescriptor) availableReports.get(selectedReportCode);

			AbstractReportParameters reportParams =
					reportService.getNewReportParameters(reportDescriptor);


		}

		model.put(ReportConstants.KEY_AVAILABLE_REPORTS, availableReports);
		model.put(ReportConstants.KEY_REPORT_CODE, selectedReportCode);

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

		// get the current reparticion in the session
		final Department department = DepartmentController.getCurrentDepartment(request);
		employeeAdditionsPromotionsReportParameters.addDepartment(department.getId());

		employeeAdditionsPromotionsReportParameters.addCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod().getId());


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

		creditsEntriesReportParameters.addCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod().getId());


	}


}

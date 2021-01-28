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
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentController;
import org.dpi.web.configuration.ApplicationConfigurationService;
import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles and retrieves report requests
 */
@Controller
@RequestMapping("/reports")
public class ReportController{

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @Resource(name = "creditsEntryService")
    private CreditsEntryService creditsEntryService;

    @Resource(name = "creditsManagerService")
    private CreditsManagerService creditsManagerService;

    @Resource(name = "creditsPeriodService")
    private CreditsPeriodService creditsPeriodService;


    @Resource(name = "messageSource")
    private MessageSource messageSource;

    @Resource(name = "applicationConfigurationService")
    private ApplicationConfigurationService applicationConfigurationService;



    @RequestMapping(value = "/reportSetup")
    public ModelAndView getReportSetupPage(HttpServletRequest request ) {
        logger.debug("Received request to show Report Setup page");

        Map<String, Object> model = new HashMap<String, Object>();

        Map<String, ReportDescriptor> allAvailableReports =	ReportUtils.getAvailableReports();



        String selectedReportCode = request.getParameter(ReportConstants.KEY_REPORT_CODE);


        Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currenUser = (Account)accountObj;
        final Department department = DepartmentController.getCurrentDepartment(request);

        if (department != null){

            Map<String, ReportDescriptor> availableReportsForUser = new HashMap<>();
            for (Map.Entry<String, ReportDescriptor> entry : allAvailableReports.entrySet()) {
                String reportCode = entry.getKey();
                ReportService reportService = applicationConfigurationService.getReportService(ReportService.Reports.valueOf(reportCode));
                CanGenerateReportResult canGenerateReportResult = reportService.canGenerateReport(currenUser, department.getId());
                if(canGenerateReportResult.canGenerateReport()==true) {
                    availableReportsForUser.put(reportCode, entry.getValue());
                }
            }

            model.put(ReportConstants.KEY_AVAILABLE_REPORTS, availableReportsForUser);
            model.put(ReportConstants.KEY_REPORT_CODE, selectedReportCode);

        }

        return new ModelAndView("reports/setup", model);

    }



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
            if(reportParameters.getSelectedOutputFormat()==OutputFormat.XLS) {
                response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getReportCode()+".xls");    
            }else
                if(reportParameters.getSelectedOutputFormat()==OutputFormat.PDF) {
                    response.setHeader("Content-disposition", "attachment; filename="+reportParameters.getReportCode()+".pdf");    
                }

            Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account currenUser = (Account)accountObj;

            reportParameters.setGeneratedByUser(currenUser);



            ReportService reportService = applicationConfigurationService.getReportService(ReportService.Reports.valueOf(reportParameters.getReportCode()));


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

    @RequestMapping(value = "/runReport", method = RequestMethod.POST)
    public void runReport(HttpServletRequest request 
            , HttpServletResponse response
            , ReportParameters userReportParameters
            ) throws ServletException, IOException,
    ClassNotFoundException, SQLException {

        try {


            logger.debug("Received request to generate a report");

            Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            Account currenUser = (Account)accountObj;
            // get the current reparticion in the session
            final Department department = DepartmentController.getCurrentDepartment(request);

            ReportService reportService = applicationConfigurationService.getReportService(ReportService.Reports.valueOf(userReportParameters.getReportCode()));
            CanGenerateReportResult canGenerateReportResult = reportService.canGenerateReport(currenUser, department.getId());

            if(canGenerateReportResult.canGenerateReport()==false) {
                //TODO return the reason codes
                return;
            }

            String reportFileName = messageSource.getMessage("msg."+userReportParameters.getReportCode(),null, new Locale("es", "AR"));
            //remove white spaces
            reportFileName = reportFileName.replaceAll("\\s+","_");

            if(userReportParameters.getSelectedOutputFormat()==OutputFormat.XLS) {
                response.setHeader("Content-disposition", "attachment; filename="+reportFileName+".xls");    
            }else
                if(userReportParameters.getSelectedOutputFormat()==OutputFormat.PDF) {
                    response.setHeader("Content-disposition", "attachment; filename="+reportFileName+".pdf");    
                }

            userReportParameters.setGeneratedByUser(currenUser);
            userReportParameters.addDepartmentIds(department.getId());


            ByteArrayOutputStream generatedReportAsByteArrayOutputStream = reportService.generate(ReportParametersFactory.buildReportParameters(userReportParameters)/*, response.getOutputStream()*/);

            response.setContentLength(generatedReportAsByteArrayOutputStream.size());
            generatedReportAsByteArrayOutputStream.writeTo(response.getOutputStream());

            response.getOutputStream().flush();

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }

    }


}

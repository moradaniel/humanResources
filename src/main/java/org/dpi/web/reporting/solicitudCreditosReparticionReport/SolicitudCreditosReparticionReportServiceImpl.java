package org.dpi.web.reporting.solicitudCreditosReparticionReport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.dpi.department.Department;
import org.dpi.department.DepartmentReportService;
import org.dpi.department.DepartmentService;
import org.dpi.stats.PeriodSummaryData;
import org.dpi.web.reporting.BaseReportService;
import org.dpi.web.reporting.CanGenerateReportResult;
import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.dpi.web.reporting.ReportService;
import org.janux.bus.security.Account;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;



public class SolicitudCreditosReparticionReportServiceImpl extends BaseReportService<SolicitudCreditosReparticionReportParameters> implements ReportService<SolicitudCreditosReparticionReportParameters>{

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Resource(name = "departmentReportService")
    private DepartmentReportService departmentReportService;
    
    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    @Autowired
    ServletContext servletContext;

    public SolicitudCreditosReparticionReportServiceImpl() {

    }

    @Override
    public Reports getReportCode() {
        return ReportService.Reports.SolicitudCreditosReparticionReport;
    }

    @Override
    public CanGenerateReportResult canGenerateReport(Account account, Long departmentId) {


        CanGenerateReportResult canGenerateReportResult = super.canGenerateReport(account, departmentId);

        //Custom permission here

        return canGenerateReportResult;
    }



    @Override
    public ByteArrayOutputStream generate(SolicitudCreditosReparticionReportParameters parameters)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = null;
/*
        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.addCreditsPeriodNames((String[]) parameters.getCreditPeriodNames().toArray(new String[0]));



        List<CreditsEntry> creditsEntries = creditsEntryService.find(creditsEntryQueryFilter);
        
        List<ResumenDeSaldosDeCreditosDeReparticionesReportRecord> creditsEntriesReportRecords = new ArrayList<>();
        for(CreditsEntry creditsEntry : creditsEntries) {
            Department ministerioDeReparticion = departmentService.getMinisterioOSecretariaDeEstado(creditsEntry.getEmployment().getSubDepartment().getDepartment());
            ResumenDeSaldosDeCreditosDeReparticionesReportRecord creditsEntriesReportRecord = new ResumenDeSaldosDeCreditosDeReparticionesReportRecord(creditsEntry, ministerioDeReparticion);
            
            creditsEntriesReportRecords.add(creditsEntriesReportRecord);
        }*/
        
        //List<PeriodSummaryData> resumenDeSaldosReparticionesRecords = departmentReportService.getCurrentPeriodDepartmentsSummaryData();
        
        Department currentDepartment = departmentService.findById(parameters.getDepartmentId());
        
        PeriodSummaryData solicitudCreditosReparticionRecords = departmentReportService.buildCurrentPeriodSummaryData(currentDepartment);

        /*if (OutputFormat.PDF.equals(parameters.getOutputFormat())) {
            getPdfDocument(parameters, null);
        } else*/
            if (OutputFormat.XLS.equals(parameters.getOutputFormat())) {
                byteArrayOutputStream = getExcelDocument(parameters,solicitudCreditosReparticionRecords/*, outputStream*/);
            }

        return byteArrayOutputStream;
    }

    private ByteArrayOutputStream getExcelDocument(SolicitudCreditosReparticionReportParameters parameters,
            PeriodSummaryData solicitudCreditosReparticionRecords) throws Exception{
        log.info("Running SolicitudCreditosReparticionReport");

        String TEMPLATE_FOLDER = "/WEB-INF/reports/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(InputStream templateReportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+parameters.getTemplateFileName())) {
            Context context = new Context();
            context.putVar("solicitudCreditosReparticionRecords", solicitudCreditosReparticionRecords);
            JxlsHelper.getInstance().processTemplate(templateReportStream, baos, context);
        }

        return baos;

    }



}

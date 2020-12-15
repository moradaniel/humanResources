package org.dpi.web.reporting.creditsEntriesReport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentService;
import org.dpi.employment.EmploymentService;
import org.dpi.person.PersonService;
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

import fr.opensagres.xdocreport.converter.ConverterTypeTo;
import fr.opensagres.xdocreport.converter.ConverterTypeVia;
import fr.opensagres.xdocreport.converter.Options;
import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;



public class CreditsEntriesReportServiceImpl extends BaseReportService<CreditsEntriesReportParameters> implements ReportService<CreditsEntriesReportParameters>{

    Logger log = LoggerFactory.getLogger(this.getClass());


    @Resource(name = "creditsEntryService")
    private CreditsEntryService creditsEntryService;

    @Resource(name = "employmentService")
    private EmploymentService employmentService;

    @Resource(name = "personService")
    private PersonService personService;

    @Resource(name = "creditsManagerService")
    private CreditsManagerService creditsManagerService;

    @Resource(name = "creditsPeriodService")
    private CreditsPeriodService creditsPeriodService;

    @Resource(name = "departmentService")
    private DepartmentService departmentService;

    @Autowired
    ServletContext servletContext;

    public CreditsEntriesReportServiceImpl() {

    }

    public Reports getReportCode() {
        return ReportService.Reports.CreditsEntriesReport;
    }

    @Override
    public CanGenerateReportResult canGenerateReport(Account account, Long departmentId) {


        CanGenerateReportResult canGenerateReportResult = super.canGenerateReport(account, departmentId);

        //Custom permission here

        return canGenerateReportResult;
    }


    @Override
    public ByteArrayOutputStream generate(CreditsEntriesReportParameters parameters)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = null;

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.addCreditsPeriodNames((String[]) parameters.getCreditPeriodNames().toArray(new String[0]));

        //dont report test department
        creditsEntryQueryFilter.addNotInDepartmentCode("9999999999");


        List<CreditsEntry> creditsEntries = creditsEntryService.find(creditsEntryQueryFilter);
        
        List<CreditsEntriesReportRecord> creditsEntriesReportRecords = new ArrayList<>();
        for(CreditsEntry creditsEntry : creditsEntries) {
            Department ministerioDeReparticion = departmentService.getMinisterioOSecretariaDeEstado(creditsEntry.getEmployment().getSubDepartment().getDepartment());
            CreditsEntriesReportRecord creditsEntriesReportRecord = new CreditsEntriesReportRecord(creditsEntry, ministerioDeReparticion);
            
            creditsEntriesReportRecords.add(creditsEntriesReportRecord);
        }

        if (OutputFormat.PDF.equals(parameters.getOutputFormat())) {
            getPdfDocument(parameters, null);
        } else
            if (OutputFormat.XLS.equals(parameters.getOutputFormat())) {
                byteArrayOutputStream = getExcelDocument(parameters,creditsEntriesReportRecords/*, outputStream*/);
            }

        return byteArrayOutputStream;
    }

    private ByteArrayOutputStream getExcelDocument(CreditsEntriesReportParameters parameters,
            List<CreditsEntriesReportRecord> creditsEntriesReportRecords) throws Exception{
        log.info("Running CreditsSummaryReportService");

        String TEMPLATE_FOLDER = "/WEB-INF/reports/";

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(InputStream templateReportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+parameters.getTemplateFileName())) {
            Context context = new Context();
            context.putVar("creditsEntriesReportRecords", creditsEntriesReportRecords);
            JxlsHelper.getInstance().processTemplate(templateReportStream, baos, context);
        }

        return baos;

    }

    private void getPdfDocument(CreditsEntriesReportParameters parameters, OutputStream outputStream) {

        String TEMPLATE_FOLDER = "/WEB-INF/reports/";
        String templateFileName = "ODTProjectWithVelocity.odt";

        try {
            // 1) Load ODT file by filling Velocity template engine and cache it to the registry
            //InputStream in = ODTProjectWithVelocity.class.getResourceAsStream("ODTProjectWithVelocity.odt");
            InputStream templateReportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+templateFileName);
            IXDocReport report = XDocReportRegistry.getRegistry().loadReport(templateReportStream,TemplateEngineKind.Velocity);

            // 2) Create fields metadata to manage lazy loop (#forech velocity)
            // for table row.
            FieldsMetadata metadata = new FieldsMetadata();
            metadata.addFieldAsList("developers.Name");
            metadata.addFieldAsList("developers.Profesion");
            metadata.addFieldAsList("developers.Payment");
            metadata.addFieldAsList("developers.Bonus");
            report.setFieldsMetadata(metadata);

            // 2) Create context Java model
            IContext context = report.createContext();
            CreditsSummaryData project = new CreditsSummaryData(1l,"Resumen de Creditos", "Tecnico",new java.util.Date(), 200, 20);
            context.put("project", project);
            List<CreditsSummaryData> employees = generateSampleEmployeeData();
            context.put("developers", employees);

            // 3) Generate report by merging Java model with the ODT
            // OutputStream out = new FileOutputStream(new File("ODTProjectWithVelocity_Out.odt"));
            //report.process(context, outputStream);

            // 4) Generate report by merging Java model with the ODT
            /* OutputStream out = new FileOutputStream(new File(
                    "ODTProjectWithVelocityList_Out.pdf"));*/
            // report.process(context, out);
            Options options = Options.getTo(ConverterTypeTo.PDF).via(
                    ConverterTypeVia.ODFDOM);
            report.convert(context, options, outputStream);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (XDocReportException e) {
            e.printStackTrace();
        }
    }


    private List<CreditsSummaryData> generateSampleEmployeeData() {
        List<CreditsSummaryData> employees = new ArrayList<>();
        employees.add(new CreditsSummaryData(1l, "pepe", "Tecnico",new java.util.Date(), 200, 20));
        employees.add(new CreditsSummaryData(2l, "Natalia", "Lic en Informatica",new java.util.Date(), 500, 50));
        return employees;

    }


    public static final class CreditsSummaryData implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Long id;
        private final String name;
        private Date birthDate;
        private String profesion;
        private Integer payment;
        private Integer bonus;
        //private List<Response> responses;

        public CreditsSummaryData(Long id, String name, String profesion, Date birthDate,
                Integer payment, Integer bonus) {
            super();
            this.id = id;
            this.name = name;
            this.profesion = profesion;
            this.birthDate = birthDate;
            this.profesion = profesion;
            this.payment = payment;
            this.bonus = bonus;
        }

        public Date getBirthDate() {
            return birthDate;
        }
        public void setBirthDate(Date birthDate) {
            this.birthDate = birthDate;
        }
        public String getProfesion() {
            return profesion;
        }

        public void setProfesion(String profesion) {
            this.profesion = profesion;
        }
        public Integer getPayment() {
            return payment;
        }
        public void setPayment(Integer payment) {
            this.payment = payment;
        }
        public Integer getBonus() {
            return bonus;
        }
        public void setBonus(Integer bonus) {
            this.bonus = bonus;
        }
        public Long getId() {
            return id;
        }
        public String getName() {
            return name;
        }



    }


}

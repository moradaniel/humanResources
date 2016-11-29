package org.dpi.web.reporting2;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsEntry.CreditsEntryVO;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.DepartmentService;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.person.PersonService;
import org.dpi.web.reporting.AdditionsCreditEntriesReportDataSource;
import org.dpi.web.reporting.CanGenerateReportResult;
import org.dpi.web.reporting.CanGenerateReportResult.ReasonCodes;
import org.dpi.web.reporting.PromotionCreditEntriesReportDataSource;
import org.dpi.web.reporting.dto.GenericReportRecord;
import org.dpi.web.reporting.jasper.ExporterService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;



public class EmployeeAdditionsPromotionsReportServiceImpl extends BaseReportService<org.dpi.web.reporting2.EmployeeAdditionsPromotionsReportParameters> implements ReportService<org.dpi.web.reporting2.EmployeeAdditionsPromotionsReportParameters>
{
    Logger log = LoggerFactory.getLogger(this.getClass());

    /*@Resource(name = "creditsEntryDao")
	private CreditsEntryDao creditsEntryDao;*/

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

    


    private ApplicationContext applicationContext;

    @Autowired
    ServletContext servletContext;

    @Resource(name = "exporterService")
    private ExporterService exporter;

    public EmployeeAdditionsPromotionsReportServiceImpl() {

    }
    
    
    public CanGenerateReportResult canGenerateReport(String reportCode, Account account, Long departmentId) {
        
        
        CanGenerateReportResult canGenerateReportResult = super.canGenerateReport(reportCode, account, departmentId);
        
    
        /*if(reportCode.equals(org.dpi.web.reporting2.ReportService.Reports.EmployeeAdditionsPromotionsReport.name())) {*/
            if(creditsPeriodService.getCurrentCreditsPeriod().getStatus()!=Status.Active){
                canGenerateReportResult.addReasonCode(ReasonCodes.closedCreditsPeriod.name());
            }else {
                Long creditosDisponiblesSegunSolicitadoPeriodoActual = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodService.getCurrentCreditsPeriod().getId(),departmentId);
                if(creditosDisponiblesSegunSolicitadoPeriodoActual < 0) {
                    canGenerateReportResult.addReasonCode(ReasonCodes.negativeBalance.name());
                }
            }
        /*}*/
        
        return canGenerateReportResult;
    }
    

    @Override
    public ByteArrayOutputStream generate(org.dpi.web.reporting2.EmployeeAdditionsPromotionsReportParameters parameters/*, OutputStream outputStream*/)
            throws Exception {

        ByteArrayOutputStream byteArrayOutputStream = null;
        
        //CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        //creditsEntryQueryFilter.addCreditsPeriodNames((String[]) parameters.getCreditPeriodNames().toArray(new String[0]));


       // List<CreditsEntry> creditsEntries = creditsEntryService.find(creditsEntryQueryFilter);

        if (org.dpi.web.reporting2.AbstractReportParameters.OutputFormat.PDF.equals(parameters.getOutputFormat())) {
            byteArrayOutputStream = getPdfDocument(parameters/*, outputStream*/);
        } /*else
        if (OutputFormat.xls.equals(parameters.getOutputFormat())) {
            getExcelDocument(parameters,creditsEntries, outputStream);
        }*/

        return byteArrayOutputStream;
    }



    private ByteArrayOutputStream getPdfDocument(org.dpi.web.reporting2.EmployeeAdditionsPromotionsReportParameters reportParams/*,
            OutputStream outputStream*/) {
        
        String TEMPLATE_FOLDER = "/WEB-INF/reports/";
        //String templateFileName = "nota_creditos_conFechaImpresion.jrxml";

        try {


            Map<String, Object> reportParamMap = new HashMap<String, Object>();


            // 1. Add report parameters

            reportParamMap.put("Title", "Report");

            // 2.  Retrieve template

            InputStream reportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+reportParams.getTemplateFileName()) ;

            //InputStream reportStream = this.getClass().getClassLoader().getResourceAsStream(TEMPLATE_FOLDER+reportParams.getTemplateFileName());


            Map<String, Object> recordsMap = null;

            //if (reportParams instanceof EmployeeAdditionsPromotionsReportParameters)
            //{
                recordsMap =    getReportData(reportParams);

            //}

            for (Map.Entry<String, Object> entry : recordsMap.entrySet()) {
                reportParamMap.put(entry.getKey(), entry.getValue());
            }

            // 3. Convert template to JasperDesign
            JasperDesign jd = JRXmlLoader.load(reportStream);

            // 4. Compile design to JasperReport
            JasperReport jr = JasperCompileManager.compileReport(jd);

            Locale locale = new Locale("es", "AR");
            reportParamMap.put(JRParameter.REPORT_LOCALE, locale);

            // 5. Create the JasperPrint object
            // Make sure to pass the JasperReport, report parameters, and data source
            JasperPrint jp = JasperFillManager.fillReport(jr, reportParamMap, new JREmptyDataSource());

            // 6. Create an output byte stream where data will be written
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // 7. Export report
            //exporter.export(reportParams.getOutputFormat(), jp, response, baos);

            exportPdf(jp, baos);


            return baos;
            // 8. Write to reponse stream
            //write(outputStream, baos);

        } catch (JRException jre) {
            log.error("Unable to process download");
            throw new RuntimeException(jre);
        }
    }

    /**
     * Writes the report to the output stream
     */
    private void write( OutputStream outputStream,
            ByteArrayOutputStream baos) {

        try {
            log.debug(String.valueOf(baos.size()));

            // Retrieve output stream
            //  ServletOutputStream outputStream = response.getOutputStream();
            // Write to output stream
            baos.writeTo(outputStream);
            // Flush the stream
            outputStream.flush();

            // Remove download token
            //tokenService.remove(token);

        } catch (Exception e) {
            log.error("Unable to write report to the output stream");
            throw new RuntimeException(e);
        }
    }




    public Map<String, Object> getReportData(final org.dpi.web.reporting2.EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters) {

        CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
        creditsPeriodQueryFilter.setName(employeeAdditionsPromotionsReportParameters.getCreditPeriodNames().iterator().next());
        
        
        long creditsPeriodId = creditsPeriodService.find(creditsPeriodQueryFilter).get(0).getId();


        long departmentId = employeeAdditionsPromotionsReportParameters.getDepartmentId();

        // Call DownloadService to do the actual report processing
        //downloadService.downloadPdf(response);
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("DEPARTMENT_NAME",  departmentService.findById(departmentId).getName());

        //get movimientos de ascenso
        EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
        empleoQueryFilter.setDepartmentId(departmentId);
        //todos los status
        //empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
        creditsEntryQueryFilter.addCreditsPeriodIds(creditsPeriodId);

        List<CreditsEntry> creditsEntryDepartment = creditsEntryService.find(creditsEntryQueryFilter);

        Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currenUser = (Account)accountObj;
        List<CreditsEntryVO> creditsEntriesAscensosDepartmentVO = creditsEntryService.buildCreditsEntryVO(creditsEntryDepartment,currenUser);



        params.put("MOVIMIENTOS_ASCENSO",  new PromotionCreditEntriesReportDataSource(getReportDataMovimientosAscenso(creditsEntriesAscensosDepartmentVO)));

        //get movimientos de Ingreso
        empleoQueryFilter = new EmploymentQueryFilter();
        empleoQueryFilter.setDepartmentId(departmentId);
        //todos los estados
        //empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
        creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
        creditsEntryQueryFilter.addCreditsPeriodIds(creditsPeriodId);
        creditsEntryQueryFilter.setHasCredits(true);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);

        List<CreditsEntry> creditsEntryIngresosDepartment = creditsEntryService.find(creditsEntryQueryFilter);

        int cantidadMovimientosAscenso = 0;
        if(!CollectionUtils.isEmpty(creditsEntriesAscensosDepartmentVO)){
            cantidadMovimientosAscenso=creditsEntriesAscensosDepartmentVO.size();
        }

        int cantidadMovimientosIngreso = 0;
        if(!CollectionUtils.isEmpty(creditsEntryIngresosDepartment)){
            cantidadMovimientosIngreso=creditsEntryIngresosDepartment.size();
        }

        params.put("CANTIDAD_MOVIMIENTOS_ASCENSO",  cantidadMovimientosAscenso);
        params.put("CANTIDAD_MOVIMIENTOS_INGRESO",  cantidadMovimientosIngreso);

        params.put("MOVIMIENTOS_INGRESO",  new AdditionsCreditEntriesReportDataSource(getReportDataMovimientosIngreso(creditsEntryIngresosDepartment)));


        Long creditosReubicacionDeReparticion =this.creditsManagerService.getCreditosReparticion_ReubicacionDeReparticion_Periodo(creditsPeriodId,departmentId);
                
        Long creditosDisponiblesAlInicioDelPeriodo =this.creditsManagerService.getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,departmentId);

        Long creditosAcreditadosPorBajaDurantePeriodoActual = this.creditsManagerService.getCreditosPorBajasDeReparticion(creditsPeriodId,departmentId);

        Long creditosRetenidosPeriodoActual = this.creditsManagerService.getRetainedCreditsByDepartment(creditsPeriodId,departmentId);

        Long creditosReasignadosPeriodoActual = this.creditsManagerService.getCreditosReparticion_ReasignadosDeRetencion_Periodo(creditsPeriodId,departmentId);

        Long creditosPorIngresosOAscensosSolicitados = this.creditsManagerService.getCreditosPorIngresosOAscensosSolicitados(creditsPeriodId, departmentId);

        Long creditosDisponibles = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodId,departmentId);


        params.put("CANTIDAD_CREDITOS_TRANSFERIDOS_POR_REUBICACION",creditosReubicacionDeReparticion);
        params.put("CANTIDAD_CREDITOS_DISPONIBLES_INICIO_PROCESO",creditosDisponiblesAlInicioDelPeriodo);
        params.put("CANTIDAD_CREDITOS_POR_BAJAS",creditosAcreditadosPorBajaDurantePeriodoActual);
        params.put("CANTIDAD_CREDITOS_RETENIDOS",creditosRetenidosPeriodoActual);
        params.put("CANTIDAD_CREDITOS_REASIGNADOS",creditosReasignadosPeriodoActual);

        params.put("CANTIDAD_CREDITOS_UTILIZADOS",creditosPorIngresosOAscensosSolicitados);
        params.put("CANTIDAD_CREDITOS_DISPONIBLES_AL_FINAL_DEL_PERIODO",creditosDisponibles);

        params.put("CURRENT_USER_NAME",employeeAdditionsPromotionsReportParameters.getGeneratedByUser().getName());


        return params;


    }



    private List<GenericReportRecord> getReportDataMovimientosAscenso(List<CreditsEntryVO> creditsEntriesAscensosDepartmentVO) {

        final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
        for(CreditsEntryVO creditsEntryAscensosDepartmentVO:creditsEntriesAscensosDepartmentVO){
            GenericReportRecord record = new GenericReportRecord();
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.PERSON_APELLIDO_NOMBRE.name(),creditsEntryAscensosDepartmentVO.getCreditsEntry().getEmployment().getPerson().getApellidoNombre());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.PERSON_CUIL.name(),creditsEntryAscensosDepartmentVO.getCreditsEntry().getEmployment().getPerson().getCuil());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.EMPLOYMENT_CURRENT_CATEGORY.name(),creditsEntryAscensosDepartmentVO.getCurrentCategory());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.EMPLOYMENT_PROPOSED_CATEGORY.name(),creditsEntryAscensosDepartmentVO.getProposedCategory());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.EMPLOYMENT_OCCUPATIONAL_GROUP.name(),creditsEntryAscensosDepartmentVO.getOccupationalGroup());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.EMPLOYMENT_PARENT_OCCUPATIONAL_GROUP.name(),creditsEntryAscensosDepartmentVO.getParentOccupationalGroup());
            record.setValue(PromotionCreditEntriesReportDataSource.ReportFieldID.NUMBER_OF_CREDITS.name(),creditsEntryAscensosDepartmentVO.getCreditsEntry().getNumberOfCredits());
            records.add(record);
        }

        return records;
    }


    private List<GenericReportRecord> getReportDataMovimientosIngreso(List<CreditsEntry> creditsEntryIngresosDepartment) {

        final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
        for(CreditsEntry creditsEntryIngreso:creditsEntryIngresosDepartment){
            GenericReportRecord record = new GenericReportRecord();
            record.setValue(AdditionsCreditEntriesReportDataSource.ReportFieldID.PERSON_NUEVO_PERFIL.name(),"");
            record.setValue(AdditionsCreditEntriesReportDataSource.ReportFieldID.NEW_EMPLOYMENT_PARENT_OCCUPATIONAL_GROUP.name(),"");
            record.setValue(AdditionsCreditEntriesReportDataSource.ReportFieldID.PERSON_NUEVO_CATEGORIA_PROPUESTA.name(),creditsEntryIngreso.getEmployment().getCategory().getCode());
            record.setValue(AdditionsCreditEntriesReportDataSource.ReportFieldID.NUMBER_OF_CREDITS.name(),creditsEntryIngreso.getNumberOfCredits());

            records.add(record);
        }

        return records;
    }
    
    
    public void exportPdf(JasperPrint jasperPrint, ByteArrayOutputStream baos) {
        // Create a JRPdfExporter instance
        JRPdfExporter exporter = new JRPdfExporter();

        // Here we assign the parameters jp and baos to the exporter
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);

        try {
            exporter.exportReport();

        } catch (JRException e) {
            throw new RuntimeException(e);
        }
        
    }

}

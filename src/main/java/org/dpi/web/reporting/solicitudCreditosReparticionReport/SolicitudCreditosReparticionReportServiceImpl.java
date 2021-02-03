package org.dpi.web.reporting.solicitudCreditosReparticionReport;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.ServletContext;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentReportService;
import org.dpi.department.DepartmentService;
import org.dpi.employment.EmploymentQueryFilter;
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
    
    @Resource(name = "creditsEntryService")
    private CreditsEntryService creditsEntryService;
    
    @Resource(name = "creditsPeriodService")
    private CreditsPeriodService creditsPeriodService;

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

        Department currentDepartment = departmentService.findById(parameters.getDepartmentId());
        Department ministerioOSecretariaDeEstadoDepartment = departmentService.getMinisterioOSecretariaDeEstado(currentDepartment);
        
        List<IngresoRecord> movimientosIngresoRecords = getMovimientosIngreso(parameters);
        List<AscensoRecord> movimientosAscensoRecords = getMovimientosAscenso(parameters);
        
        int movimientosIngresoRecordsSize = movimientosIngresoRecords.size();
        int movimientosAscensoRecordsSize = movimientosAscensoRecords.size();
        
        if(movimientosIngresoRecordsSize<movimientosAscensoRecordsSize) {
            //rellenar con movimientos vacios para que tenga la misma cantidad de filas q los ascensos
            for(int i=0;i<movimientosAscensoRecordsSize-movimientosIngresoRecordsSize;i++){
                movimientosIngresoRecords.add(new IngresoRecord("",0,0,0));
            }
        }else if(movimientosIngresoRecordsSize>movimientosAscensoRecordsSize){
            //rellenar con movimientos vacios para que tenga la misma cantidad de filas q los ingresos
            for(int i=0;i<movimientosAscensoRecordsSize-movimientosIngresoRecordsSize;i++){
                movimientosAscensoRecords.add(new AscensoRecord("","",0,0,0));
            }
                
        }
        
        PeriodSummaryData periodSummaryData = departmentReportService.buildCurrentPeriodSummaryData(currentDepartment);
        
        
        Map<String, Object> reportData= new HashMap<String, Object>();
        reportData.put("ministerioOSecretariaDeEstadoDepartment", ministerioOSecretariaDeEstadoDepartment);
        reportData.put("department", currentDepartment);
        reportData.put("movimientosIngresoRecords", movimientosIngresoRecords);
        reportData.put("movimientosAscensoRecords", movimientosAscensoRecords);
        reportData.put("periodSummaryData", periodSummaryData);
        
        

        /*if (OutputFormat.PDF.equals(parameters.getOutputFormat())) {
            getPdfDocument(parameters, null);
        } else*/
            if (OutputFormat.XLS.equals(parameters.getOutputFormat())) {
                byteArrayOutputStream = getExcelDocument(parameters,reportData/*, outputStream*/);
            }

        return byteArrayOutputStream;
    }

    private ByteArrayOutputStream getExcelDocument(SolicitudCreditosReparticionReportParameters parameters,
            Map<String, Object> reportData) throws Exception{
        log.info("Running SolicitudCreditosReparticionReport");

        String TEMPLATE_FOLDER = "/WEB-INF/reports/";

        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(InputStream templateReportStream = servletContext.getResourceAsStream(TEMPLATE_FOLDER+parameters.getTemplateFileName())) {
            Context context = new Context();
            context.putVar("data", reportData);
            JxlsHelper.getInstance().processTemplate(templateReportStream, baos, context);
        }

        return baos;

    }
    
    
    private List<IngresoRecord> getMovimientosIngreso(SolicitudCreditosReparticionReportParameters parameters) {
        
        //get movimientos de Ingreso
        CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
        creditsPeriodQueryFilter.setName(parameters.getCreditPeriodNames().iterator().next());
        
        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();


        long creditsPeriodId = creditsPeriodService.find(creditsPeriodQueryFilter).get(0).getId();

        
        EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
        empleoQueryFilter.setDepartmentId(parameters.getDepartmentId());
        //todos los estados
        //empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
        creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
        creditsEntryQueryFilter.addCreditsPeriodIds(creditsPeriodId);
        creditsEntryQueryFilter.setHasCredits(true);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);

        List<CreditsEntry> creditsEntryIngresosDepartment = creditsEntryService.find(creditsEntryQueryFilter);
        
        
        Map<String, IngresoRecord> ingresoRecordMap = new HashMap<>();
        for(CreditsEntry entry : creditsEntryIngresosDepartment){
            String category = entry.getEmployment().getCategory().getCode();
            IngresoRecord record = ingresoRecordMap.get(category);
            if(record ==null) {
                record = new IngresoRecord(category, 0, entry.getNumberOfCredits(),0);
                ingresoRecordMap.put(category, record);
            }
            record.setCantidad(record.getCantidad()+1);
            record.setTotalCreditosIngresosEnCategoria(record.getTotalCreditosIngresosEnCategoria()+entry.getNumberOfCredits());
        }
        
        List<IngresoRecord> ingresosSortedDescendingByCategory = ingresoRecordMap.values().stream()
                .sorted(Comparator.comparing(IngresoRecord::getCategoria).reversed())
                .collect(Collectors.toList());
        
        
        return ingresosSortedDescendingByCategory;
    }


    private List<AscensoRecord> getMovimientosAscenso(SolicitudCreditosReparticionReportParameters parameters) {
        CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
        creditsPeriodQueryFilter.setName(parameters.getCreditPeriodNames().iterator().next());

        long creditsPeriodId = creditsPeriodService.find(creditsPeriodQueryFilter).get(0).getId();


        long departmentId = parameters.getDepartmentId();
        //get movimientos de ascenso
        EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
        empleoQueryFilter.setDepartmentId(parameters.getDepartmentId());

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
        creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
        creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
        creditsEntryQueryFilter.addCreditsPeriodIds(creditsPeriodId);
    
        List<CreditsEntry> creditsEntryDepartment = creditsEntryService.find(creditsEntryQueryFilter);
    
        Map<String, AscensoRecord> movimientosMap = new HashMap<>();
        for(CreditsEntry creditsEntry:creditsEntryDepartment){
            String currentCategory = creditsEntry.getEmployment().getPreviousEmployment().getCategory().getCode();
            String proposedCategory = creditsEntry.getEmployment().getCategory().getCode();
            
            String key = currentCategory+"_"+proposedCategory;
            
            AscensoRecord ascensoRecord = movimientosMap.get(key);
            if(ascensoRecord==null){
                ascensoRecord = new AscensoRecord(currentCategory, proposedCategory, 0, creditsEntry.getNumberOfCredits(), 0);
                movimientosMap.put(key, ascensoRecord);
            }
            ascensoRecord.setCantidad(ascensoRecord.getCantidad()+1);
            ascensoRecord.setTotalCreditosIngresosEnCategoria(ascensoRecord.getTotalCreditosIngresosEnCategoria()+creditsEntry.getNumberOfCredits());
           
        }

        List<AscensoRecord> ascensos = movimientosMap.values()
                                      .stream()
                                      .sorted(Comparator.comparing(AscensoRecord::getCategoriaActual)
                                      .thenComparing(Comparator.comparing(AscensoRecord::getCategoriaPropuesta)))
                                      .collect(Collectors.toList());
        return ascensos;

    }

}

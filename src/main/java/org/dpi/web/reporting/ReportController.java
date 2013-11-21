package org.dpi.web.reporting;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsEntry.CreditsEntryVO;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionController;
import org.dpi.web.reporting.dto.GenericReportRecord;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


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
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	/**
	 * Handles and retrieves the download page
	 * 
	 * @return the name of the JSP page
	 */
    @RequestMapping(value = "/buildSetupPage", method = RequestMethod.GET)
    public String getDownloadPage() {
    	logger.debug("Received request to show Report Setup page");
    
    	// Do your work here. Whatever you like
    	// i.e call a custom service to do your business
    	// Prepare a model to be used by the JSP page
    	
    	// This will resolve to /WEB-INF/jsp/downloadpage.jsp
    	return "reports/setup";
	}
 
    /**
     * Retrieves the download file
     * 
     * @return
     */
    @RequestMapping(value = "/creditos", method = RequestMethod.POST)
    public void doCreditosReport(HttpServletRequest request, 
    							HttpServletResponse response
    						/*, ReportParameters parameters*/
    	) throws ServletException, IOException,
		ClassNotFoundException, SQLException, JRException {
    	
		logger.debug("Received request to download creditos report");
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();

		// Call DownloadService to do the actual report processing
		//downloadService.downloadPdf(response);
		HashMap<String, Object> params = new HashMap<String, Object>();
		
		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		//get movimientos de ascenso
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());
		//todos los status
		//empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.setIdCreditsPeriod(currentCreditsPeriod.getId());

		List<CreditsEntry> creditsEntryReparticion = creditsEntryService.find(creditsEntryQueryFilter);

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		List<CreditsEntryVO> creditsEntriesAscensosReparticionVO = creditsEntryService.buildCreditsEntryVO(creditsEntryReparticion,currenUser);
		
			
		
		params.put("MOVIMIENTOS_ASCENSO",  new MovimientosAscensoReportDataSource(getReportDataMovimientosAscenso(creditsEntriesAscensosReparticionVO)));
		
		//get movimientos de Ingreso
		empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());
		//todos los estados
		//empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryQueryFilter.setIdCreditsPeriod(currentCreditsPeriod.getId());
		creditsEntryQueryFilter.setHasCredits(true);
		
		List<CreditsEntry> creditsEntryIngresosReparticion = creditsEntryService.find(creditsEntryQueryFilter);

		int cantidadMovimientosIngreso = 0;
		if(!CollectionUtils.isEmpty(creditsEntryIngresosReparticion)){
			cantidadMovimientosIngreso=creditsEntryIngresosReparticion.size();
		}
		
		params.put("CANTIDAD_MOVIMIENTOS_INGRESO",  cantidadMovimientosIngreso);
		
		params.put("MOVIMIENTOS_INGRESO",  new MovimientosIngresoReportDataSource(getReportDataMovimientosIngreso(creditsEntryIngresosReparticion)));
		
		
		Long creditosDisponiblesAlInicioDelPeriodo =this.administradorCreditosService.getCreditosDisponiblesAlInicioPeriodo(currentCreditsPeriod,reparticion.getId());
		
		Long creditosAcreditadosPorBajaDurantePeriodoActual = this.administradorCreditosService.getCreditosPorBajasDeReparticion(currentCreditsPeriod,reparticion.getId());

		Long creditosPorIngresosOAscensosSolicitados = this.administradorCreditosService.getCreditosPorIngresosOAscensosSolicitados(currentCreditsPeriod, reparticion.getId());

		Long creditosDisponibles = this.administradorCreditosService.getCreditosDisponiblesSegunSolicitado(currentCreditsPeriod,reparticion.getId());
	
		
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_INICIO_PROCESO",creditosDisponiblesAlInicioDelPeriodo);
		params.put("CANTIDAD_CREDITOS_POR_BAJAS",creditosAcreditadosPorBajaDurantePeriodoActual);
		
		params.put("CANTIDAD_CREDITOS_UTILIZADOS",creditosPorIngresosOAscensosSolicitados);
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_AL_FINAL_DEL_PERIODO",creditosDisponibles);
		
		
		downloadService.download("pdf", params, response);
	}
    
    
	private List<GenericReportRecord> getReportDataMovimientosAscenso(List<CreditsEntryVO> creditsEntriesAscensosReparticionVO) {

		final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
		for(CreditsEntryVO creditsEntryAscensosReparticionVO:creditsEntriesAscensosReparticionVO){
			GenericReportRecord record = new GenericReportRecord();
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.PERSON_APELLIDO_NOMBRE.name(),creditsEntryAscensosReparticionVO.getCreditsEntry().getEmployment().getPerson().getApellidoNombre());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.PERSON_CUIL.name(),creditsEntryAscensosReparticionVO.getCreditsEntry().getEmployment().getPerson().getCuil());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.EMPLOYMENT_CURRENT_CATEGORY.name(),creditsEntryAscensosReparticionVO.getCurrentCategory());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.EMPLOYMENT_PROPOSED_CATEGORY.name(),creditsEntryAscensosReparticionVO.getProposedCategory());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.EMPLOYMENT_OCCUPATIONAL_GROUP.name(),creditsEntryAscensosReparticionVO.getOccupationalGroup());
			record.setValue(MovimientosAscensoReportDataSource.ReportFieldID.EMPLOYMENT_PARENT_OCCUPATIONAL_GROUP.name(),creditsEntryAscensosReparticionVO.getParentOccupationalGroup());
			records.add(record);
		}
		
		return records;
	}
	
	
	private List<GenericReportRecord> getReportDataMovimientosIngreso(List<CreditsEntry> creditsEntryIngresosReparticion) {

		final List<GenericReportRecord> records = new ArrayList<GenericReportRecord>();
		for(CreditsEntry creditsEntryIngreso:creditsEntryIngresosReparticion){
			GenericReportRecord record = new GenericReportRecord();
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.PERSON_NUEVO_PERFIL.name(),"");
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.NEW_EMPLOYMENT_PARENT_OCCUPATIONAL_GROUP.name(),"");
			record.setValue(MovimientosIngresoReportDataSource.ReportFieldID.PERSON_NUEVO_CATEGORIA_PROPUESTA.name(),creditsEntryIngreso.getEmployment().getCategory().getCode());
			
			records.add(record);
		}
		
		return records;
	}


}

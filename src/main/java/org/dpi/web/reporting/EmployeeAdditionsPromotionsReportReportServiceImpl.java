package org.dpi.web.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsEntry.CreditsEntryVO;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.web.reporting.dto.GenericReportRecord;
import org.dpi.web.reporting.parameters.EmployeeAdditionsPromotionsReportParameters;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("employeeAdditionsPromotionsReportReportService")
public class EmployeeAdditionsPromotionsReportReportServiceImpl	implements EmployeeAdditionsPromotionsReportReportService 
{
	Log log = LogFactory.getLog(this.getClass());
	
	@Resource(name = "creditsEntryService")
	private CreditsEntryService creditsEntryService;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	

	/**
	 * 
	 */
	public Map<String, Object> getReportData(final EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters) {
		

		long creditsPeriodId = employeeAdditionsPromotionsReportParameters.getCreditsPeriodIds().iterator().next();
		
				
		long reparticionId = employeeAdditionsPromotionsReportParameters.getReparticionIds().iterator().next();

		// Call DownloadService to do the actual report processing
		//downloadService.downloadPdf(response);
		HashMap<String, Object> params = new HashMap<String, Object>();
		


		//get movimientos de ascenso
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setReparticionId( String.valueOf(reparticionId));
		//todos los status
		//empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);

		List<CreditsEntry> creditsEntryReparticion = creditsEntryService.find(creditsEntryQueryFilter);

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		List<CreditsEntryVO> creditsEntriesAscensosReparticionVO = creditsEntryService.buildCreditsEntryVO(creditsEntryReparticion,currenUser);
		
			
		
		params.put("MOVIMIENTOS_ASCENSO",  new MovimientosAscensoReportDataSource(getReportDataMovimientosAscenso(creditsEntriesAscensosReparticionVO)));
		
		//get movimientos de Ingreso
		empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setReparticionId(String.valueOf(reparticionId));
		//todos los estados
		//empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		creditsEntryQueryFilter.setHasCredits(true);
		
		List<CreditsEntry> creditsEntryIngresosReparticion = creditsEntryService.find(creditsEntryQueryFilter);

		int cantidadMovimientosIngreso = 0;
		if(!CollectionUtils.isEmpty(creditsEntryIngresosReparticion)){
			cantidadMovimientosIngreso=creditsEntryIngresosReparticion.size();
		}
		
		params.put("CANTIDAD_MOVIMIENTOS_INGRESO",  cantidadMovimientosIngreso);
		
		params.put("MOVIMIENTOS_INGRESO",  new MovimientosIngresoReportDataSource(getReportDataMovimientosIngreso(creditsEntryIngresosReparticion)));
		
		
		Long creditosDisponiblesAlInicioDelPeriodo =this.creditsManagerService.getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,reparticionId);
		
		Long creditosAcreditadosPorBajaDurantePeriodoActual = this.creditsManagerService.getCreditosPorBajasDeReparticion(creditsPeriodId,reparticionId);

		Long creditosPorIngresosOAscensosSolicitados = this.creditsManagerService.getCreditosPorIngresosOAscensosSolicitados(creditsPeriodId, reparticionId);

		Long creditosDisponibles = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodId,reparticionId);
	
		
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_INICIO_PROCESO",creditosDisponiblesAlInicioDelPeriodo);
		params.put("CANTIDAD_CREDITOS_POR_BAJAS",creditosAcreditadosPorBajaDurantePeriodoActual);
		
		params.put("CANTIDAD_CREDITOS_UTILIZADOS",creditosPorIngresosOAscensosSolicitados);
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_AL_FINAL_DEL_PERIODO",creditosDisponibles);

			
		return params;
		

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

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
		
				
		long departmentId = employeeAdditionsPromotionsReportParameters.getDepartmentIds().iterator().next();

		// Call DownloadService to do the actual report processing
		//downloadService.downloadPdf(response);
		HashMap<String, Object> params = new HashMap<String, Object>();
		


		//get movimientos de ascenso
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setDepartmentId(departmentId);
		//todos los status
		//empleoQueryFilter.setEstadosEmpleo(CollectionUtils.arrayToList(EstadoEmpleo.values()));
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);

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
		creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriodId);
		creditsEntryQueryFilter.setHasCredits(true);
		
		List<CreditsEntry> creditsEntryIngresosDepartment = creditsEntryService.find(creditsEntryQueryFilter);

		int cantidadMovimientosIngreso = 0;
		if(!CollectionUtils.isEmpty(creditsEntryIngresosDepartment)){
			cantidadMovimientosIngreso=creditsEntryIngresosDepartment.size();
		}
		
		params.put("CANTIDAD_MOVIMIENTOS_INGRESO",  cantidadMovimientosIngreso);
		
		params.put("MOVIMIENTOS_INGRESO",  new AdditionsCreditEntriesReportDataSource(getReportDataMovimientosIngreso(creditsEntryIngresosDepartment)));
		
		
		Long creditosDisponiblesAlInicioDelPeriodo =this.creditsManagerService.getCreditosDisponiblesAlInicioPeriodo(creditsPeriodId,departmentId);
		
		Long creditosAcreditadosPorBajaDurantePeriodoActual = this.creditsManagerService.getCreditosPorBajasDeReparticion(creditsPeriodId,departmentId);

		Long creditosPorIngresosOAscensosSolicitados = this.creditsManagerService.getCreditosPorIngresosOAscensosSolicitados(creditsPeriodId, departmentId);

		Long creditosDisponibles = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodId,departmentId);
	
		
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_INICIO_PROCESO",creditosDisponiblesAlInicioDelPeriodo);
		params.put("CANTIDAD_CREDITOS_POR_BAJAS",creditosAcreditadosPorBajaDurantePeriodoActual);
		
		params.put("CANTIDAD_CREDITOS_UTILIZADOS",creditosPorIngresosOAscensosSolicitados);
		params.put("CANTIDAD_CREDITOS_DISPONIBLES_AL_FINAL_DEL_PERIODO",creditosDisponibles);

			
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
			
			records.add(record);
		}
		
		return records;
	}



}

package org.dpi.creditsManagement;

import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.departmentCreditsEntry.DepartmentCreditsEntryQueryFilter;


public interface CreditsManagerService {

	public int getCreditosPorCargaInicial(String categoryCode);
	
	public int getCreditosPorBaja(String categoryCode);
	
	public int getCreditosPorAscenso(String currentCategoryCode, String newCategoryCode);
	
	public int getCreditosPorIngreso(String categoryCode);
	
	public Long getCreditosPorCargaInicialDeReparticion(final Long creditsPeriodId, final long departmentId);
	
	public Long getCreditosPorBajasDeReparticion(final Long creditsPeriodId,final long departmentId);
	
	public Long getRetainedCreditsByDepartment(final Long creditsPeriodId,final long departmentId);
		
	public Long getCreditsEntriesSum(CreditsEntryQueryFilter creditsEntryQueryFilter);

	public Long getCreditosPorIngresosOAscensosSolicitados(Long creditsPeriodId, Long departmentId);
	
	public Long getCreditosPorIngresosOAscensosOtorgados(Long creditsPeriodId, Long departmentId);
		
	public Long getCreditosDisponiblesSegunSolicitado(Long creditsPeriodId,long departmentId);
	
	public Long getCreditosDisponiblesSegunOtorgado(Long creditsPeriodId ,long departmentId);
	
	public Long getTotalCreditos(final CreditsEntryQueryFilter creditsEntryQueryFilter);

	public Long getCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, Long departmentId);
	
	/*this is for considering retentions*/
	public Long getTotalCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, long departmentId);
	
	
	public Long getTotalDepartmentCreditEntries(final DepartmentCreditsEntryQueryFilter departmentCreditsEntryQueryFilter);
	
    public Long getCreditosReparticionAjustesDebitoPeriodo(Long creditsPeriodId, Long departmentId);
    
    public Long getCreditosReparticionAjustesCreditoPeriodo(Long creditsPeriodId, Long departmentId);
    
    public Long getCreditosReparticion_ReasignadosDeRetencion_Periodo(Long creditsPeriodId, Long departmentId);
    
    public Long getCreditosReparticion_ReubicacionDeReparticion_Periodo(Long creditsPeriodId, Long departmentId);
	
	

}
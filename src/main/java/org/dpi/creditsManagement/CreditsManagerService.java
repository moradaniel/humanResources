package org.dpi.creditsManagement;

import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.person.PersonCondition;


public interface CreditsManagerService {

	public int getCreditosPorCargaInicial(String categoryCode);
	
	public int getCreditosPorBaja(String categoryCode);
	
	public int getCreditosPorAscenso(PersonCondition personCondition, String currentCategoryCode, String newCategoryCode);
	
	public int getCreditosPorIngreso(String categoryCode);
	
	public Long getCreditosPorCargaInicialDeReparticion(final Long creditsPeriodId, final long reparticionId);
	
	public Long getCreditosPorBajasDeReparticion(final Long creditsPeriodId,final long reparticionId);
	
	public Long getRetainedCreditsByDepartment(final Long creditsPeriodId,final long reparticionId);
		
	public Long getCreditsEntriesSum(CreditsEntryQueryFilter creditsEntryQueryFilter);

	public Long getCreditosPorIngresosOAscensosSolicitados(Long creditsPeriodId, Long reparticionId);
	
	public Long getCreditosPorIngresosOAscensosOtorgados(Long creditsPeriodId, Long reparticionId);
		
	public Long getCreditosDisponiblesSegunSolicitado(Long creditsPeriodId,long reparticionId);
	
	public Long getCreditosDisponiblesSegunOtorgado(Long creditsPeriodId ,long reparticionId);
	
	public Long getTotalCreditos(final CreditsEntryQueryFilter creditsEntryQueryFilter);

	public Long getCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, Long reparticionId);
	
	/*this is for considering retentions*/
	public Long getTotalCreditosDisponiblesAlInicioPeriodo(Long creditsPeriodId, long reparticionId);
	
	

}
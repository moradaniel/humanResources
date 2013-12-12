package org.dpi.creditsManagement;

import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.person.PersonCondition;


public interface CreditsManagerService {

	public int getCreditosPorCargaInicial(String categoryCode);
	
	public int getCreditosPorBaja(String categoryCode);
	
	public int getCreditosPorAscenso(PersonCondition personCondition, String currentCategoryCode, String newCategoryCode);
	
	public int getCreditosPorIngreso(String categoryCode);
	
	public Long getCreditosPorCargaInicialDeReparticion(final CreditsPeriod creditsPeriod, final long reparticionId);
	
	public Long getCreditosPorBajasDeReparticion(final CreditsPeriod creditsPeriod, final long reparticionId);

	public Long getCreditosPorIngresosOAscensosSolicitados(CreditsPeriod creditsPeriod, Long reparticionId);
	
	public Long getCreditosPorIngresosOAscensosOtorgados(CreditsPeriod creditsPeriod, Long reparticionId);
		
	public Long getCreditosDisponiblesSegunSolicitado(CreditsPeriod creditsPeriod,long reparticionId);
	
	public Long getCreditosDisponiblesSegunOtorgado(CreditsPeriod creditsPeriod,long reparticionId);
	
	public Long getTotalCreditos(final CreditsEntryQueryFilter creditsEntryQueryFilter);

	public Long getCreditosDisponiblesAlInicioPeriodo(CreditsPeriod creditsPeriod, Long id);
	

}
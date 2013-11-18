package org.dpi.configuracionAsignacionCreditos;

import org.dpi.agente.CondicionAgente;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;


public interface AdministradorCreditosService {

	public int getCreditosPorCargaInicial(String categoryCode);
	
	public int getCreditosPorBaja(String categoryCode);
	
	public int getCreditosPorAscenso(CondicionAgente condicionAgente, String currentCategoryCode, String newCategoryCode);
	
	public int getCreditosPorIngreso(String categoryCode);
	
	public Long getCreditosPorCargaInicialDeReparticion(final CreditsPeriod creditsPeriod, final long reparticionId);
	
	public Long getCreditosPorBajasDeReparticion(final CreditsPeriod creditsPeriod, final long reparticionId);

	public Long getCreditosPorIngresosOAscensosSolicitados(CreditsPeriod creditsPeriod, Long reparticionId);
	
	public Long getCreditosPorIngresosOAscensosOtorgados(CreditsPeriod creditsPeriod, Long reparticionId);
		
	public Long getCreditosDisponiblesSegunSolicitado(CreditsPeriod creditsPeriod,long reparticionId);
	
	public Long getCreditosDisponiblesSegunOtorgado(CreditsPeriod creditsPeriod,long reparticionId);
	
	public Long getTotalCreditos(final MovimientoCreditosQueryFilter movimientoCreditosQueryFilter);

	public Long getCreditosDisponiblesAlInicioPeriodo(CreditsPeriod creditsPeriod, Long id);
	

}
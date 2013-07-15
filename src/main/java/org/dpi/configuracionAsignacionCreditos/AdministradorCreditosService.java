package org.dpi.configuracionAsignacionCreditos;

import org.dpi.agente.CondicionAgente;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodImpl;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;


public interface AdministradorCreditosService {

	/**
	 * TODO agregar fecha de validez
	 * @return
	 */
	public int getCreditosPorCargaInicial(String codigoCategoria);
	
	public int getCreditosPorBaja(String codigoCategoria);
	
	public int getCreditosPorAscenso(CondicionAgente condicionAgente, String codigoCategoriaActual, String codigoCategoriaNueva);
	
	public int getCreditosPorIngreso(String codigoCategoria);
	
	public Long getCreditosPorCargaInicialDeReparticion(final long reparticionId);
	
	public Long getCreditosPorBajasDeReparticion(final long reparticionId);
	


	public Long getCreditosDisponiblesAlInicioDelPeriodo(Long id);

	public Long getCreditosPorIngresosOAscensosSolicitados(CreditsPeriod creditsPeriod, Long reparticionId);
	public Long getCreditosPorIngresosOAscensosOtorgados(CreditsPeriodImpl creditsPeriodImpl, Long reparticionId);
	
	
	public Long getCreditosDisponiblesSegunSolicitado(long reparticionId);
	public Long getCreditosDisponiblesSegunOtorgado(long reparticionId);
	
	public 	Long getTotalCreditos(final MovimientoCreditosQueryFilter movimientoCreditosQueryFilter);

	public Long getCreditosDisponiblesAlInicioPeriodo(CreditsPeriodImpl creditsPeriodImpl, Long id);


}
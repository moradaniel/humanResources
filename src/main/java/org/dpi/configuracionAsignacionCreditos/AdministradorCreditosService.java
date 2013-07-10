package org.dpi.configuracionAsignacionCreditos;

import org.dpi.agente.CondicionAgente;


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
	
	public Long getCreditosPorIngresosOAscensos(final long reparticionId);
	
	public Long getCreditosDisponibles(long reparticionId);

	public Long getCreditosDisponiblesAlInicioDelPeriodo(Long id);

}
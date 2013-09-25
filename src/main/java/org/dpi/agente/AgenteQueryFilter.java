package org.dpi.agente;

import java.io.Serializable;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class AgenteQueryFilter implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String apellidoNombre;
	private String cuil;
	private Long agenteId;
	private CondicionAgente condicionAgente;

	//EstadoAgente estadoAgente = EstadoAgente.ACTIVO;
	

	private Long reparticionId;
	

	public String getApellidoNombre() {
		return apellidoNombre;
	}
	public void setApellidoNombre(String apellidoNombre) {
		this.apellidoNombre = apellidoNombre;
	}
	public String getCuil() {
		return cuil;
	}
	public void setCuil(String cuil) {
		this.cuil = cuil;
	}

	public Long getReparticionId() {
		return reparticionId;
	}
	public void setReparticionId(Long reparticionId) {
		this.reparticionId = reparticionId;
	}
	
	/*public EstadoAgente getEstadoAgente() {
		return estadoAgente;
	}*/
	
	/*public void setEstadoAgente(EstadoAgente estadoAgente) {
		this.estadoAgente = estadoAgente;
	}*/
	
	public Long getAgenteId() {
		return agenteId;
	}
	public void setAgenteId(Long agenteId) {
		this.agenteId = agenteId;
	}
	
	public CondicionAgente getCondicionAgente() {
		return condicionAgente;
	}
	
	public void setCondicionAgente(CondicionAgente condicionAgente) {
		this.condicionAgente = condicionAgente;
	}

}

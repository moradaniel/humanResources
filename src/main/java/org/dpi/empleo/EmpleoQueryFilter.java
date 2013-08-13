package org.dpi.empleo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class EmpleoQueryFilter implements Serializable {
	
	/*public enum estado{
		ACTIVO,
		DE_BAJA,
		INACTIVO,
		PENDIENTE,
		TODOS
	}*/

	private static final long serialVersionUID = 1L;
	
	private String apellidoNombre;
	private String cuil;
	private String codigoCentro;
	private String codigoSector;
	private String codigoCategoria;
	private Long agenteId;

	private String reparticionId;
	private String empleoId;
	
	private Date fechaComienzo;
	private Date fechaFin;

	
	List<EstadoEmpleo> estadosEmpleo = new ArrayList<EstadoEmpleo>();
	
	

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
	public String getCodigoCentro() {
		return codigoCentro;
	}
	public void setCodigoCentro(String codigoCentro) {
		this.codigoCentro = codigoCentro;
	}
	public String getCodigoSector() {
		return codigoSector;
	}
	public void setCodigoSector(String codigoSector) {
		this.codigoSector = codigoSector;
	}
	public Date getFechaComienzo() {
		return fechaComienzo;
	}
	public void setFechaComienzo(Date fechaComienzo) {
		this.fechaComienzo = fechaComienzo;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCodigoCategoria() {
		return codigoCategoria;
	}
	public void setCodigoCategoria(String codigoCategoria) {
		this.codigoCategoria = codigoCategoria;
	}
	
	public String getReparticionId() {
		return reparticionId;
	}
	public void setReparticionId(String reparticionId) {
		this.reparticionId = reparticionId;
	}
	
	public String getEmpleoId() {
		return empleoId;
	}
	public void setEmpleoId(String empleoId) {
		this.empleoId = empleoId;
	}
	
	public List<EstadoEmpleo> getEstadosEmpleo() {
		return estadosEmpleo;
	}
	public void setEstadosEmpleo(List<EstadoEmpleo> estadosEmpleo) {
		this.estadosEmpleo = estadosEmpleo;
	}
	
	public void addEstadoEmpleo(EstadoEmpleo estadoEmpleo){
		if(this.estadosEmpleo == null){
			this.estadosEmpleo = new ArrayList<EstadoEmpleo>();
		}
		this.estadosEmpleo.add(estadoEmpleo);
	}
	
	public Long getAgenteId() {
		return agenteId;
	}
	public void setAgenteId(Long agenteId) {
		this.agenteId = agenteId;
	}
}

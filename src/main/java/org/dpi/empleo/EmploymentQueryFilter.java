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
public class EmploymentQueryFilter implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	private String apellidoNombre;
	private String cuil;
	private String codigoCentro;
	private String codigoSector;
	private String categoryCode;
	private List<Long> agentesIds= new ArrayList<Long>();

	private String reparticionId;
	private String empleoId;
	
	private Date fechaComienzo;
	private Date fechaFin;

	
	List<EmploymentStatus> estadosEmpleo = new ArrayList<EmploymentStatus>();
	
	

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
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
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
	
	public List<EmploymentStatus> getEstadosEmpleo() {
		return estadosEmpleo;
	}
	public void setEstadosEmpleo(List<EmploymentStatus> estadosEmpleo) {
		this.estadosEmpleo = estadosEmpleo;
	}
	
	public void addEstadoEmpleo(EmploymentStatus estadoEmpleo){
		if(this.estadosEmpleo == null){
			this.estadosEmpleo = new ArrayList<EmploymentStatus>();
		}
		this.estadosEmpleo.add(estadoEmpleo);
	}
	
	public List<Long> getAgentesIds() {
		return agentesIds;
	}
	
	public void setAgentesIds(List<Long> agentesIds) {
		this.agentesIds = agentesIds;
	}
	
	public void addAgenteId(Long agenteId) {
		this.agentesIds.add(agenteId);
	}
	
	
}

package org.dpi.employment;

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
	private List<Long> personsIds= new ArrayList<Long>();

	private String reparticionId;
	private String employmentId;
	
	private Date fechaComienzo;
	private Date fechaFin;

	
	List<EmploymentStatus> employmentStatuses = new ArrayList<EmploymentStatus>();
	
	

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
	
	public String getEmploymentId() {
		return employmentId;
	}
	public void setEmploymentId(String employmentId) {
		this.employmentId = employmentId;
	}
	
	public List<EmploymentStatus> getEmploymentStatuses() {
		return employmentStatuses;
	}
	public void setEmploymentStatuses(List<EmploymentStatus> employmentStatuses) {
		this.employmentStatuses = employmentStatuses;
	}
	
	public void addEmploymentStatus(EmploymentStatus employmentStatus){
		if(this.employmentStatuses == null){
			this.employmentStatuses = new ArrayList<EmploymentStatus>();
		}
		this.employmentStatuses.add(employmentStatus);
	}
	
	public List<Long> getPersonsIds() {
		return personsIds;
	}
	
	public void setPersonsIds(List<Long> personsIds) {
		this.personsIds = personsIds;
	}
	
	public void addPersonId(Long personId) {
		this.personsIds.add(personId);
	}
	
	
}

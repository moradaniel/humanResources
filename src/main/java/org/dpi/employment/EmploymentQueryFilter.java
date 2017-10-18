package org.dpi.employment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.util.query.AbstractQueryFilter;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class EmploymentQueryFilter extends AbstractQueryFilter implements Serializable {
	

	private static final long serialVersionUID = 1L;
	
	private String apellidoNombre;
	private String cuil;
	private String codigoCentro;
	private String codigoSector;
	private String categoryCode;
	private List<Long> personsIds= new ArrayList<Long>();

	private Long departmentId;
	private Long employmentId;
	
	private Long previousEmploymentId;
	
	private Date startDate;

	private Date endDate;

	
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getCategoryCode() {
		return categoryCode;
	}
	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
	public Long getDepartmentId() {
		return departmentId;
	}
	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}
	
	public Long getEmploymentId() {
		return employmentId;
	}
	public void setEmploymentId(Long employmentId) {
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
	
	
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
    public Long getPreviousEmploymentId() {
        return previousEmploymentId;
    }
    public void setPreviousEmploymentId(Long previousEmploymentId) {
        this.previousEmploymentId = previousEmploymentId;
    }

	@Override
	public void reset() {
		/*role = null;
		fromDate = null;
		toDate = null;
		activeLoanEncharges = null;
		consultancyType = null;	
		posId = null;
		acId = null;
		amId = null;*/
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("centro", codigoCentro).
                append("sector", codigoSector).
                append("cuil", cuil).
                toString();
    }
}

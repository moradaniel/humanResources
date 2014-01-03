package org.dpi.employment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.category.Category;
import org.dpi.centroSector.CentroSector;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.domain.PersistentAbstract;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.person.Person;
import org.janux.util.JanuxToStringStyle;

public class EmploymentImpl  extends PersistentAbstract implements Employment{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Person person;
	CentroSector centroSector;
	
	Category category;
	
	private Set<CreditsEntry> creditsEntries = new HashSet<CreditsEntry>();

	EmploymentStatus status;
	
	Date fechaInicio;
	Date fechaFin;
	
	Employment previousEmployment;
	
	OccupationalGroup occupationalGroup;
	

	@Override
	public Person getPerson() {
		return this.person;
	}

	@Override
	public void setPerson(Person person) {
		this.person=person;
		
	}
	
	@Override
	public CentroSector getCentroSector() {
		return this.centroSector;
	}

	@Override
	public void setCentroSector(CentroSector centroSector) {
		this.centroSector=centroSector;
		
	}
	
	@Override
	public Date getFechaInicio() {
		return fechaInicio;
	}

	@Override
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	@Override
	public Date getFechaFin() {
		return fechaFin;
	}

	@Override
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	
	
	@Override
	public Category getCategory() {
		return category;
	}
	
	@Override
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@Override
	public Set<CreditsEntry> getCreditsEntries() {
		return this.creditsEntries;
	}
	
	@Override
	public void setCreditsEntries(Set<CreditsEntry> creditsEntries) {
		this.creditsEntries = creditsEntries;
		
	}

	@Override
	public void addCreditsEntry(CreditsEntry creditsEntry) {
		this.creditsEntries.add(creditsEntry);
	}

	@Override
	public EmploymentStatus getStatus() {
		return status;
	}

	@Override
	public void setStatus(EmploymentStatus status) {
		this.status= status;
		
	}

	@Override
	public boolean isClosed() {
		if (this.fechaFin == null)
			return false;
		return true;
	}

	@Override
	public Employment getPreviousEmployment() {
		return previousEmployment;
	}

	@Override
	public void setPreviousEmployment(Employment previousEmployment) {
		this.previousEmployment = previousEmployment;
	}
	
	@Override
	public OccupationalGroup getOccupationalGroup() {
		return occupationalGroup;
	}

	@Override
	public void setOccupationalGroup(OccupationalGroup occupationalGroup) {
		this.occupationalGroup = occupationalGroup;
	}
	
	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("person", person.toString());
		
		sb.append("category", getCategory().getCode());
		sb.append("status", getStatus());

		
		return sb.toString();
	}

}

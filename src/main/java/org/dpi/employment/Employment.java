package org.dpi.employment;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.dpi.category.Category;
import org.dpi.centroSector.CentroSector;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.Persistent;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.person.Person;


public interface Employment extends Persistent,Serializable{
	

	public Person getPerson();

	public void setPerson(Person person);

	public CentroSector getCentroSector();

	public void setCentroSector(CentroSector centroSector);
	
	public Category getCategory();

	public void setCategory(Category category);
	
	public EmploymentStatus getStatus();

	public void setStatus(EmploymentStatus status);

	public Date getStartDate();

	public void setStartDate(Date startDate);

	public Date getEndDate();

	public void setEndDate(Date endDate);
	
	public Set<CreditsEntry> getCreditsEntries();
	
	public Set<CreditsEntry> getCreditsEntries(CreditsPeriod period, CreditsEntryType... creditsEntryTypes);
	
	public void setCreditsEntries(Set<CreditsEntry> creditsEntries);

	public void addCreditsEntry(CreditsEntry creditsEntry);

	public boolean isClosed();

	public Employment getPreviousEmployment();

	public void setPreviousEmployment(Employment previousEmployment);
	
	public OccupationalGroup getOccupationalGroup();

	public void setOccupationalGroup(OccupationalGroup occupationalGroup);
	

}
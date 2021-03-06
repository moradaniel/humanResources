package org.dpi.employment;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.dpi.category.Category;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.domain.PersistentAbstract;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.person.Person;
import org.dpi.subDepartment.SubDepartment;



public class EmploymentImpl  extends PersistentAbstract implements Employment{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Person person;
	SubDepartment subDepartment;
	
	Category category;
	
	private Set<CreditsEntry> creditsEntries = new HashSet<CreditsEntry>();

	EmploymentStatus status;
	
	Date startDate;
	Date endDate;
	
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
	public SubDepartment getSubDepartment() {
		return this.subDepartment;
	}

	@Override
	public void setSubDepartment(SubDepartment subDepartment) {
		this.subDepartment=subDepartment;
		
	}
	
	@Override
	// @JsonSerialize(using=DateSerializer.class)
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Override
	// @JsonSerialize(using=DateSerializer.class)
	public Date getEndDate() {
		return endDate;
	}

	@Override
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
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
		if (this.endDate == null)
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
	public Set<CreditsEntry> getCreditsEntries(CreditsPeriod period, CreditsEntryType... creditsEntryTypes){
		HashSet<CreditsEntry> foundCreditsEntries = new HashSet<CreditsEntry>();
		
		Set<CreditsEntryType> creditsEntryTypesSet = new HashSet<CreditsEntryType>();
		for (CreditsEntryType creditsEntryType : creditsEntryTypes) {
			creditsEntryTypesSet.add(creditsEntryType);
		}
		
		for(CreditsEntry entry:creditsEntries) {
			if(creditsEntryTypesSet.contains(entry.getCreditsEntryType()) 
				 && 	entry.getCreditsPeriod().getId().longValue()==period.getId().longValue()
					
					) {
				
				foundCreditsEntries.add(entry);
			}
		}
		return foundCreditsEntries;
		
	}

    @Override
    public String toString() {
        return new ToStringBuilder(this).
                append("person", person).
                append("subDepartment", subDepartment).
                append("status", status).
                append("startDate", startDate).
                append("endDate", endDate).
                append("occupationalGroup", occupationalGroup).
                toString();
    }

	
/*	@Override
	public String toString() 
	{
		ToStringBuilder sb = new ToStringBuilder(this, JanuxToStringStyle.COMPACT);
		
		sb.append(super.toString());
		
		sb.append("person", person.toString());
		sb.append("status", getStatus());
		
		sb.append("category", getCategory().getCode());
		sb.append("occupationalGroup", getOccupationalGroup().toString());
		
		
		sb.append("subdepartment", getSubDepartment().toString());
		
		return sb.toString();
	}*/

}

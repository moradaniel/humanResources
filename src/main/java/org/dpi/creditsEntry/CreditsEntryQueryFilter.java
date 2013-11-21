package org.dpi.creditsEntry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.employment.EmploymentQueryFilter;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class CreditsEntryQueryFilter implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	EmploymentQueryFilter employmentQueryFilter;
	
	Long id;
	
	Long idCreditsPeriod;

	List<CreditsEntryType> creditsEntryTypes = new ArrayList<CreditsEntryType>();
	
	List<GrantedStatus> grantedStatuses = new ArrayList<GrantedStatus>();
	
	Boolean hasCredits = null;
	

	public CreditsEntryQueryFilter(){
		
	}

	public EmploymentQueryFilter getEmploymentQueryFilter() {
		return employmentQueryFilter;
	}

	public void setEmploymentQueryFilter(EmploymentQueryFilter employmentQueryFilter) {
		this.employmentQueryFilter = employmentQueryFilter;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public List<CreditsEntryType> getCreditsEntryTypes() {
		return creditsEntryTypes;
	}

	public void setCreditsEntryTypes(
			List<CreditsEntryType> tiposCreditsEntry) {
		this.creditsEntryTypes = tiposCreditsEntry;
	}
	
	public void addCreditsEntryType(CreditsEntryType tipoCreditsEntry) {
		this.creditsEntryTypes.add(tipoCreditsEntry);
	}
	
	public List<GrantedStatus> getGrantedStatuses(){
		return this.grantedStatuses;
	}

	public void setGrantedStatuses(
			List<GrantedStatus> grantedStatuses) {
		this.grantedStatuses = grantedStatuses;
	}
	
	public void addGrantedStatus(GrantedStatus grantedStatus) {
		this.grantedStatuses.add(grantedStatus);
	}
	
	public Long getIdCreditsPeriod() {
		return idCreditsPeriod;
	}

	public void setIdCreditsPeriod(Long idCreditsPeriod) {
		this.idCreditsPeriod = idCreditsPeriod;
	}
	
	public Boolean isHasCredits() {
		return hasCredits;
	}

	public void setHasCredits(Boolean hasCredits) {
		this.hasCredits = hasCredits;
	}
	
}

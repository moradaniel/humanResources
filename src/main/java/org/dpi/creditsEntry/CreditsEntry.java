package org.dpi.creditsEntry;

import java.io.Serializable;
import java.util.List;

import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.domain.Persistent;
import org.dpi.employment.Employment;


public interface CreditsEntry extends Persistent,Serializable{
	
	public enum GrantedStatus{
		Solicitado, //Requested
		Otorgado //Granted
	}

	Employment getEmployment();

	void setEmployment(Employment employment);
	
	int getNumberOfCredits();

	void setNumberOfCredits(int numberOfCredits);
	
	public CreditsEntryType getCreditsEntryType();

	public void setCreditsEntryType(CreditsEntryType creditsEntryType);
	
	void setNotes(String notes);
	
	String getNotes();

	public CreditsPeriod getCreditsPeriod();
	public void setCreditsPeriod(CreditsPeriod grantedStatus);

	public GrantedStatus getGrantedStatus();
	public void setGrantedStatus(GrantedStatus grantedStatus);
	
    boolean canCreditsEntryStatusBeChanged(CreditsEntryService creditsEntryService,CreditsPeriodService creditsPeriodService);

    void setSubsequentCreditsEntries(List<CreditsEntry> subsequentCreditsEntries);
    List<CreditsEntry> getSubsequentCreditsEntries(CreditsEntryService creditsEntryService);
	
}
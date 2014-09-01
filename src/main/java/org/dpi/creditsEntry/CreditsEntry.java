package org.dpi.creditsEntry;

import java.io.Serializable;

import org.dpi.creditsPeriod.CreditsPeriod;
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
	
}
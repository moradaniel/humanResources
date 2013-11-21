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
	
	int getCantidadCreditos();

	void setCantidadCreditos(int cantidadCreditos);
	
	public CreditsEntryType getCreditsEntryType();

	public void setCreditsEntryType(CreditsEntryType creditsEntryType);
	
	void setObservaciones(String observaciones);
	
	String getObservaciones();

	public CreditsPeriod getCreditsPeriod();
	public void setCreditsPeriod(CreditsPeriod grantedStatus);

	public GrantedStatus getGrantedStatus();
	public void setGrantedStatus(GrantedStatus grantedStatus);
	
}
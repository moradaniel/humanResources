package org.dpi.creditsPeriod;

import java.io.Serializable;
import java.util.Date;

import org.dpi.domain.Persistent;

public interface CreditsPeriod extends Persistent,Serializable{
	
	public enum Status{
		Active,
		Closed
	}
	

	public String getName();

	public void setName(String name);

	public Date getStartDate();

	public void setStartDate(Date startDate);

	public Date getEndDate();

	public void setEndDate(Date endDate);

	public String getDescription();

	public void setDescription(String description);
	
	public Status getStatus();
	
	public void setStatus(Status status);
	
	public CreditsPeriod getPreviousCreditsPeriod();

	public void setPreviousCreditsPeriod(CreditsPeriod previousCreditsPeriod);
	

}

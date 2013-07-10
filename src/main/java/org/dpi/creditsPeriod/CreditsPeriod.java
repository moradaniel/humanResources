package org.dpi.creditsPeriod;

import java.util.Date;

public interface CreditsPeriod {
	

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
	

}

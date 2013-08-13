package org.dpi.creditsPeriod;

import java.util.Date;

import org.dpi.domain.PersistentAbstract;

public class CreditsPeriodImpl extends PersistentAbstract implements CreditsPeriod {

	private String name;
	private Date startDate;
	private Date endDate;
	private Status status;
	private String description;
	
	private CreditsPeriod previousCreditsPeriod;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public void setStatus(Status status) {
		this.status = status;
		
	}
	
	@Override
	public CreditsPeriod getPreviousCreditsPeriod() {
		return previousCreditsPeriod;
	}

	@Override
	public void setPreviousCreditsPeriod(CreditsPeriod previousCreditsPeriod) {
		this.previousCreditsPeriod = previousCreditsPeriod;
	}
	
}

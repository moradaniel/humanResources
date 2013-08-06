package org.dpi.creditsPeriod;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.dpi.creditsPeriod.CreditsPeriod.Status;


/**
 * DAO implementation agnostic class that can hold commons constraints to be used
 * by query DAO query methods. The class allows to keep informations useful to do 
 * common things on object retrieval methods like multiple field results ordering, 
 * paging, and very common by date filtering.
 * 
 *
 */
public class CreditsPeriodQueryFilter implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	private String creditsPeriodId;
	private String name;
	
	private Date startDate;
	private Date endDate;
	
	List<CreditsPeriod.Status> statuses;
	
	public String getCreditsPeriodId() {
		return creditsPeriodId;
	}

	public void setCreditsPeriodId(String creditsPeriodId) {
		this.creditsPeriodId = creditsPeriodId;
	}

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


	public List<Status> getStatuses() {
		return statuses;
	}

	public void setStatuses(
			List<Status> statuses) {
		this.statuses = statuses;
	}
	
	public void addStatus(Status status) {
		this.statuses.add(status);
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	

}

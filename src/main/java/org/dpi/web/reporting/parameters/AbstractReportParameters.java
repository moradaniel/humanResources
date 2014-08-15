package org.dpi.web.reporting.parameters;

import java.util.HashSet;
import java.util.Set;

import biz.janux.calendar.DateRange;



public abstract class AbstractReportParameters
{
	/** defines the allowed types of report output */
    public enum OutputFormat {HTML, XML, TEXT, PDF, XLS}
	//** defines the allowed types of report output 
    //public enum ReportDateType {BOOK_DATE, ARRIVE_DATE, DEPART_DATE}

	// the date range for the report 
    private DateRange dateRange;
    /* specifies whether given date range applies to booking date, arrival date, or departure date
    //private ReportDateType dateType;

    //** specifies the desired report output format */
    private OutputFormat outputFormat;
    
	private Set<Long> departmentIds = new HashSet<Long>();
	
	private Set<Long> creditsPeriodIds = new HashSet<Long>();
		

	
	public abstract String getTemplateFileName();

    
	public OutputFormat getOutputFormat()
	{
		return outputFormat;
	}
	public void setOutputFormat(OutputFormat outputFormat)
	{
		this.outputFormat = outputFormat;
	}
	public DateRange getDateRange() {
		return dateRange;
	}
	public void setDateRange(DateRange dateRange) {
		this.dateRange = dateRange;
	}
	
	public Set<Long> getCreditsPeriodIds() {
		return creditsPeriodIds;
	}



	public void setCreditsPeriodIds(Set<Long> creditsPeriodIds) {
		this.creditsPeriodIds = creditsPeriodIds;
	}



	public Set<Long> getDepartmentIds() {
		return departmentIds;
	}


	public void setDepartmentIds(Set<Long> departmentIds) {
		this.departmentIds = departmentIds;
	}
	
	public void addCreditsPeriod(Long creditsPeriodId) {
		this.creditsPeriodIds.add(creditsPeriodId);
	}

	public void addDepartment(Long departmentId) {
		this.departmentIds.add(departmentId);
	}

}
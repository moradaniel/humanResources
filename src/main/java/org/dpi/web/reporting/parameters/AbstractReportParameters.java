package org.dpi.web.reporting.parameters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.janux.bus.security.Account;

import biz.janux.calendar.DateRange;



public abstract class AbstractReportParameters
{
    String reportCode;
    
    /** defines the allowed types of report output */
    //public enum OutputFormat {PDF, XLS}
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
    List<String> creditPeriodNames = new ArrayList<>();
        
    Account generatedByUser;
    
    public abstract String getTemplateFileName();

    public String getReportCode() {
        return reportCode;
    }


    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }
    
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

    public List<String> getCreditPeriodNames() {
        return creditPeriodNames;
    }

    public void addCreditPeriodNames(String ... creditPeriodNames) {
        this.creditPeriodNames.addAll(Arrays.asList(creditPeriodNames));
    }

    public void setCreditPeriodNames(List<String> creditPeriodNames) {
        this.creditPeriodNames = creditPeriodNames;
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
    
    public Account getGeneratedByUser() {
        return generatedByUser;
    }


    public void setGeneratedByUser(Account generatedByUser) {
        this.generatedByUser = generatedByUser;
    }


}
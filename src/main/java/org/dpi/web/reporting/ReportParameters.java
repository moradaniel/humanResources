package org.dpi.web.reporting;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.janux.bus.security.Account;

public class ReportParameters {
    
    String reportCode;
    
    OutputFormat selectedOutputFormat;
    
    Date startDate;
    Date endDate;
    
    String selectedPeriodName;
    
    Set<Long> departmentIds = new HashSet<>();
    
    String opcion2;

    Account generatedByUser;
    
    public String getReportCode() {
        return reportCode;
    }

    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
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

    public Set<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(Set<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
    
    
    public void addDepartmentIds(Long ... departmentIds) {
        this.departmentIds.addAll(Arrays.asList(departmentIds));
    }
    
    
    public String getOpcion2() {
        return opcion2;
    }

    public void setOpcion2(String opcion2) {
        this.opcion2 = opcion2;
    }

    public String getSelectedPeriodName() {
        return selectedPeriodName;
    }

    public void setSelectedPeriodName(String selectedPeriodName) {
        this.selectedPeriodName = selectedPeriodName;
    }

    public OutputFormat getSelectedOutputFormat() {
        return selectedOutputFormat;
    }

    public void setSelectedOutputFormat(OutputFormat selectedOutputFormat) {
        this.selectedOutputFormat = selectedOutputFormat;
    }

    public void setGeneratedByUser(Account generatedByUser) {
        this.generatedByUser = generatedByUser;
        
    }

    public Account getGeneratedByUser() {
        return generatedByUser;
    }
}

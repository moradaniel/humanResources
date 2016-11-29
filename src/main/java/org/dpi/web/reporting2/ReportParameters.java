package org.dpi.web.reporting2;

import java.sql.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.dpi.web.reporting2.AbstractReportParameters.OutputFormat;
import org.janux.bus.security.Account;

public class ReportParameters {
    
    //public enum OutputFormat {HTML, XML, TEXT, PDF, XLS}
    //public enum OutputFormat {pdf, xls}
    
    String reportCode;

    //String fileName;
    
    OutputFormat selectedOutputFormat;
    
    Date startDate;
    Date endDate;
    
    //List<String> periods = new ArrayList<>();
    
    String selectedPeriodName;
    
    Set<Long> departmentIds = new HashSet<>();
    
    String opcion2;
    /*
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }*/
        
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

    /*public List<String> getPeriods() {
        return periods;
    }

    public void setPeriods(List<String> periods) {
        this.periods = periods;
    }*/

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

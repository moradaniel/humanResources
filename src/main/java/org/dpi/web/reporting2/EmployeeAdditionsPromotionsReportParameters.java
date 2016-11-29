package org.dpi.web.reporting2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EmployeeAdditionsPromotionsReportParameters extends AbstractReportParameters implements IReportParameters
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	Long departmentId;
	
    //String reportCode;

    //String fileName;
    
    //OutputFormat outputFormat = OutputFormat.xls;
    
	/*
    Date startDate;
    Date endDate;*/
    

    
	
	public EmployeeAdditionsPromotionsReportParameters() {
	    setOutputFormat(OutputFormat.XLS);
	}
	
	
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;  
    }


    public Long getDepartmentId() {
        return departmentId;
    }

	/*
    public String getReportCode() {
        return reportCode;
    }


    public void setReportCode(String reportCode) {
        this.reportCode = reportCode;
    }
*/


	
	/*
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
    }*/

    @Override
    public String getTemplateFileName() {
        return "nota_creditos_conFechaImpresion.jrxml";
    }




    

}

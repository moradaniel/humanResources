package org.dpi.web.reporting.employeeAdditionsPromotionsReport;

import org.dpi.web.reporting.IReportParameters;
import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class EmployeeAdditionsPromotionsReportParameters extends AbstractReportParameters implements IReportParameters {
    
    Logger log = LoggerFactory.getLogger(this.getClass());

    Long departmentId;


    public EmployeeAdditionsPromotionsReportParameters() {
        setOutputFormat(OutputFormat.XLS);
    }


    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;  
    }


    public Long getDepartmentId() {
        return departmentId;
    }

    @Override
    public String getTemplateFileName() {
        return "nota_creditos_conFechaImpresion.jrxml";
    }

}

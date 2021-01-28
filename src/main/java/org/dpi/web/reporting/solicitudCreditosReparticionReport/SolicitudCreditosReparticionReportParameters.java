package org.dpi.web.reporting.solicitudCreditosReparticionReport;

import org.dpi.web.reporting.IReportParameters;
import org.dpi.web.reporting.ReportOutputFormat.OutputFormat;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class SolicitudCreditosReparticionReportParameters extends AbstractReportParameters implements IReportParameters {
    
    Logger log = LoggerFactory.getLogger(this.getClass());

    Long departmentId;


    public SolicitudCreditosReparticionReportParameters() {
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
        //return "solicitud_de_creditos_para_reparticion.jrxml";
        return "SGP-IYP-REG-02_pedido_de_puntos_DPI_template.xls";
    }

}

package org.dpi.web.reporting.resumenDeSaldosDeCreditosDeReparticionesReport;

import org.dpi.web.reporting.IReportParameters;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class ResumenDeSaldosDeCreditosDeReparticionesReportParameters  extends AbstractReportParameters implements IReportParameters
{
	Logger log = LoggerFactory.getLogger(this.getClass());
    
	
	public ResumenDeSaldosDeCreditosDeReparticionesReportParameters() {

	}

    
    @Override
    public String getTemplateFileName() {
        return "resumenDeSaldosDeCreditosDeReparticiones_template.xls";
    }

}

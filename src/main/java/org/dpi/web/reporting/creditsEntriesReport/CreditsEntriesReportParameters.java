package org.dpi.web.reporting.creditsEntriesReport;

import org.dpi.web.reporting.IReportParameters;
import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class CreditsEntriesReportParameters  extends AbstractReportParameters implements IReportParameters
{
	Logger log = LoggerFactory.getLogger(this.getClass());
    
	
	public CreditsEntriesReportParameters() {

	}

    
    @Override
    public String getTemplateFileName() {
        return "creditsEntriesReport_template.xls";
    }

}

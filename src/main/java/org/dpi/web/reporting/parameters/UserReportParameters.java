package org.dpi.web.reporting.parameters;

import org.dpi.web.reporting.parameters.AbstractReportParameters.OutputFormat;


public class UserReportParameters
{

	OutputFormat outputFormat;
	
	String reportCode;
	

	public OutputFormat getOutputFormat() {
		return outputFormat;
	}

	public void setOutputFormat(OutputFormat outputFormat) {
		this.outputFormat = outputFormat;
	}


	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}


}

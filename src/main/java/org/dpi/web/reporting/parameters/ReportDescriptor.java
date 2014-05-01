package org.dpi.web.reporting.parameters;

public class ReportDescriptor
{
	String reportCode;
	String reportTitle;
	String reportDescription;
	String reportFileName; // not including extension

		
	public ReportDescriptor(String reportCode, String reportTitle, String reportFileName)
	{
		this.reportCode = reportCode;
		this.reportTitle = reportTitle;
		this.reportFileName=reportFileName;
	}

	public String getReportCode() {
		return reportCode;
	}
	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}
	public String getReportDescription() {
		return reportDescription;
	}
	public void setReportDescription(String reportDescription) {
		this.reportDescription = reportDescription;
	}
	public String getReportTitle() {
		return reportTitle;
	}
	public void setReportTitle(String reportTitle) {
		this.reportTitle = reportTitle;
	}
	
	public String getReportFileName() {
		return reportFileName;
	}

	public void setReportFileName(String reportFileName) {
		this.reportFileName = reportFileName;
	}

}

package org.dpi.web.reporting.parameters;


public class EmployeeAdditionsPromotionsReportParameters extends AbstractReportParameters{

	private String templateFileName = "nota_creditos_conFechaImpresion.jrxml";

	public EmployeeAdditionsPromotionsReportParameters()
	{
	}
	
	
	public String getTemplateFileName() {
		return templateFileName;
	}

}
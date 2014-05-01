package org.dpi.web.reporting;

import java.util.Map;

import org.dpi.web.reporting.parameters.EmployeeAdditionsPromotionsReportParameters;

public interface EmployeeAdditionsPromotionsReportReportService {

	public Map<String, Object> getReportData(
			EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters);
	
}
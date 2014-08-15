package org.dpi.web.reporting;

import java.util.Map;

import org.dpi.web.reporting.parameters.AbstractReportParameters;
import org.dpi.web.reporting.parameters.ReportDescriptor;
import org.janux.bus.security.Account;

public interface ReportService
{
	/**
	 * @returns a list of the reports currently available
	 */
	public Map<String,ReportDescriptor> getAvailableReports();

	public AbstractReportParameters getNewReportParameters(ReportDescriptor descriptor);
	
	public CanGenerateReportResult canGenerateReport(String reportCode, Account account, Long departmentId);

	//void runReport(final AbstractReportParameters aReportParameters,ReportFormatter formatter);
	
	//String getReservationViewUrl();
}

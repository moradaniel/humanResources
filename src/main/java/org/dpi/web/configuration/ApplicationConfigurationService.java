package org.dpi.web.configuration;

import org.dpi.web.reporting.ReportService;
import org.dpi.web.reporting.ReportService.Reports;
import org.springframework.context.ApplicationContextAware;


public interface ApplicationConfigurationService extends ApplicationContextAware
{

	
    ReportService getReportService(Reports reportCode);


}

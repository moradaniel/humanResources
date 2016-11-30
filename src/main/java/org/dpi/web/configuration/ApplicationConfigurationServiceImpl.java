package org.dpi.web.configuration;

import org.dpi.web.reporting.ReportService;
import org.dpi.web.reporting.ReportService.Reports;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;



@Service("applicationConfigurationService")
public class ApplicationConfigurationServiceImpl implements ApplicationConfigurationService {
	
    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)throws BeansException {
        this.applicationContext = applicationContext;
    }
    

    @Override
    public ReportService getReportService(Reports reportCode){
        
        String serviceReportName = reportCode.name()+"Service";

        ReportService reportService = (ReportService)applicationContext.getBean(serviceReportName);
        return reportService;
    }
}
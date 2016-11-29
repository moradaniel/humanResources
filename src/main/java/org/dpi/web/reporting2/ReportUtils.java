package org.dpi.web.reporting2;

import java.util.HashMap;
import java.util.Map;

import org.dpi.web.reporting.parameters.ReportDescriptor;

public class ReportUtils {


    //TODO move this to database
    // hardcoded list of available reports
    private static final ReportDescriptor descriptors[] =
    {
        new ReportDescriptor(org.dpi.web.reporting2.ReportService.Reports.EmployeeAdditionsPromotionsReport.name(),"Employee Additions Promotions Report","Employee_Additions_Promotions_Report")
        ,new ReportDescriptor(org.dpi.web.reporting2.ReportService.Reports.CreditsEntriesReport.name(),"Credits Entries Report","Credits_Entries_Report")

    };
    
    public static Map<String,ReportDescriptor> getAvailableReports()
    {
        Map<String,ReportDescriptor> reportDescriptors = new HashMap<String,ReportDescriptor>();
        
        for (int i = 0; i < descriptors.length; i++)
        {
            reportDescriptors.put(descriptors[i].getReportCode(), descriptors[i]);
        }
        
        return reportDescriptors;
    }
}

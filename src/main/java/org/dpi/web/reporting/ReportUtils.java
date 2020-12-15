package org.dpi.web.reporting;

import java.util.HashMap;
import java.util.Map;

public class ReportUtils {


    //TODO move this to database
    // hardcoded list of available reports
    private static final ReportDescriptor descriptors[] =
    {
        new ReportDescriptor(ReportService.Reports.EmployeeAdditionsPromotionsReport.name(),"Employee Additions Promotions Report","Employee_Additions_Promotions_Report")
        ,new ReportDescriptor(ReportService.Reports.CreditsEntriesReport.name(),"Credits Entries Report","Credits_Entries_Report")
        ,new ReportDescriptor(ReportService.Reports.ResumenDeSaldosDeCreditosDeReparticionesReport.name(),"Reporte Resumen Saldos de Creditos de Reparticiones","Resumen_Saldos_Creditos_Reparticiones_Report")

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

package org.dpi.web.reporting2;

public class ReportGeneratorFactory {

    public static ReportGenerator getGenerator(ReportParameters reportParameters) {
        
        ReportGenerator reportGenerator = null;
        
        switch (reportParameters.getSelectedOutputFormat()) {
        case PDF:
            reportGenerator = new PdfGenerator();
            break;
        case XLS:
            reportGenerator = new ExcelReportGenerator();
            break;
        default:
            break;
        }
         
        return reportGenerator;
    }
    
}

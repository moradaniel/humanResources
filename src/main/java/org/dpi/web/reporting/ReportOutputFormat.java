package org.dpi.web.reporting;

public class ReportOutputFormat {
    
    public enum OutputFormat {HTML, XML, TEXT, PDF, XLS}
    
    public static final String MEDIA_TYPE_EXCEL = "application/vnd.ms-excel";
    public static final String MEDIA_TYPE_PDF = "application/pdf";
    public static final String EXTENSION_TYPE_EXCEL = "xls";
    public static final String EXTENSION_TYPE_PDF = "pdf";
    
    String code;
    String name;
    String description;
    
    
}

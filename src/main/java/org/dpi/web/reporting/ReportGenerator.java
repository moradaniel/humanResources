package org.dpi.web.reporting;

import java.io.OutputStream;

public interface ReportGenerator {
    
    void generate(ReportParameters parameters, OutputStream outputStream) throws Exception;
    
}

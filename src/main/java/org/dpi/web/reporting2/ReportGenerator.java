package org.dpi.web.reporting2;

import java.io.OutputStream;

public interface ReportGenerator {
    
    void generate(ReportParameters parameters, OutputStream outputStream) throws Exception;
    
}

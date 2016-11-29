package org.dpi.web.reporting2;

import java.io.ByteArrayOutputStream;

import org.dpi.web.reporting.CanGenerateReportResult;
import org.janux.bus.security.Account;

public interface ReportService<T extends IReportParameters> {
    
    public enum Reports{
        EmployeeAdditionsPromotionsReport,
        CreditsEntriesReport
    }
    
    public CanGenerateReportResult canGenerateReport(String reportCode, Account account, Long departmentId);
    
    public ByteArrayOutputStream generate(T parameters/*, OutputStream outputStream*/)
            throws Exception;

}

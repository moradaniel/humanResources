package org.dpi.web.reporting;

import java.io.ByteArrayOutputStream;

import org.janux.bus.security.Account;

public interface ReportService<T extends IReportParameters> {
    
    public enum Reports{
        EmployeeAdditionsPromotionsReport,
        CreditsEntriesReport,
        ResumenDeSaldosDeCreditosDeReparticionesReport
        
    }
    
    public CanGenerateReportResult canGenerateReport(Account account, Long departmentId);
    
    public ByteArrayOutputStream generate(T parameters)
            throws Exception;

}

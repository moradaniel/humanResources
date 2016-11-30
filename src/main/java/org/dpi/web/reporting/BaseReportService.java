package org.dpi.web.reporting;

import java.io.ByteArrayOutputStream;

import org.janux.bus.security.Account;

public abstract class BaseReportService<T extends IReportParameters> implements ReportService<T>{
    
    abstract  public Reports getReportCode();
    
    public CanGenerateReportResult canGenerateReport(Account account, Long departmentId) {
        
        CanGenerateReportResult canGenerateReportResult = new CanGenerateReportResult();
        
        canGenerateReportResult.setHasPermissions( account.hasPermissions(getReportCode().name(), "READ") );

        return canGenerateReportResult;
    }

    @Override
    public abstract ByteArrayOutputStream generate(T parameters) throws Exception;

}

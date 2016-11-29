package org.dpi.web.reporting2;

import java.io.ByteArrayOutputStream;

import org.dpi.web.reporting.CanGenerateReportResult;
import org.janux.bus.security.Account;

public abstract class BaseReportService<T extends IReportParameters> implements ReportService<T>{
    
    
    public CanGenerateReportResult canGenerateReport(String reportCode, Account account, Long departmentId) {
        
        CanGenerateReportResult canGenerateReportResult = new CanGenerateReportResult();
        
        canGenerateReportResult.setHasPermissions( account.hasPermissions(reportCode, "READ") );
        
    
        /*if(reportCode.equals(org.dpi.web.reporting2.ReportService.Reports.EmployeeAdditionsPromotionsReport.name())) {
            if(creditsPeriodService.getCurrentCreditsPeriod().getStatus()!=Status.Active){
                canGenerateReportResult.addReasonCode(ReasonCodes.closedCreditsPeriod.name());
            }else {
                Long creditosDisponiblesSegunSolicitadoPeriodoActual = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(creditsPeriodService.getCurrentCreditsPeriod().getId(),departmentId);
                if(creditosDisponiblesSegunSolicitadoPeriodoActual < 0) {
                    canGenerateReportResult.addReasonCode(ReasonCodes.negativeBalance.name());
                }
            }
        }*/
        
        return canGenerateReportResult;
    }

    @Override
    public abstract ByteArrayOutputStream generate(T parameters) throws Exception;

}

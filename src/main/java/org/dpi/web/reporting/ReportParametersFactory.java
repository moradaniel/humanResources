package org.dpi.web.reporting;

import org.dpi.web.reporting.creditsEntriesReport.CreditsEntriesReportParameters;
import org.dpi.web.reporting.employeeAdditionsPromotionsReport.EmployeeAdditionsPromotionsReportParameters;
import org.dpi.web.reporting.resumenDeSaldosDeCreditosDeReparticionesReport.ResumenDeSaldosDeCreditosDeReparticionesReportParameters;
import org.dpi.web.reporting.solicitudCreditosReparticionReport.SolicitudCreditosReparticionReportParameters;

public class ReportParametersFactory {
    
    public static IReportParameters buildReportParameters(ReportParameters userReportParameters){
       if(userReportParameters == null){
          return null;
       }     
       if(userReportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.EmployeeAdditionsPromotionsReport.name())){
           EmployeeAdditionsPromotionsReportParameters employeeAdditionsPromotionsReportParameters = new EmployeeAdditionsPromotionsReportParameters();
           employeeAdditionsPromotionsReportParameters.setOutputFormat(userReportParameters.getSelectedOutputFormat());
           employeeAdditionsPromotionsReportParameters.setGeneratedByUser(userReportParameters.getGeneratedByUser());
           employeeAdditionsPromotionsReportParameters.setDepartmentId(userReportParameters.getDepartmentIds().iterator().next());
           employeeAdditionsPromotionsReportParameters.addCreditPeriodNames(userReportParameters.getSelectedPeriodName());
           return employeeAdditionsPromotionsReportParameters;
          
       } else if(userReportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.CreditsEntriesReport.name())){
           
           
           CreditsEntriesReportParameters creditsEntriesReportParameters = new CreditsEntriesReportParameters();
           creditsEntriesReportParameters.setOutputFormat(userReportParameters.getSelectedOutputFormat());
           creditsEntriesReportParameters.setGeneratedByUser(userReportParameters.getGeneratedByUser());
           creditsEntriesReportParameters.addCreditPeriodNames(userReportParameters.getSelectedPeriodName());
           return creditsEntriesReportParameters;
          
       } else if(userReportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.ResumenDeSaldosDeCreditosDeReparticionesReport.name())){
           
           
           ResumenDeSaldosDeCreditosDeReparticionesReportParameters resumenDeSaldosDeCreditosDeReparticionesReportParameters = new ResumenDeSaldosDeCreditosDeReparticionesReportParameters();
           resumenDeSaldosDeCreditosDeReparticionesReportParameters.setOutputFormat(userReportParameters.getSelectedOutputFormat());
           resumenDeSaldosDeCreditosDeReparticionesReportParameters.setGeneratedByUser(userReportParameters.getGeneratedByUser());
           resumenDeSaldosDeCreditosDeReparticionesReportParameters.addCreditPeriodNames(userReportParameters.getSelectedPeriodName());
           return resumenDeSaldosDeCreditosDeReparticionesReportParameters;
          
       } else if(userReportParameters.getReportCode().equalsIgnoreCase(ReportService.Reports.SolicitudCreditosReparticionReport.name())){
           
           
           SolicitudCreditosReparticionReportParameters solicitudCreditosReparticionReportParameters = new SolicitudCreditosReparticionReportParameters();
           solicitudCreditosReparticionReportParameters.setOutputFormat(userReportParameters.getSelectedOutputFormat());
           solicitudCreditosReparticionReportParameters.setGeneratedByUser(userReportParameters.getGeneratedByUser());
           solicitudCreditosReparticionReportParameters.setDepartmentId(userReportParameters.getDepartmentIds().iterator().next());
           solicitudCreditosReparticionReportParameters.addCreditPeriodNames(userReportParameters.getSelectedPeriodName());
           return solicitudCreditosReparticionReportParameters;
          
       }
       
       return null;
    }
 }
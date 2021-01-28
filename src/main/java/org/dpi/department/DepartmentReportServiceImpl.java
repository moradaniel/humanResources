package org.dpi.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.security.UserAccessService;
import org.dpi.stats.PeriodSummaryData;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;





public class DepartmentReportServiceImpl implements DepartmentReportService {
    
	Logger log = LoggerFactory.getLogger(this.getClass());
	private ApplicationContext applicationContext;
	
	private UserAccessService userAccessService;
		
	private CreditsPeriodService creditsPeriodService;

    private CreditsManagerService creditsManagerService;
    
    private DepartmentService departmentService;


	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException {
		this.applicationContext = aApplicationContext;
	}

        
    public List<PeriodSummaryData> getCurrentPeriodDepartmentsSummaryData(){
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountName = ((Account) principal).getName();
        Set<DepartmentAdminInfo> departmentsInfoSet = userAccessService.getDepartmentListForAccount(accountName, null);
        List<DepartmentAdminInfo> departmentsInfoList = new ArrayList<DepartmentAdminInfo>(departmentsInfoSet);

        //List<GenericTreeNode<DepartmentResults<String>>> childrenArrayList = node.getChildren();
        
        Collections.sort(departmentsInfoList, new Comparator<DepartmentAdminInfo>()
            {
                public int compare( DepartmentAdminInfo one, DepartmentAdminInfo another){
                    return one.getCode().compareTo(another.getCode());
                }
                
            }
        );
    
       // int i = 0;
            List<PeriodSummaryData> currentPeriodDepartmentsSummaryData = new ArrayList<PeriodSummaryData>();
            for( DepartmentAdminInfo departmentInfo : departmentsInfoList){
        
               
                //TODO cambiar esto. Se debe buscar las reparticiones que no esten desactivadas.
                //No buscar una por una si esta desactivada. Usar un quiery filter de department
                Department department = departmentService.findById(departmentInfo.getId());
                if(department.getValidToPeriod()!=null){
                    continue;
                }
                
                /*
                
                if (department instanceof Department) 
                {
                    model.addAttribute(KEY_DEPARTMENT_LIST, department.getId());
                }*/
                
                //build current year
                
                log.debug("Generating listDepartmentsCreditsSummary, building buildCurrentPeriodSummaryData for department {}",department.getCode()+department.getName());
                PeriodSummaryData currentPeriodSummaryData = buildCurrentPeriodSummaryData(department);
                
                currentPeriodDepartmentsSummaryData.add(currentPeriodSummaryData);

                /*
                i++;
                if(i==13) {
                    break;
                }*/
            }
            return currentPeriodDepartmentsSummaryData;
    }
    
    
    
    
    public PeriodSummaryData buildCurrentPeriodSummaryData(Department department){
        
        //--------------------- current year
        
        CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
        
        PeriodSummaryData currentPeriodSummaryData = new PeriodSummaryData();
        
        currentPeriodSummaryData.setDepartment(department);
        currentPeriodSummaryData.setMinisterioDeReparticion(departmentService.getMinisterioOSecretariaDeEstado(department));
        
        currentPeriodSummaryData.setYear(currentCreditsPeriod.getName());
        
        
        Long creditosDisponiblesInicioPeriodo = this.creditsManagerService.getCreditosDisponiblesAlInicioPeriodo(currentCreditsPeriod.getId(),department.getId());
        currentPeriodSummaryData.setCreditosDisponiblesInicioPeriodo(creditosDisponiblesInicioPeriodo);

    
        Long creditosAcreditadosPorBajaDurantePeriodoActual = this.creditsManagerService.getCreditosPorBajasDeReparticion(currentCreditsPeriod.getId(), department.getId());

        currentPeriodSummaryData.setCreditosAcreditadosPorBajaDurantePeriodo(creditosAcreditadosPorBajaDurantePeriodoActual);
        
        
        Long currentPeriodRetainedCredits = this.creditsManagerService.getRetainedCreditsByDepartment(currentCreditsPeriod.getId(), department.getId());

        currentPeriodSummaryData.setRetainedCredits(currentPeriodRetainedCredits);
        
        
        Long currentPeriodTotalAvailableCredits = this.creditsManagerService.getTotalCreditosDisponiblesAlInicioPeriodo(currentCreditsPeriod.getId(), department.getId());
        

        currentPeriodSummaryData.setTotalAvailableCredits(currentPeriodTotalAvailableCredits);
        
        
        
        Long creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo = this.creditsManagerService.getCreditosPorIngresosOAscensosSolicitados(currentCreditsPeriod.getId(), department.getId());
        
        currentPeriodSummaryData.setCreditosConsumidosPorIngresosOAscensosSolicitadosPeriodo(creditosConsumidosPorIngresosOAscensosSolicitadosPeriodo);
        

        Long creditosPorIngresosOAscensosOtorgadosPeriodo = this.creditsManagerService.getCreditosPorIngresosOAscensosOtorgados(currentCreditsPeriod.getId(), department.getId());
        
        currentPeriodSummaryData.setCreditosPorIngresosOAscensosOtorgadosPeriodo(creditosPorIngresosOAscensosOtorgadosPeriodo);

                

        Long creditosDisponiblesSegunSolicitadoPeriodo = this.creditsManagerService.getCreditosDisponiblesSegunSolicitado(currentCreditsPeriod.getId(),department.getId());
        currentPeriodSummaryData.setCreditosDisponiblesSegunSolicitadoPeriodo(creditosDisponiblesSegunSolicitadoPeriodo);
        
        Long creditosDisponiblesSegunOtorgadoPeriodo = this.creditsManagerService.getCreditosDisponiblesSegunOtorgado(currentCreditsPeriod.getId(),department.getId());
        currentPeriodSummaryData.setCreditosDisponiblesSegunOtorgadoPeriodo(creditosDisponiblesSegunOtorgadoPeriodo);
        
        Long totalCreditosReparticionAjustes_Debito_Periodo = this.creditsManagerService.getCreditosReparticionAjustesDebitoPeriodo(currentCreditsPeriod.getId(), department.getId());
        currentPeriodSummaryData.setTotalCreditosReparticionAjustes_Debito_Periodo(totalCreditosReparticionAjustes_Debito_Periodo);
        
        Long totalCreditosReparticionAjustes_Credito_Periodo = this.creditsManagerService.getCreditosReparticionAjustesCreditoPeriodo(currentCreditsPeriod.getId(), department.getId());
        currentPeriodSummaryData.setTotalCreditosReparticionAjustes_Credito_Periodo(totalCreditosReparticionAjustes_Credito_Periodo);

        Long totalCreditosReparticion_ReasignadosDeRetencion_Periodo = this.creditsManagerService.getCreditosReparticion_ReasignadosDeRetencion_Periodo(currentCreditsPeriod.getId(), department.getId());
        currentPeriodSummaryData.setTotalCreditosReparticion_ReasignadosDeRetencion_Periodo(totalCreditosReparticion_ReasignadosDeRetencion_Periodo);

        
        Long totalCreditosReparticion_ReubicacionReparticion_Periodo = this.creditsManagerService.getCreditosReparticion_ReubicacionDeReparticion_Periodo(currentCreditsPeriod.getId(), department.getId());
        currentPeriodSummaryData.setTotalCreditosReparticion_Reubicacion_Periodo(totalCreditosReparticion_ReubicacionReparticion_Periodo);

        
        return currentPeriodSummaryData;
                    
    }

    
    public UserAccessService getUserAccessService() {
        return userAccessService;
    }

    public void setUserAccessService(UserAccessService userAccessService) {
        this.userAccessService = userAccessService;
    }
    
    public CreditsPeriodService getCreditsPeriodService() {
        return creditsPeriodService;
    }

    public void setCreditsPeriodService(CreditsPeriodService creditsPeriodService) {
        this.creditsPeriodService = creditsPeriodService;
    }
    
    public CreditsManagerService getCreditsManagerService() {
        return creditsManagerService;
    }

    public void setCreditsManagerService(
            CreditsManagerService creditsManagerService) {
        this.creditsManagerService = creditsManagerService;
    }
    
    public DepartmentService getDepartmentService() {
    return departmentService;
    }
    
    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

}

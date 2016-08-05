package org.dpi.department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.dpi.common.ResponseMap;
import org.dpi.creditsEntry.CambiosMultiplesEstadoMovimientosForm;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryServiceImpl;
import org.dpi.creditsEntry.CreditsEntryVO;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.EmploymentCreditsEntriesService;
import org.dpi.employment.EmploymentCreditsEntriesServiceImpl;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.person.PersonService;
import org.dpi.security.AccountSettings;
import org.dpi.security.UserAccessService;
import org.dpi.security.UserSettingsFactory;
import org.dpi.security.UserSettingsFactoryImpl;
import org.dpi.stats.HistoricPeriodSummaryData;
import org.dpi.stats.PeriodSummaryData;
import org.dpi.util.tree.GenericTreeNode;
import org.dpi.web.reporting.CanGenerateReportResult;
import org.dpi.web.reporting.ReportService;
import org.dpi.web.reporting.ReportServiceImpl.ManagementReports;
import org.janux.bus.persistence.EntityNotFoundException;
import org.janux.bus.security.Account;
import org.janux.bus.security.AccountImpl;
import org.janux.bus.security.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.travel.AjaxUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



@Controller
public class DepartmentController {


	static Logger log = LoggerFactory.getLogger(DepartmentController.class);

	/**  name of the http Request parameter which we use to pass an department's code: 'departmentId' */
	public static final String PARAM_DEPARTMENT_ID = "departmentId";

	public static final String PARAM_CURR_PATH = "currPath";

	public static final String KEY_CURRENT_DEPARTMENT_ID = "currentDepartmentId";


	/** 
	 * key that we use when placing an Department instance in a model: 'department'
	 * TODO: make private; have clients use session getters/putters
	 */
	public static final String KEY_DEPARTMENT = "currentDepartment";

	/** 
	 * The key by which we store in the Session the list of Departments that the
	 * Principal is authorized to view: "myDepartments"
	 */
	private static final String KEY_DEPARTMENT_LIST = "myDepartments";

	/** name of a generic view used to display messages: 'MessagePage' */
	public static final String VIEW_MESSAGE = "MessagePage";

	/** key that we use when placing a Message string in a model: 'message' */
	public static final String KEY_MESSAGE = "message";

	private DepartmentService departmentService;

	@Resource(name = "accountServiceGeneric")
	private AccountService accountService;

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "employmentCreditsEntriesService")
	private EmploymentCreditsEntriesService employmentCreditsEntriesService;

	@Resource(name = "personService")
	private PersonService personService;


	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;

	@Resource(name = "creditsEntryService")
	private CreditsEntryService creditsEntryService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	@Resource(name = "accountSettingsFactory")
	private UserSettingsFactory settingsFactory;	
	
	@Resource(name = "reportService")
	private ReportService reportService;
	
	@Resource(name = "userAccessService")
	private UserAccessService userAccessService;
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper objectMapper; 


	@Inject
	public DepartmentController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}


	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}

	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}

	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

	public static Department getCurrentDepartment(HttpServletRequest request)
	{
		return (Department) request.getAttribute(DepartmentController.KEY_DEPARTMENT);
	}

	@RequestMapping(value = "/departments/department/showCredits", method = RequestMethod.GET)
	public String showCredits(HttpServletRequest request, HttpServletResponse response, Model model) {

		// get the current department in the session
		final Department department = DepartmentController.getCurrentDepartment(request);

		if (department != null){

			if (department instanceof Department) 
			{
				model.addAttribute(PARAM_DEPARTMENT_ID, department.getId());
			}
			
			//build current year
			PeriodSummaryData currentPeriodSummaryData = buildCurrentPeriodSummaryData(department);
					
			model.addAttribute("currentPeriodSummaryData", currentPeriodSummaryData);
			
			
			//build historic periods

			List<HistoricPeriodSummaryData> historicPeriodsSummaryData = buildHistoricPeriodsSummaryData(department);
			
			model.addAttribute("historicPeriodsSummaryData", historicPeriodsSummaryData);
			


		}
		return "departments/show";
	}


	@RequestMapping(value = "/departments/department/showEmployments", method = RequestMethod.GET)
	public String showEmployments(	HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		// get the current department in the session
		final Department department = DepartmentController.getCurrentDepartment(request);

		if (department != null){
			
			/*
			EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
			empleoQueryFilter.setdepartmentid(String.valueOf(department.getId()));
			empleoQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);
			
			List<Employment> activeEmployments = employmentService.find(empleoQueryFilter);
			
			//canAccountPromotePerson
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currenUser = (Account)accountObj;
			List<EmploymentVO> employmentsVO = employmentCreditsEntriesService.buildEmploymentsVO(activeEmployments,department.getId(),currenUser);
			
			model.addAttribute("activeEmployments", employmentsVO);*/
			
			
			Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("canAccountProposeNewEmployment", EmploymentCreditsEntriesServiceImpl.canAccountProposeNewEmployment(currentUser));
			


		}
		return "departments/edit";
	}

	@RequestMapping(value = "/departments/{departmentId}", method = RequestMethod.POST)
	public String edit(DepartmentImpl department, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (result.hasErrors()) {
			return "departments/edit";
		}
		departmentService.saveOrUpdate(department);
		return (AjaxUtils.isAjaxRequest(requestedWith)) ? "departments/show" : "redirect:/departments/" + department.getId();
	}

	@RequestMapping(value = "/departments/department/showCreditEntries/{creditsPeriodName}", method = RequestMethod.GET)
	public String showCreditEntries(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long creditsPeriodName,
			Model model) {

		// get the current department in the session
		final Department department = DepartmentController.getCurrentDepartment(request);
		
		
		if (department != null){
			
			//by default use current period
			long currentPeriodName = Long.parseLong(creditsPeriodService.getCurrentCreditsPeriod().getName());
			
			if(creditsPeriodName!=null){
					if(creditsPeriodName<2012){
						creditsPeriodName=currentPeriodName;
					}else
						if(creditsPeriodName>currentPeriodName){
							creditsPeriodName=currentPeriodName;
						}
			}
			else{//default to current period
				creditsPeriodName=currentPeriodName;//TODO get year from current date or better the ACTIVE period
			}
			


			EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
			empleoQueryFilter.setDepartmentId(department.getId());

			CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
			creditsEntryQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);

			CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
			creditsPeriodQueryFilter.setName(String.valueOf(creditsPeriodName));
			
			List<CreditsPeriod> creditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
			CreditsPeriod creditsPeriod = creditsPeriods.get(0);
			creditsEntryQueryFilter.addCreditsPeriodIds(creditsPeriod.getId());
			
			List<CreditsEntry> creditsEntryOfDepartment = creditsEntryService.find(creditsEntryQueryFilter);
			
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currentUser = (Account)accountObj;
					
			List<CreditsEntryVO> creditsEntryVODepartment = creditsEntryService.buildCreditsEntryVO(creditsEntryOfDepartment,currentUser);
					
			model.addAttribute("creditsEntries", creditsEntryVODepartment);
			
			//------------------ Should we build the form for editing status? -----------------------------
			
			model.addAttribute("canAccountChangeCreditsEntryStatusOfPeriod",CreditsEntryServiceImpl.canChangeCreditsEntryStatus(currentUser, creditsPeriod));
			
			CambiosMultiplesEstadoMovimientosForm cambiosMultiplesEstadoMovimientosForm =  new CambiosMultiplesEstadoMovimientosForm();
			for(CreditsEntryVO creditsEntryVO :creditsEntryVODepartment){
				cambiosMultiplesEstadoMovimientosForm.getMovimientos().add(creditsEntryVO.getCreditsEntry());
			}
			
			model.addAttribute("grantedStatuses", GrantedStatus.values());
			model.addAttribute("cambiosMultiplesEstadoMovimientosForm", cambiosMultiplesEstadoMovimientosForm);
			
			
			
			//------------------ Should we show the report generation button? -----------------------------			
			
			CanGenerateReportResult canGenerateReportResult = reportService.canGenerateReport(ManagementReports.Employee_Additions_Promotions_Report.name(), currentUser, department.getId());
			
			
			boolean showReportGenerationButton = false;
			boolean canGenerateReport = false;
						
			boolean hasPermissionToGenerateReport = canGenerateReportResult.hasPermissions();
			model.addAttribute("hasPermissionToGenerateReport", hasPermissionToGenerateReport);
			
			canGenerateReport = canGenerateReportResult.canGenerateReport();
			
			if(canGenerateReport){
				showReportGenerationButton=true;
			}else {
				showReportGenerationButton=false;
				model.addAttribute("notAllowedReasons", canGenerateReportResult.getReasonCodes());
			}
			
			
			model.addAttribute("showReportGenerationButton", showReportGenerationButton);
			
						
		}
		return "departments/creditsentries";
	}
	
	

	@RequestMapping(value = "/departments/department/creditsentries/{creditsEntryId}/delete", method = RequestMethod.GET)
	public String deleteCreditsEntry(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long creditsEntryId,
			Model model) {

		// get the current department in the session
		final Department department = DepartmentController.getCurrentDepartment(request);

		if (department != null){
			CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
			creditsEntryQueryFilter.addCreditsEntryIds(creditsEntryId);
			EmploymentQueryFilter empleoFilter = new EmploymentQueryFilter();
			empleoFilter.setDepartmentId(department.getId());
			creditsEntryQueryFilter.setEmploymentQueryFilter(empleoFilter);

			CreditsEntry entry = creditsEntryService.find(creditsEntryQueryFilter).get(0);

			if(entry!=null){
				creditsEntryService.delete(entry);	
			}
		}

		return "redirect:/departments/department/showCreditEntries/"+creditsPeriodService.getCurrentCreditsPeriod().getName();

	}


	public CreditsEntryService getCreditsEntryService() {
		return creditsEntryService;
	}


	public void setCreditsEntryService(
			CreditsEntryService creditsEntryService) {
		this.creditsEntryService = creditsEntryService;
	}



	/**
	 * Reloads the list of departments that a user is allowed to edit via the AccessService.
	 * If the departments list for a user changes, the changes will be seen immediately
	 * @param request
	 * @param accessService
	 */
	public static void refreshRequestDepartmentList(HttpServletRequest request,UserAccessService accessService)
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String accountName = ((Account) principal).getName();
		Set<DepartmentAdminInfo> departments = accessService.getDepartmentListForAccount(accountName, null);
		
		

		request.setAttribute(DepartmentController.KEY_DEPARTMENT_LIST,departments);
	}

	/** 
	 * Loads the department in the Request, and checks that the current account has the proper
	 * permissions to view that department
	 */
	public static void refreshRequestDepartment(HttpServletRequest request,DepartmentService departmentService,
			UserAccessService accessService,boolean checkAccess)
	{
		boolean createSessionIfNeeded = true;
		HttpSession session = request.getSession(createSessionIfNeeded);
		Long departmentId = (Long) session.getAttribute(DepartmentController.KEY_CURRENT_DEPARTMENT_ID);

		AccountSettings settings = UserSettingsFactoryImpl.getSettingsForPrincipal();

		checkAccess = checkAccess || (departmentId == null); // first time in

		if(departmentId == null){
			if(StringUtils.hasText(settings.getLastDepartment())){
				departmentId = Long.parseLong(settings.getLastDepartment());	
			}
		}

		Department department = null;

		if (departmentId == null)
		{
			log.warn("There is currently no Department active in the session");
		}
		else 
		{
			try
			{ 
				department = departmentService.findById(departmentId);
			}
			catch (EntityNotFoundException e)
			{
				log.error(e.toString());
				department = null;
			}

			if (checkAccess)
			{
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Account account = (Account) principal;
				boolean hasAccess = accessService.hasAccessToDepartment(account, departmentId); 

				if (!hasAccess)
				{
					department = null;
					settings.setLastDepartment(null);
					log.warn("Account: '" + account.getName() + "' attempting to access page without authorization");
				}
			}
		}

		request.setAttribute(DepartmentController.KEY_DEPARTMENT,department);
	}


	@RequestMapping(value = "/departments/select", method = RequestMethod.GET)
	public ModelAndView selectDepartment(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("departmentId") Long departmentId,
			@RequestParam("currPath") String currPath)
					throws Exception 
					{
		ModelAndView returnView = null;

		if (this.doSelectDepartment(request,departmentId))
		{
			if (log.isDebugEnabled()) {log.debug("Doing redirect to: '" + currPath + "'");}
			returnView = new ModelAndView(new RedirectView("/home",true));

		}	
		else
		{
			returnView = new ModelAndView(VIEW_MESSAGE, KEY_MESSAGE, "Unable to load Department with code '" + departmentId + "'");
		}

		return returnView;
	}

	/** 
	 * deep loads an Department based on a code passed in the query string parameter denoted by
	 * DepartmentInfoController.PARAM_DEPARTMENT_ID and forwards to a summary display page 
	 * @returns whether selection was successful (empty code allowed; for non-empty code, department must
	 * be able to be loaded.) 
	 */
	public boolean doSelectDepartment(HttpServletRequest request, Long departmentId) 
	{
		boolean ok = true;
		Department department = null;

		if (departmentId!=null)
		{
		    department = departmentService.findById(departmentId);

			if (department != null)
			{

				Account januxUserDetailsAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				Account account = accountService.loadAccountByName(januxUserDetailsAccount.getName());

				AccountSettings accountSettings = (AccountSettings)settingsFactory.getSettingsForAccount(account);

				accountSettings.setLastDepartment(String.valueOf(departmentId));

				accountService.saveOrUpdateAccount((AccountImpl)account);
				request.getSession().setAttribute(KEY_CURRENT_DEPARTMENT_ID, department.getId());
				request.setAttribute(KEY_DEPARTMENT, department);
				if (log.isDebugEnabled()) {log.debug("Completed doing doSelectDepartment with ok status");}
			}
			else {
				ok = false;
			}
		}

		return ok;
	}
	

	PeriodSummaryData buildCurrentPeriodSummaryData(Department department){
		
		//--------------------- current year
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		PeriodSummaryData currentPeriodSummaryData = new PeriodSummaryData();
		
		currentPeriodSummaryData.setDepartment(department);
		
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

        
		return currentPeriodSummaryData;
					
	}
	
	List<HistoricPeriodSummaryData> buildHistoricPeriodsSummaryData(Department department){
		
		List<HistoricPeriodSummaryData> historicPeriodsSummaryData = new ArrayList<HistoricPeriodSummaryData>();
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		
		CreditsPeriod previousCreditsPeriod = currentCreditsPeriod.getPreviousCreditsPeriod();
		
		while(previousCreditsPeriod!=null) {
			HistoricPeriodSummaryData historicPeriodSummaryData = new HistoricPeriodSummaryData();
			historicPeriodSummaryData.setYear(previousCreditsPeriod.getName());
			
			Long creditosAcreditadosPorCargaInicial = this.creditsManagerService.getCreditosPorCargaInicialDeReparticion(previousCreditsPeriod.getId(),department.getId());
			historicPeriodSummaryData.setCreditosAcreditadosPorCargaInicial(creditosAcreditadosPorCargaInicial);
			
			
			Long creditosDisponiblesInicioPeriodo = this.creditsManagerService.getCreditosDisponiblesAlInicioPeriodo(previousCreditsPeriod.getId(),department.getId());
			historicPeriodSummaryData.setCreditosDisponiblesInicioPeriodo(creditosDisponiblesInicioPeriodo);
						
				
			Long creditosAcreditadosPorBajas = this.creditsManagerService.getCreditosPorBajasDeReparticion(previousCreditsPeriod.getId(),department.getId());
			historicPeriodSummaryData.setCreditosAcreditadosPorBajas(creditosAcreditadosPorBajas);
				
	        Long creditosRetenidosPorBajas = this.creditsManagerService.getRetainedCreditsByDepartment(previousCreditsPeriod.getId(),department.getId());
	        historicPeriodSummaryData.setCreditosRetenidosPorBajas(creditosRetenidosPorBajas);
	            
			Long creditosConsumidosPorIngresosOAscensosOtorgados = this.creditsManagerService.getCreditosPorIngresosOAscensosOtorgados(previousCreditsPeriod.getId(), department.getId());
			
			historicPeriodSummaryData.setCreditosConsumidosPorIngresosOAscensosOtorgados(creditosConsumidosPorIngresosOAscensosOtorgados);
			
			
	        Long totalCreditosReparticionAjustes_Debito_Periodo = this.creditsManagerService.getCreditosReparticionAjustesDebitoPeriodo(previousCreditsPeriod.getId(), department.getId());
	            
	        historicPeriodSummaryData.setTotalCreditosReparticionAjustes_Debito(totalCreditosReparticionAjustes_Debito_Periodo);
	        
	        Long totalCreditosReparticionAjustes_Credito_Periodo = this.creditsManagerService.getCreditosReparticionAjustesCreditoPeriodo(previousCreditsPeriod.getId(), department.getId());
               
	        historicPeriodSummaryData.setTotalCreditosReparticionAjustes_Credito(totalCreditosReparticionAjustes_Credito_Periodo);
	            
			
	        Long totalCreditosReparticion_ReasignadosDeRetencion_Periodo = this.creditsManagerService.getCreditosReparticion_ReasignadosDeRetencion_Periodo(previousCreditsPeriod.getId(), department.getId());
               
	        historicPeriodSummaryData.setTotalCreditosReparticion_ReasignadosDeRetencion(totalCreditosReparticion_ReasignadosDeRetencion_Periodo);
	            
	        
	        
			Long saldoCreditosAlFinalPeriodo = creditosAcreditadosPorCargaInicial
			                                   +creditosDisponiblesInicioPeriodo
			                                   +creditosAcreditadosPorBajas
			                                   -creditosRetenidosPorBajas
			                                   -creditosConsumidosPorIngresosOAscensosOtorgados
			                                   -totalCreditosReparticionAjustes_Debito_Periodo
			                                   +totalCreditosReparticionAjustes_Credito_Periodo
			                                   +totalCreditosReparticion_ReasignadosDeRetencion_Periodo;
			
			historicPeriodSummaryData.setSaldoCreditosAlFinalPeriodo(saldoCreditosAlFinalPeriodo);
			
			
			historicPeriodsSummaryData.add(historicPeriodSummaryData);
			
			
			CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
			creditsPeriodQueryFilter.setName(previousCreditsPeriod.getName());
							
			List<CreditsPeriod> creditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
			if(creditsPeriods!=null) {
				previousCreditsPeriod = creditsPeriods.get(0);
			}
			
			previousCreditsPeriod = previousCreditsPeriod.getPreviousCreditsPeriod();
			
		}
		
		return historicPeriodsSummaryData;

	}
	
	   @RequestMapping(value = "/departments/department/hierarchicalAccumulatedCredits", method = RequestMethod.GET)
	    public String showHierarchicalAccumulatedCredits(HttpServletRequest request, HttpServletResponse response, Model model) {

	        // get the current department in the session
	        final Department department = DepartmentController.getCurrentDepartment(request);

	        if (department != null){

	            if (department instanceof Department) 
	            {
	                model.addAttribute(PARAM_DEPARTMENT_ID, department.getId());
	            }
	            
	            List<String> departmentsList = buildHierarchicalAccumulatedCredits(department);

	            model.addAttribute("departmentsList", departmentsList);
	            


	        }
	        return "departments/showHierarchicalAccumulatedCredits";
	    }

	   List<String> buildHierarchicalAccumulatedCredits(Department department){

           
           
           if(!departmentService.canGenerateRetainedCreditsTree(department)) {
               //return empty list
               return new ArrayList<String>();
           }
           
                     
	       
	       GenericTreeNode<Department> currentNode = departmentService.getSubTree(department.getId());
	       
       
     	   
	       GenericTreeNode<DepartmentResults<String>> departmentsTreeWithResults = new GenericTreeNode<DepartmentResults<String>>();

           
	       int indent = 0;

	       CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
	       creditsPeriodQueryFilter.setName("2014");
	       CreditsPeriod creditsPeriod = creditsPeriodService.find(creditsPeriodQueryFilter).get(0);

           //-------------------------------------
           
	      /* GenericTreeNode<DepartmentSummary> resultNodes = nonRecursivePostOrder(currentNode, creditsPeriod);
	       List<DepartmentSummary> resultsDepartmentSummaryList = new ArrayList<DepartmentSummary>();
	       treeDepartmentSummaryToList(resultNodes, resultsDepartmentSummaryList);
	       */
           //-------------------------------------
           
	       
	       printTree(currentNode,indent,departmentsTreeWithResults, creditsPeriod);
	       
	       List<String> resultsList = new ArrayList<String>();
	          
	       treeToList(departmentsTreeWithResults, resultsList);
	       
	       return resultsList;
	       
	   }
	   
      private void treeToList(GenericTreeNode<DepartmentResults<String>> node, List<String> resultsList) {

          resultsList.add(node.getData().getResults());
          
          List<GenericTreeNode<DepartmentResults<String>>> childrenArrayList = node.getChildren();
          
          Collections.sort(childrenArrayList, new Comparator<GenericTreeNode<DepartmentResults<String>>>()
          {
              public int compare( GenericTreeNode<DepartmentResults<String>> one, GenericTreeNode<DepartmentResults<String>> another){
                  return one.getData().getDepartment().getCode().compareTo(another.getData().getDepartment().getCode());
              }
              
          }
          );
          
          for (GenericTreeNode<DepartmentResults<String>>  childNode :childrenArrayList) {
              treeToList(childNode, resultsList);
          }
          
      }
       
	   private Long printTree(GenericTreeNode<Department> currentNode, int indent, GenericTreeNode<DepartmentResults<String>> currentNodeDepartmentTreeWithResults, CreditsPeriod creditsPeriod) {

	       String prefix = "";
	       for(int i = 0;i<=indent;i++) {
	           prefix = prefix + "--";
	       }

	       //CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
       
	       Long retainedCreditsForCurrentDepartment = this.creditsManagerService.getRetainedCreditsByDepartment(creditsPeriod.getId(), currentNode.getData().getId());
	       
	       Long accumulatedRetainedCreditsForCurrentDepartmentFromChildren = 0l;

	       List<GenericTreeNode<Department>> childrenArrayList = currentNode.getChildren();
	       
	       /*We do not apply 30% for departments like
	        * 1000000001 - GOBERNACION - Retenidos:0 - Creditos retenidos acumulados en subordinados: 0
              1000000002 - ASESORIA LETRADA DE GOBIERNO - Retenidos:889 - Creditos retenidos acumulados en subordinados: 0
              1000000003 - ESCRIBANIA MAYOR DE GOBIERNO - Retenidos:259 - Creditos retenidos acumulados en subordinados: 0
              
              889 and 259 are added to poder ejecutivo
	        * */
	       long retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio = 0;
	       
    
	       for(GenericTreeNode<Department> child:childrenArrayList) {
	           GenericTreeNode<DepartmentResults<String>> childNodeDepartmentWithResults = new GenericTreeNode<DepartmentResults<String>>();
	           currentNodeDepartmentTreeWithResults.addChild(childNodeDepartmentWithResults);
	           Long childRetainedCredits = printTree(child,indent+1,childNodeDepartmentWithResults,creditsPeriod);
	           if(departmentService.isPoderEjecutivoChildButNotMinisterio( child.getData())) {
	               retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio += childRetainedCredits;
	             //we do not accumulate retained credits for directChildrenOfPoderEjecutivoThatAreNotMinisterio
	               continue;
	           }
	           accumulatedRetainedCreditsForCurrentDepartmentFromChildren = accumulatedRetainedCreditsForCurrentDepartmentFromChildren + childRetainedCredits;
	       }
	       
	       Long totalRetainedCreditsForCurrentNode = accumulatedRetainedCreditsForCurrentDepartmentFromChildren + retainedCreditsForCurrentDepartment;

	       String line = prefix+currentNode.getData().getCode()+" - "+currentNode.getData().getName();
	       
	       if(departmentService.isMinisterio( currentNode.getData())) {//is ministerio or secretaria, parent is root
	           //get retention for its directly dependent subdepartments
	           //accumulatedRetainedCreditsForCurrentDepartmentFromChildren = accumulatedRetainedCreditsForCurrentDepartmentFromChildren + retainedCreditsForCurrentDepartment;
	           
	           long availableToDistributeAmongDependentDepartments = Math.round( 0.7*totalRetainedCreditsForCurrentNode );
	           long availableForRootNode = totalRetainedCreditsForCurrentNode - availableToDistributeAmongDependentDepartments;
	           line = line +" - Retenidos:" + retainedCreditsForCurrentDepartment + " - Disponibles para repartir en reparticiones dependientes: 70% de "+totalRetainedCreditsForCurrentNode + " = "+availableToDistributeAmongDependentDepartments +". Para Poder Ejecutivo: "+availableForRootNode;
	       }else if(departmentService.isPoderEjecutivo(currentNode.getData())) {
	           long percentageRetainedForAggregatedMinisterios = Math.round( 0.3*totalRetainedCreditsForCurrentNode );
	           long availableCreditsForRoot =  percentageRetainedForAggregatedMinisterios + retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio;
	           line = line +" - Disponibles: 30% de " + totalRetainedCreditsForCurrentNode +" = "+percentageRetainedForAggregatedMinisterios+" + retenido de reparticiones directas del poder ejecutivo "+retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio+"  = "+availableCreditsForRoot;
	       }else{ // other departments
	           line = line +" - Retenidos:" + retainedCreditsForCurrentDepartment + " - Creditos retenidos acumulados en subordinados: "+ accumulatedRetainedCreditsForCurrentDepartmentFromChildren ;    
	       }
	       
	       DepartmentResults<String> currentNodeDepartmentResults = new DepartmentResults<String>();
	       currentNodeDepartmentResults.setDepartment(currentNode.getData());
	       currentNodeDepartmentResults.setResults(line);
	       
	       currentNodeDepartmentTreeWithResults.setData(currentNodeDepartmentResults);

	       return totalRetainedCreditsForCurrentNode;

	   }

	   
       @RequestMapping(value = "/departments/listDepartmentsCreditsSummary", method = RequestMethod.GET)
       public String listDepartmentsCreditsSummary(HttpServletRequest request, HttpServletResponse response, Model model) {

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
                   
           
           List<PeriodSummaryData> currentPeriodDepartmentsSummaryData = new ArrayList<PeriodSummaryData>();
           for( DepartmentAdminInfo departmentInfo : departmentsInfoList){

               Department department = departmentService.findById(departmentInfo.getId());
               
               /*
               
               if (department instanceof Department) 
               {
                   model.addAttribute(KEY_DEPARTMENT_LIST, department.getId());
               }*/
               
               //build current year
               
               log.debug("Generating listDepartmentsCreditsSummary, building buildCurrentPeriodSummaryData for department {}",department.getCode()+department.getName());
               PeriodSummaryData currentPeriodSummaryData = buildCurrentPeriodSummaryData(department);
               
               currentPeriodDepartmentsSummaryData.add(currentPeriodSummaryData);
               
               model.addAttribute("currentPeriodDepartmentsSummaryData", currentPeriodDepartmentsSummaryData);
               


           }
           
           /*
           // get the current department in the session
           final Department department = DepartmentController.getCurrentDepartment(request);

           if (department != null){

               if (department instanceof Department) 
               {
                   model.addAttribute(PARAM_DEPARTMENT_ID, department.getId());
               }
               
               List<String> departmentsList = buildHierarchicalAccumulatedCredits(department);

               model.addAttribute("departmentsList", departmentsList);

           }*/
           
           return "departments/listDepartmentsCreditsSummary";
       }
       
       
       
       public GenericTreeNode<DepartmentSummary> nonRecursivePostOrder(GenericTreeNode<Department> root, CreditsPeriod creditsPeriod){
           Stack<GenericTreeNode<Department>> stackOfDepartments = new Stack<>();
           Stack<GenericTreeNode<Department>> nodesWithChildrenStack = new Stack<>();
           
          // Stack<GenericTreeNode<DepartmentSummary>> stackOfDepartmentsSummary = new Stack<>();
           
           HashMap<Long ,GenericTreeNode<DepartmentSummary>> departmentSummaryMap = new HashMap<>();

           DepartmentSummary rootDepartmentSummary = new DepartmentSummary(root.getData());
           GenericTreeNode<DepartmentSummary> rootNodeSummary = new GenericTreeNode<DepartmentSummary>(rootDepartmentSummary);
           departmentSummaryMap.put(root.getData().getId(),rootNodeSummary);
           
           if(root.hasChildren()) {
               stackOfDepartments.push(root);
           } 

           //build a stack with all nodes that have children
           while(true) {
               if(stackOfDepartments.isEmpty()) {
                   break;
               }
               GenericTreeNode<Department> currentNode = stackOfDepartments.pop();
               GenericTreeNode<DepartmentSummary> currentNodeSummary = departmentSummaryMap.get(currentNode.getData().getId());
                              
               
               if(currentNode.hasChildren()) {
                  nodesWithChildrenStack.push(currentNode);
                  for(GenericTreeNode<Department> child : currentNode.getChildren()) {
                     stackOfDepartments.push(child);
                     
                     DepartmentSummary childDepartmentSummary = new DepartmentSummary(child.getData());
                     GenericTreeNode<DepartmentSummary> childNodeSummary = new GenericTreeNode<DepartmentSummary>(childDepartmentSummary);
                     departmentSummaryMap.put(child.getData().getId(),childNodeSummary);
                     
                     currentNodeSummary.addChild(childNodeSummary);
                  }
               }
           }
           
           
           long retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio = 0l;
           
           //aggregate child sum nodes from bottom to top        
           while(true) {
               if(nodesWithChildrenStack.isEmpty()) {
                   break;
               }
               
               GenericTreeNode<Department> currentNode = nodesWithChildrenStack.pop();
               

               
               Long retainedCreditsForCurrentDepartment = this.creditsManagerService.getRetainedCreditsByDepartment(creditsPeriod.getId(), currentNode.getData().getId());
               
               Long accumulatedRetainedCreditsForCurrentDepartmentFromChildren = 0l;
               
               
               
               for(GenericTreeNode<Department> child:currentNode.getChildren()) {
                  // currentNode.getData().setWeight(currentNode.getData().getWeight() + child.getData().getWeight());
                   
                   GenericTreeNode<DepartmentSummary> childNodeSummary = departmentSummaryMap.get(child.getData().getId());
                   DepartmentSummary childDepartmentSummary = childNodeSummary.getData();
                   
                   Long childRetainedCredits = this.creditsManagerService.getRetainedCreditsByDepartment(creditsPeriod.getId(), child.getData().getId());
                   if(!child.hasChildren()) {

                       
                       //childRetainedCredits = this.creditsManagerService.getRetainedCreditsByDepartment(creditsPeriod.getId(), child.getData().getId());
                       
                       
                       childDepartmentSummary.setRetainedCreditsForCurrentDepartment(childRetainedCredits);
                       //childDepartmentSummary.setTotalRetainedCreditsForCurrentNode(totalRetainedCreditsForCurrentNode);
                       
                       childDepartmentSummary.setDescription("Retenidos:" + childRetainedCredits);
                       
                       if(departmentService.isPoderEjecutivoChildButNotMinisterio( child.getData())) {
                           retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio += childRetainedCredits;
                         //we do not accumulate retained credits for directChildrenOfPoderEjecutivoThatAreNotMinisterio
                           continue;
                       }
                       
                   }
                   accumulatedRetainedCreditsForCurrentDepartmentFromChildren += childRetainedCredits + childDepartmentSummary.getAccumulatedRetainedCreditsForCurrentDepartmentFromChildren();
               }
               
               Long totalRetainedCreditsForCurrentNode = accumulatedRetainedCreditsForCurrentDepartmentFromChildren + retainedCreditsForCurrentDepartment;
               
               GenericTreeNode<DepartmentSummary> currentNodeSummary = departmentSummaryMap.get(currentNode.getData().getId());
               DepartmentSummary currentDepartmentSummary = currentNodeSummary.getData();
               
               if(departmentService.isMinisterio( currentNode.getData())) {//is ministerio or secretaria, parent is root
                   //get retention for its directly dependent subdepartments
                   //accumulatedRetainedCreditsForCurrentDepartmentFromChildren = accumulatedRetainedCreditsForCurrentDepartmentFromChildren + retainedCreditsForCurrentDepartment;
                   
                   long availableToDistributeAmongDependentDepartments = Math.round( 0.7*totalRetainedCreditsForCurrentNode );
                   long availableForRootNode = totalRetainedCreditsForCurrentNode - availableToDistributeAmongDependentDepartments;
                   currentDepartmentSummary.setAvailableToDistributeAmongDependentDepartments(availableToDistributeAmongDependentDepartments);
                   currentDepartmentSummary.setAvailableForRootNode(availableForRootNode);
                   
                   currentDepartmentSummary.setDescription("Retenidos:" + retainedCreditsForCurrentDepartment + " - Disponibles para repartir en reparticiones dependientes: 70% de "+totalRetainedCreditsForCurrentNode + " = "+availableToDistributeAmongDependentDepartments +". Para Poder Ejecutivo: "+availableForRootNode);
               }else if(departmentService.isPoderEjecutivo(currentNode.getData())) {
                   long percentageRetainedForAggregatedMinisterios = Math.round( 0.3*totalRetainedCreditsForCurrentNode );
                   long availableCreditsForRoot =  percentageRetainedForAggregatedMinisterios + retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio;
                   currentDepartmentSummary.setDescription("Disponibles: 30% de " + totalRetainedCreditsForCurrentNode +" = "+percentageRetainedForAggregatedMinisterios+" + retenido de reparticiones directas del poder ejecutivo "+retainedFromDirectChildrenOfPoderEjecutivoThatAreNotMinisterio+"  = "+availableCreditsForRoot);
                   currentDepartmentSummary.setPercentageRetainedForAggregatedMinisterios(percentageRetainedForAggregatedMinisterios);
                   currentDepartmentSummary.setAvailableCreditsForRoot(availableCreditsForRoot);
               }else{ // other departments
                   currentDepartmentSummary.setDescription("Retenidos:" + retainedCreditsForCurrentDepartment + " - Creditos retenidos acumulados en subordinados: "+ accumulatedRetainedCreditsForCurrentDepartmentFromChildren) ;    
               }
               
                           
               currentDepartmentSummary.setRetainedCreditsForCurrentDepartment(retainedCreditsForCurrentDepartment);
               currentDepartmentSummary.setAccumulatedRetainedCreditsForCurrentDepartmentFromChildren(accumulatedRetainedCreditsForCurrentDepartmentFromChildren);
               currentDepartmentSummary.setTotalRetainedCreditsForCurrentNode(totalRetainedCreditsForCurrentNode);
               
              // System.out.println(currentNode.getData().getNodeName() + currentNode.getData().getWeight());    
           }
       
           return rootNodeSummary;
       
       }

       
       /*
       private void treeDepartmentSummaryToList(GenericTreeNode<DepartmentSummary> node, List<DepartmentSummary> resultsList) {

           resultsList.add(node.getData());
           
           List<GenericTreeNode<DepartmentSummary>> childrenArrayList = node.getChildren();
           
           Collections.sort(childrenArrayList, new Comparator<GenericTreeNode<DepartmentSummary>>()
           {
               public int compare( GenericTreeNode<DepartmentSummary> one, GenericTreeNode<DepartmentSummary> another){
                   return one.getData().getDepartment().getCode().compareTo(another.getData().getDepartment().getCode());
               }
               
           }
           );
           
           for (GenericTreeNode<DepartmentSummary>  childNode :childrenArrayList) {
               treeDepartmentSummaryToList(childNode, resultsList);
           }
           
       }*/
       
       
       @RequestMapping(value = "/rest/hierchicalRetainedCredits/findHierchicalRetainedCredits", method = RequestMethod.GET,
               produces = MediaType.APPLICATION_JSON_VALUE)
       public ResponseEntity<String> findHierchicalRetainedCredits(/*@PathVariable String departmentId,*/

               @RequestParam(value="departmentId", required=true) String departmentId,
               @RequestParam(value="creditsPeriodName", required=true) String creditsPeriodName
               ) throws JsonProcessingException {
           log.info("Started findHierchicalRetainedCredits");       

           
           //GenericTreeNode<Department> currentNode = departmentService.getSubTree(341l);
           GenericTreeNode<Department> currentNode = departmentService.getSubTree(Long.valueOf(departmentId));
           
           
           CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
           creditsPeriodQueryFilter.setName(creditsPeriodName);
           CreditsPeriod creditsPeriod = creditsPeriodService.find(creditsPeriodQueryFilter).get(0);

           
           GenericTreeNode<DepartmentSummary> resultNodes = nonRecursivePostOrder(currentNode, creditsPeriod);
           List<DepartmentSummary> resultsDepartmentSummaryList = new ArrayList<DepartmentSummary>();
           treeDepartmentSummaryToList(resultNodes, resultsDepartmentSummaryList);

           
           //print the list
           /*resultsDepartmentSummaryList.forEach(item->{
               System.out.println(item.toString());   
           });*/


           HttpHeaders responseHeaders = new HttpHeaders();
           responseHeaders.add("Content-Type", "application/json;charset=utf-8");

           Map<String, Object> responseMap = new ResponseMap<DepartmentSummary>().mapOK(resultsDepartmentSummaryList);

           String serializedResponse = objectMapper.writeValueAsString(responseMap);


           return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

       }
       
       private void treeDepartmentSummaryToList(GenericTreeNode<DepartmentSummary> node, List<DepartmentSummary> resultsList) {

           resultsList.add(node.getData());
           
           List<GenericTreeNode<DepartmentSummary>> childrenArrayList = node.getChildren();
           
           Collections.sort(childrenArrayList, new Comparator<GenericTreeNode<DepartmentSummary>>()
           {
               public int compare( GenericTreeNode<DepartmentSummary> one, GenericTreeNode<DepartmentSummary> another){
                   return one.getData().getDepartment().getCode().compareTo(another.getData().getDepartment().getCode());
               }
               
           }
           );
           
           for (GenericTreeNode<DepartmentSummary>  childNode :childrenArrayList) {
               treeDepartmentSummaryToList(childNode, resultsList);
           }
           
       }
       
       
       private static class DepartmentSummary{

           private Department department;

           Long retainedCreditsForCurrentDepartment = 0l;

           Long accumulatedRetainedCreditsForCurrentDepartmentFromChildren = 0l;

           Long totalRetainedCreditsForCurrentNode;

           Long availableToDistributeAmongDependentDepartments;

           Long availableForRootNode;
           
           Long percentageRetainedForAggregatedMinisterios;

           Long availableCreditsForRoot;
           
           private String description;
           

           public DepartmentSummary(Department department) {
               super();
               this.setDepartment(department);
           }


           public Department getDepartment() {
               return department;
           }

           public void setDepartment(Department department) {
               this.department = department;
           }
           
           public Long getRetainedCreditsForCurrentDepartment() {
               return retainedCreditsForCurrentDepartment;
           }

           public void setRetainedCreditsForCurrentDepartment(
                   Long retainedCreditsForCurrentDepartment) {
               this.retainedCreditsForCurrentDepartment = retainedCreditsForCurrentDepartment;
           }

           public Long getTotalRetainedCreditsForCurrentNode() {
               return totalRetainedCreditsForCurrentNode;
           }

           public void setTotalRetainedCreditsForCurrentNode(
                   Long totalRetainedCreditsForCurrentNode) {
               this.totalRetainedCreditsForCurrentNode = totalRetainedCreditsForCurrentNode;
           }
           
           public Long getAccumulatedRetainedCreditsForCurrentDepartmentFromChildren() {
               return accumulatedRetainedCreditsForCurrentDepartmentFromChildren;
           }

           public void setAccumulatedRetainedCreditsForCurrentDepartmentFromChildren(
                   Long accumulatedRetainedCreditsForCurrentDepartmentFromChildren) {
               this.accumulatedRetainedCreditsForCurrentDepartmentFromChildren = accumulatedRetainedCreditsForCurrentDepartmentFromChildren;
           }
           
           public Long getAvailableToDistributeAmongDependentDepartments() {
               return availableToDistributeAmongDependentDepartments;
           }


           public void setAvailableToDistributeAmongDependentDepartments(
                   Long availableToDistributeAmongDependentDepartments) {
               this.availableToDistributeAmongDependentDepartments = availableToDistributeAmongDependentDepartments;
           }


           public Long getAvailableForRootNode() {
               return availableForRootNode;
           }


           public void setAvailableForRootNode(Long availableForRootNode) {
               this.availableForRootNode = availableForRootNode;
           }

           public Long getPercentageRetainedForAggregatedMinisterios() {
               return percentageRetainedForAggregatedMinisterios;
           }


           public void setPercentageRetainedForAggregatedMinisterios(
                   Long percentageRetainedForAggregatedMinisterios) {
               this.percentageRetainedForAggregatedMinisterios = percentageRetainedForAggregatedMinisterios;
           }


           public Long getAvailableCreditsForRoot() {
               return availableCreditsForRoot;
           }


           public void setAvailableCreditsForRoot(Long availableCreditsForRoot) {
               this.availableCreditsForRoot = availableCreditsForRoot;
           }
           
           public String getDescription() {
               return description;
           }


           public void setDescription(String description) {
               this.description = description;
           }
           
           
           @Override
           public String toString() {
               ToStringBuilder builder = new ToStringBuilder(this,
                       ToStringStyle.SHORT_PREFIX_STYLE);
               
               builder.append("department", department);
               builder.append("retainedCreditsForCurrentDepartment",
                       retainedCreditsForCurrentDepartment);
               builder.append(
                       "accumulatedRetainedCreditsForCurrentDepartmentFromChildren",
                       accumulatedRetainedCreditsForCurrentDepartmentFromChildren);
               builder.append("totalRetainedCreditsForCurrentNode",
                       totalRetainedCreditsForCurrentNode);
               builder.append("availableToDistributeAmongDependentDepartments",
                       availableToDistributeAmongDependentDepartments);
               builder.append("availableForRootNode", availableForRootNode);
               
               builder.append("percentageRetainedForAggregatedMinisterios", percentageRetainedForAggregatedMinisterios);
               builder.append("availableCreditsForRoot", availableCreditsForRoot);
               return builder.toString();
           }



       }
}
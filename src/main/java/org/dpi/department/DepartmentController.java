package org.dpi.department;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.dpi.web.reporting.CanGenerateReportResult;
import org.dpi.web.reporting.ReportService;
import org.dpi.web.reporting.ReportServiceImpl.ManagementReports;
import org.janux.bus.persistence.EntityNotFoundException;
import org.janux.bus.security.Account;
import org.janux.bus.security.AccountImpl;
import org.janux.bus.security.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
public class DepartmentController {


	static Logger log = LoggerFactory.getLogger(DepartmentController.class);

	/**  name of the http Request parameter which we use to pass an department's code: 'departmentId' */
	public static final String PARAM_DEPARTMENT_ID = "departmentId";

	public static final String PARAM_CURR_PATH = "currPath";

	public static final String KEY_CURRENT_DEPARTMENT_ID = "currentDepartmentId";


	/** 
	 * key that we use when placing an Reparticion instance in a model: 'reparticion'
	 * TODO: make private; have clients use session getters/putters
	 */
	public static final String KEY_DEPARTMENT = "currentDepartment";

	/** 
	 * The key by which we store in the Session the list of Reparticions that the
	 * Principal is authorized to view: "myReparticions"
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

		// get the current reparticion in the session
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
			creditsEntryQueryFilter.setIdCreditsPeriod(creditsPeriod.getId());
			
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
			creditsEntryQueryFilter.setId(creditsEntryId);
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
				
			
			Long creditosConsumidosPorIngresosOAscensosOtorgados = this.creditsManagerService.getCreditosPorIngresosOAscensosOtorgados(previousCreditsPeriod.getId(), department.getId());
			
			historicPeriodSummaryData.setCreditosConsumidosPorIngresosOAscensosOtorgados(creditosConsumidosPorIngresosOAscensosOtorgados);
			
			
			Long saldoCreditosAlFinalPeriodo = creditosAcreditadosPorCargaInicial+creditosDisponiblesInicioPeriodo+creditosAcreditadosPorBajas-creditosConsumidosPorIngresosOAscensosOtorgados;
			
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


}
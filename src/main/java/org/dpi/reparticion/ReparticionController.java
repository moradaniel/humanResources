package org.dpi.reparticion;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.dpi.agente.AgenteService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodQueryFilter;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmploymentCreditsEntriesService;
import org.dpi.empleo.EmploymentCreditsEntriesServiceImpl;
import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.empleo.EmploymentService;
import org.dpi.empleo.EmploymentStatus;
import org.dpi.empleo.EmploymentVO;
import org.dpi.movimientoCreditos.CambiosMultiplesEstadoMovimientosForm;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.dpi.movimientoCreditos.MovimientoCreditosServiceImpl;
import org.dpi.movimientoCreditos.MovimientoCreditosVO;
import org.dpi.security.AccountSettings;
import org.dpi.security.UserAccessService;
import org.dpi.security.UserSettingsFactory;
import org.dpi.security.UserSettingsFactoryImpl;
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
public class ReparticionController {


	static Logger log = LoggerFactory.getLogger(ReparticionController.class);

	/**  name of the http Request parameter which we use to pass an Reparticion's code: 'reparticionCode' */
	public static final String PARAM_REPARTICION_ID = "reparticionId";

	public static final String PARAM_CURR_PATH = "currPath";

	public static final String KEY_CURRENT_REPARTICION_ID = "currentReparticionId";


	/** 
	 * key that we use when placing an Reparticion instance in a model: 'reparticion'
	 * TODO: make private; have clients use session getters/putters
	 */
	public static final String KEY_REPARTICION = "reparticion";

	/** 
	 * The key by which we store in the Session the list of Reparticions that the
	 * Principal is authorized to view: "myReparticions"
	 */
	private static final String KEY_REPARTICION_LIST = "myReparticiones";

	/** name of a generic view used to display messages: 'MessagePage' */
	public static final String VIEW_MESSAGE = "MessagePage";

	/** key that we use when placing a Message string in a model: 'message' */
	public static final String KEY_MESSAGE = "message";

	private ReparticionService reparticionService;

	@Resource(name = "accountServiceGeneric")
	private AccountService accountService;

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "employmentCreditsEntriesService")
	private EmploymentCreditsEntriesService employmentCreditsEntriesService;

	@Resource(name = "agenteService")
	private AgenteService agenteService;


	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;

	@Resource(name = "movimientoCreditosService")
	private MovimientoCreditosService movimientoCreditosService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	@Resource(name = "accountSettingsFactory")
	private UserSettingsFactory settingsFactory;	

	@Inject
	public ReparticionController(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}


	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}

	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

	public static Reparticion getCurrentReparticion(HttpServletRequest request)
	{
		return (Reparticion) request.getAttribute(ReparticionController.KEY_REPARTICION);
	}

	@RequestMapping(value = "/reparticiones/reparticion/showCreditos", method = RequestMethod.GET)
	public String showCreditos(HttpServletRequest request, HttpServletResponse response, Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){

			if (reparticion instanceof Reparticion) 
			{
				model.addAttribute(PARAM_REPARTICION_ID, reparticion.getId());
			}
			
			CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
			
			Long creditosDisponiblesInicioPeriodoActual = this.administradorCreditosService.getCreditosDisponiblesAlInicioPeriodo(currentCreditsPeriod,reparticion.getId());
			model.addAttribute("creditosDisponiblesInicioPeriodoActual", creditosDisponiblesInicioPeriodoActual);
			
			
			Long creditosAcreditadosPorBajaDurantePeriodoActual = this.administradorCreditosService.getCreditosPorBajasDeReparticion(currentCreditsPeriod,reparticion.getId());
			model.addAttribute("creditosAcreditadosPorBajaDurantePeriodoActual", creditosAcreditadosPorBajaDurantePeriodoActual);
			
			
			
			Long creditosConsumidosPorIngresosOAscensosSolicitadosPeriodoActual = this.administradorCreditosService.getCreditosPorIngresosOAscensosSolicitados(currentCreditsPeriod, reparticion.getId());
			
			model.addAttribute("creditosConsumidosPorIngresosOAscensosSolicitadosPeriodoActual", creditosConsumidosPorIngresosOAscensosSolicitadosPeriodoActual);
			

			Long creditosPorIngresosOAscensosOtorgadosPeriodoActual = this.administradorCreditosService.getCreditosPorIngresosOAscensosOtorgados(currentCreditsPeriod, reparticion.getId());
			
			model.addAttribute("creditosPorIngresosOAscensosOtorgados", creditosPorIngresosOAscensosOtorgadosPeriodoActual);

			
			

			Long creditosDisponiblesSegunSolicitadoPeriodoActual = this.administradorCreditosService.getCreditosDisponiblesSegunSolicitado(currentCreditsPeriod,reparticion.getId());
			model.addAttribute("creditosDisponiblesSegunSolicitadoPeriodoActual", creditosDisponiblesSegunSolicitadoPeriodoActual);
			
			Long creditosDisponiblesSegunOtorgadoPeriodoActual = this.administradorCreditosService.getCreditosDisponiblesSegunOtorgado(currentCreditsPeriod,reparticion.getId());
			model.addAttribute("creditosDisponiblesSegunOtorgadoPeriodoActual", creditosDisponiblesSegunOtorgadoPeriodoActual);
			
						
			
			//historicos 2012
					
			Long creditosAcreditadosPorCargaInicial2012 = this.administradorCreditosService.getCreditosPorCargaInicialDeReparticion(currentCreditsPeriod.getPreviousCreditsPeriod(),reparticion.getId());
			model.addAttribute("creditosAcreditadosPorCargaInicial2012", creditosAcreditadosPorCargaInicial2012);
						
			
			Long creditosAcreditadosPorBajas2012 = this.administradorCreditosService.getCreditosPorBajasDeReparticion(currentCreditsPeriod.getPreviousCreditsPeriod(),reparticion.getId());
			model.addAttribute("creditosAcreditadosPorBajas2012", creditosAcreditadosPorBajas2012);
						
			
			Long creditosConsumidosPorIngresosOAscensosOtorgados2012 = this.administradorCreditosService.getCreditosPorIngresosOAscensosOtorgados(currentCreditsPeriod.getPreviousCreditsPeriod(), reparticion.getId());
			
			model.addAttribute("creditosConsumidosPorIngresosOAscensosOtorgados2012", creditosConsumidosPorIngresosOAscensosOtorgados2012);
			
			
			Long saldoCreditosAlFinalPeriodo2012 = creditosAcreditadosPorCargaInicial2012+creditosAcreditadosPorBajas2012-creditosConsumidosPorIngresosOAscensosOtorgados2012;
			
			model.addAttribute("saldoCreditosAlFinalPeriodo2012", saldoCreditosAlFinalPeriodo2012);

		}
		return "reparticiones/show";
	}


	@RequestMapping(value = "/reparticiones/reparticion/showEmpleos", method = RequestMethod.GET)
	public String showEmpleos(	HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){
			
			EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
			empleoQueryFilter.setReparticionId(String.valueOf(reparticion.getId()));
			empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.ACTIVO);
			
			List<Empleo> empleosActivos = employmentService.find(empleoQueryFilter);
			
			//canAccountAscenderAgente
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currenUser = (Account)accountObj;
			List<EmploymentVO> employmentsVO = employmentCreditsEntriesService.buildEmploymentsVO(empleosActivos,reparticion.getId(),currenUser);
			
			model.addAttribute("empleosActivos", employmentsVO);
			
			
			Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("canAccountIngresarAgente", EmploymentCreditsEntriesServiceImpl.canAccountIngresarAgente(currentUser));
			


		}
		return "reparticiones/edit";
	}

	@RequestMapping(value = "/reparticiones/{reparticionId}", method = RequestMethod.POST)
	public String edit(ReparticionImpl reparticion, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (result.hasErrors()) {
			return "reparticiones/edit";
		}
		reparticionService.saveOrUpdate(reparticion);
		return (AjaxUtils.isAjaxRequest(requestedWith)) ? "reparticiones/show" : "redirect:/reparticiones/" + reparticion.getId();
	}

	@RequestMapping(value = "/reparticiones/reparticion/showMovimientos/{creditsPeriodName}", method = RequestMethod.GET)
	public String showMovimientos(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long creditsPeriodName,
			Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);
		
		
		if (reparticion != null){
			
			long currentPeriodName = creditsPeriodService.getCurrentCreditsPeriodYear();
			
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
			empleoQueryFilter.setReparticionId(reparticion.getId().toString());

			MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
			movimientoCreditosQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);

			CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
			creditsPeriodQueryFilter.setName(String.valueOf(creditsPeriodName));
			
			List<CreditsPeriod> currentCreditsPeriods = creditsPeriodService.find(creditsPeriodQueryFilter);
			CreditsPeriod creditsPeriod = currentCreditsPeriods.get(0);
			movimientoCreditosQueryFilter.setIdCreditsPeriod(creditsPeriod.getId());
			
			List<MovimientoCreditos> movimientoCreditosReparticion = movimientoCreditosService.find(movimientoCreditosQueryFilter);
			
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currenUser = (Account)accountObj;
					
			List<MovimientoCreditosVO> movimientoCreditosVOReparticion = movimientoCreditosService.buildMovimientoCreditosVO(movimientoCreditosReparticion,currenUser);
					
			model.addAttribute("movimientos", movimientoCreditosVOReparticion);
			
			CambiosMultiplesEstadoMovimientosForm cambiosMultiplesEstadoMovimientosForm =  new CambiosMultiplesEstadoMovimientosForm();
			for(MovimientoCreditosVO movimientoCreditosVO :movimientoCreditosVOReparticion){
				cambiosMultiplesEstadoMovimientosForm.getMovimientos().add(movimientoCreditosVO.getMovimientoCreditos());
			}
			
			model.addAttribute("grantedStatuses", GrantedStatus.values());
			model.addAttribute("cambiosMultiplesEstadoMovimientosForm", cambiosMultiplesEstadoMovimientosForm);
			
			Long creditosDisponiblesSegunSolicitadoPeriodoActual = this.administradorCreditosService.getCreditosDisponiblesSegunSolicitado(creditsPeriodService.getCurrentCreditsPeriod(),reparticion.getId());
			model.addAttribute("creditosDisponiblesSegunSolicitadoPeriodoActual", creditosDisponiblesSegunSolicitadoPeriodoActual);
			
			Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			model.addAttribute("canAccountCambiarEstadoMovimiento",MovimientoCreditosServiceImpl.canAccountCambiarEstadoMovimientos(currentUser));
			
		}
		return "reparticiones/movimientos";
	}
	
	

	@RequestMapping(value = "/reparticiones/reparticion/movimientos/{movimientoId}/borrar", method = RequestMethod.GET)
	public String borrarMovimiento(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long movimientoId,
			Model model) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){
			MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
			movimientoCreditosQueryFilter.setId(movimientoId);
			EmploymentQueryFilter empleoFilter = new EmploymentQueryFilter();
			empleoFilter.setReparticionId(reparticion.getId().toString());
			movimientoCreditosQueryFilter.setEmploymentQueryFilter(empleoFilter);

			MovimientoCreditos movimiento = movimientoCreditosService.find(movimientoCreditosQueryFilter).get(0);

			if(movimiento!=null){
				movimientoCreditosService.delete(movimiento);	
			}
		}

		return "redirect:/reparticiones/reparticion/showMovimientos/"+creditsPeriodService.getCurrentCreditsPeriodYear();

	}

	/*@ExceptionHandler(MissingServletRequestParameterException.class)
	public String handleMyException(Exception  exception) {
		return "yourErrorViewName";
	}*/


	public MovimientoCreditosService getMovimientoCreditosService() {
		return movimientoCreditosService;
	}


	public void setMovimientoCreditosService(
			MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
	}



	/**
	 * Reloads the list of reparticiones that a user is allowed to edit via the AccessService.
	 * If the reparticiones list for a user changes, the changes will be seen immediately
	 * @param request
	 * @param accessService
	 */
	public static void refreshRequestReparticionList(HttpServletRequest request,UserAccessService accessService)
	{
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String accountName = ((Account) principal).getName();
		Set<ReparticionAdminInfo> reparticiones = accessService.getReparticionListForAccount(accountName, null);

		request.setAttribute(ReparticionController.KEY_REPARTICION_LIST,reparticiones);
	}

	/** 
	 * Loads the Reparticion in the Request, and checks that the current account has the proper
	 * permissions to view that Reparticion
	 */
	public static void refreshRequestReparticion(HttpServletRequest request,ReparticionService reparticionService,
			UserAccessService accessService,boolean checkAccess)
	{
		boolean createSessionIfNeeded = true;
		HttpSession session = request.getSession(createSessionIfNeeded);
		Long reparticionId = (Long) session.getAttribute(ReparticionController.KEY_CURRENT_REPARTICION_ID);

		AccountSettings settings = UserSettingsFactoryImpl.getSettingsForPrincipal();

		checkAccess = checkAccess || (reparticionId == null); // first time in

		if(reparticionId == null){
			if(StringUtils.hasText(settings.getLastHotel())){
				reparticionId = Long.parseLong(settings.getLastHotel());	
			}
		}

		Reparticion reparticion = null;

		if (reparticionId == null)
		{
			log.warn("There is currently no Reparticion active in the session");
		}
		else 
		{
			try
			{ 
				reparticion = reparticionService.findById(reparticionId);
			}
			catch (EntityNotFoundException e)
			{
				log.error(e.toString());
				reparticion = null;
			}

			if (checkAccess)
			{
				Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Account account = (Account) principal;
				boolean hasAccess = accessService.hasAccessToReparticion(account, reparticionId); 

				if (!hasAccess)
				{
					reparticion = null;
					settings.setLastHotel(null);
					log.warn("Account: '" + account.getName() + "' attempting to access page without authorization");
				}
			}
		}

		request.setAttribute(ReparticionController.KEY_REPARTICION,reparticion);
	}


	@RequestMapping(value = "/reparticiones/select", method = RequestMethod.GET)
	public ModelAndView selectReparticion(HttpServletRequest request, 
			HttpServletResponse response,
			@RequestParam("reparticionId") Long reparticionId,
			@RequestParam("currPath") String currPath)
					throws Exception 
					{
		ModelAndView returnView = null;

		if (this.doSelectReparticion(request,reparticionId))
		{
			if (log.isDebugEnabled()) {log.debug("Doing redirect to: '" + currPath + "'");}
			returnView = new ModelAndView(new RedirectView("/home",true));

		}	
		else
		{
			returnView = new ModelAndView(VIEW_MESSAGE, KEY_MESSAGE, "Unable to load Reparticion with code '" + reparticionId + "'");
		}

		return returnView;
	}

	/** 
	 * deep loads an Reparticion based on a code passed in the query string parameter denoted by
	 * ReparticionInfoController.PARAM_REPARTICION_ID and forwards to a summary display page 
	 * @returns whether selection was successful (empty code allowed; for non-empty code, reparticion must
	 * be able to be loaded.) 
	 */
	public boolean doSelectReparticion(HttpServletRequest request, Long id) 
	{
		boolean ok = true;
		Reparticion reparticion = null;

		if (id!=null)
		{
			reparticion = reparticionService.findById(id);

			if (reparticion != null)
			{

				Account januxUserDetailsAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

				Account account = accountService.loadAccountByName(januxUserDetailsAccount.getName());

				AccountSettings accountSettings = (AccountSettings)settingsFactory.getSettingsForAccount(account);

				accountSettings.setLastHotel(String.valueOf(id));

				accountService.saveOrUpdateAccount((AccountImpl)account);
				request.getSession().setAttribute(KEY_CURRENT_REPARTICION_ID, reparticion.getId());
				request.setAttribute(KEY_REPARTICION, reparticion);
				if (log.isDebugEnabled()) {log.debug("Completed doing doSelectReparticion with ok status");}
			}
			else {
				ok = false;
			}
		}

		return ok;
	}


}
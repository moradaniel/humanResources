package org.dpi.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.common.CustomObjectMapper;
import org.dpi.common.ResponseMap;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.person.PersonService;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionController;
import org.dpi.util.PageList;
import org.dpi.web.response.StatusResponse;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class EmploymentController {

	static Logger log = LoggerFactory.getLogger(EmploymentController.class);

	private EmploymentCreditsEntriesService employmentCreditsEntriesService;

	@Resource(name = "employmentService")
	private EmploymentService employmentService;

	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "occupationalGroupService")
	private OccupationalGroupService occupationalGroupService;



	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;

	@Resource(name = "personService")
	private PersonService personService;

	@Inject
	public EmploymentController(EmploymentCreditsEntriesService employmentCreditsEntriesService) {
		this.employmentCreditsEntriesService = employmentCreditsEntriesService;
	}


	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}

	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}

	public EmploymentCreditsEntriesService getEmploymentCreditsEntriesService() {
		return employmentCreditsEntriesService;
	}


	public void setEmploymentCreditsEntriesService(EmploymentCreditsEntriesService employmentCreditsEntriesService) {
		this.employmentCreditsEntriesService = employmentCreditsEntriesService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}



	/*@ModelAttribute
	public Reparticion addReparticion(@PathVariable Long id) {
		return (id != null) ? reparticionService.findById(id) : null;
	}*/

	/*@RequestMapping(value = "/reparticiones/{id}", method = RequestMethod.GET)
	public String show(@PathVariable Long id, Model model) {
		Long creditosDisponibles = this.administradorCreditosService.getCreditosDisponibles(id);
		model.addAttribute("creditosDisponibles", creditosDisponibles);
		return "reparticiones/show";
	}*/

	@RequestMapping(value = "/employments/{id}/deactivatePerson", method = RequestMethod.GET)
	public String deactivate(@PathVariable Long id, Model model) {
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setEmploymentId(id);

		List<Employment> employments = employmentService.findEmployments(employmentQueryFilter);

		Employment employmentToDeactivate = employments.get(0);


		//TODO check if the user has permission to deactivate THIS employment


		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		employmentCreditsEntriesService.deactivate(employmentToDeactivate,currentUser);

		return "redirect:/reparticiones/reparticion/showEmployments";

	}
	
	
	
	@RequestMapping(value = "/employments/{id}/undoDeactivatePerson", method = RequestMethod.GET)
	public String undoDeactivation(@PathVariable Long id, Model model) {
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setEmploymentId(id);

		List<Employment> employments = employmentService.findEmployments(employmentQueryFilter);

		Employment employmentToUndoDeactivation = employments.get(0);


		//TODO check if the user has permission to deactivate THIS employment


		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		employmentCreditsEntriesService.undoDeactivation(employmentToUndoDeactivation,currentUser);

		return "redirect:/reparticiones/reparticion/showEmployments";

	}
	

	@RequestMapping(value = "/employments/{id}/promotePerson", method = RequestMethod.GET)
	public String setupFormPromotePerson(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long id, Model model) {


		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){

			EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
			employmentQueryFilter.setEmploymentId(id);

			List<Employment> employments = employmentService.findEmployments(employmentQueryFilter);

			Employment currentEmployment = employments.get(0);

			if(currentEmployment.getCentroSector().getReparticion().getId().longValue()!=reparticion.getId().longValue()){

				return "redirect:/reparticiones/reparticion/showEmployments";
			}

			model.addAttribute("currentEmployment", currentEmployment);
			model.addAttribute("availableCategoriesForPromotion", employmentService.getAvailableCategoriesForPromotion(currentEmployment));

		}
		return "creditsentries/promotePersonForm";
	}





	@RequestMapping(value = "/employments/promotePerson", method = RequestMethod.POST)
	public String promotePersonFromForm(HttpServletRequest request, Model model) throws Exception {



		String idCurrentEmployment = ServletRequestUtils.getStringParameter(request, "idCurrentEmployment");

		String proposedCategoryCode = ServletRequestUtils.getStringParameter(request, "proposedCategoryCode");

		if (!StringUtils.hasText(proposedCategoryCode)){
			return "redirect:/employments/"+idCurrentEmployment+"/promotePerson";
		}


		/*if(bindingResult.hasErrors()) { 
			model.addAttribute("accountVO", accountVO); 
			return VIEW_PAGE;
		}*/

		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setEmploymentId(Long.parseLong(idCurrentEmployment));

		List<Employment> empleos = employmentService.findEmployments(empleoQueryFilter);

		Employment currentEmployment = empleos.get(0);

		employmentCreditsEntriesService.promotePerson(currentEmployment, proposedCategoryCode);

		return "redirect:/reparticiones/reparticion/showEmployments";
	}


	@RequestMapping(value = "/employments/proposeNewEmploymentForm", method = RequestMethod.POST)
	public String proposeNewEmploymentFromForm(HttpServletRequest request, Model model) throws Exception {



		//String idEmpleoActual = ServletRequestUtils.getStringParameter(request, "idEmpleoActual");

		String centroSectorId = ServletRequestUtils.getStringParameter(request, "centroSectorId");
		String proposedCategoryCode = ServletRequestUtils.getStringParameter(request, "proposedCategoryCode");

		if (!StringUtils.hasText(centroSectorId) || !StringUtils.hasText(proposedCategoryCode)){
			return "redirect:/employments/proposeNewEmploymentForm";
		}


		/*if(bindingResult.hasErrors()) { 
			model.addAttribute("accountVO", accountVO); 
			return VIEW_PAGE;
		}*/


		employmentCreditsEntriesService.proposeNewEmployment(proposedCategoryCode, Long.parseLong(centroSectorId));

		return "redirect:/reparticiones/reparticion/showEmployments";
	}






	@RequestMapping(value = "/employments/altaEmpleoForm", method = RequestMethod.GET)
	public String setupFormAltaEmpleo(Model model) {
		/*if(!model.containsAttribute("nuevoEmpleo")){


			// Create initial object
			Empleo nuevoEmpleo = new EmpleoImpl();


			// Add to model so it can be display in view
			model.addAttribute("nuevoEmployment", nuevoEmployment);
		}*/

		/*
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setEmploymentId(id.toString());

		List<Employment> empleos = empleoService.find(empleoQueryFilter);

		Employment currentEmployment = empleos.get(0);

		Agente agenteACambiarCategoria = currentEmployment.getAgente();


		model.addAttribute("currentEmployment", currentEmployment);
		model.addAttribute("categoriasDisponiblesParaAscenso", getCategoriasDisponiblesParaAscenso(currentEmployment));
		 */


		/*empleoService.deactivate(empleoACambiarCategoria);

		Employment empleoDadoDeBaja = empleoACambiarCategoria;

		//buscar los empleos de la reparticion
		empleoQueryFilter = new EmploymentQueryFilter();

		Reparticion reparticion = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());

		empleos = empleoService.find(empleoQueryFilter);*/


		return "employments/altaEmpleoForm";
	}


	@RequestMapping(value = "/employments/proposeNewEmploymentForm", method = RequestMethod.GET)
	public String setupFormIngresarPropuestaAgente(
			HttpServletRequest request, 
			HttpServletResponse response,
			Model model) {
		/*if(!model.containsAttribute("nuevoEmployment")){


			// Create initial  object
			Employment nuevoEmployment = new EmploymentImpl();


			// Add to model so it can be display in view
			model.addAttribute("nuevoEmployment", nuevoEmployment);
		}*/

		/*
		EmploymentQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setEmploymentId(id.toString());

		List<Employment> empleos = empleoService.find(empleoQueryFilter);

		Employment currentEmployment = empleos.get(0);

		Agente agenteACambiarCategoria = currentEmployment.getAgente();


		model.addAttribute("currentEmployment", currentEmployment);
		model.addAttribute("categoriasDisponiblesParaAscenso", getCategoriasDisponiblesParaAscenso(currentEmployment));
		 */


		/*empleoService.deactivate(empleoACambiarCategoria);

		Employment empleoDadoDeBaja = empleoACambiarCategoria;

		//buscar los empleos de la reparticion
		empleoQueryFilter = new EmploymentQueryFilter();

		Reparticion reparticion = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());

		empleos = empleoService.find(empleoQueryFilter);*/

		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);


		List<CentroSector> centroSectoresReparticion = centroSectorService.findCentroSectores(reparticion);

		model.addAttribute("centroSectoresDeReparticion", centroSectoresReparticion);

		return "employments/proposeNewEmploymentForm";
	}

	@RequestMapping(value="/employments/crearEmpleo", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse crearEmpleoFromForm(
			@RequestParam String idPerson,
			@RequestParam String idCentroSector){

		//User newUser = new User(username, password, firstName, lastName, new Role(role));
		//Boolean result = service.create(newUser);
		boolean result=true;

		StatusResponse response = new StatusResponse();
		response.setSuccess(result);
		if(result){
			response.setMessage("Empleo dado de alta con exito");
		}else{
			response.setMessage("Error al crear employment");

		}
		return response;
	}


	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}

	/*@RequestMapping(value = "/reparticiones/{id}", method = RequestMethod.POST)
	public String edit(ReparticionImpl reparticion, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (result.hasErrors()) {
			return "reparticiones/edit";
		}
		reparticionService.saveOrUpdate(reparticion);
		return (AjaxUtils.isAjaxRequest(requestedWith)) ? "reparticiones/show" : "redirect:/reparticiones/" + reparticion.getId();
	}*/


	/**
	 * get all employments of a department
	 * @param reparticionId
	 * @return
	 * @throws JsonProcessingException 
	 */

	@RequestMapping(value = "/rest/reparticiones/{reparticionId}/employments", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> employmentsSearch(@PathVariable String reparticionId,

			//@RequestParam(value="apellidoNombre", required=false) String apellidoNombre,
			//@RequestParam(value="cuil", required=false) String cuil,
			@RequestParam(value="employmentstatus", required=false) String employmentstatus,
			//@RequestParam(value="page", required=false) Integer page,//pageNumber requested
			@RequestParam(value="pageNumber", required=false, defaultValue = "1") Integer page,//pageNumber requested
			//@RequestParam(value="count", required=false) Integer pageSize,//number of rows requested (pagesize)
			@RequestParam(value="pageSize", required=false, defaultValue = "10") Integer pageSize,//number of rows requested (pagesize)
			//@RequestParam(value="filter[apellidoNombre]", required=false) String apellidoNombre,
			@RequestParam(value="apellidoNombre", required=false) String apellidoNombre,
			//@RequestParam(value="filter[cuil]", required=false) String cuil,
			@RequestParam(value="cuil", required=false) String cuil,
			@RequestParam(value="sidx", required=false) String sidx,
			@RequestParam(value="sord", required=false) String sord

			//@RequestParam(value="page", required=false) Integer page,//pageNumber requested
			//@RequestParam(value="rows", required=false) Integer rows,//number of rows requested (pagesize)
			//@RequestParam(value="sidx", required=false) String sidx,
			//@RequestParam(value="sord", required=false) String sord) 

			) throws JsonProcessingException {
		log.info("Started employments paginated search");		

		/*if(true) {
		    throw new JsonGenerationException("errorrrrrrrrrrr");
		}*/

		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setReparticionId(Long.parseLong(reparticionId));


		if(StringUtils.hasText(apellidoNombre)) {
			employmentQueryFilter.setApellidoNombre(apellidoNombre);
		}

		if(StringUtils.hasText(cuil)) {
			employmentQueryFilter.setCuil(cuil);
		}

		if(StringUtils.hasText(employmentstatus)) {
			employmentQueryFilter.addEmploymentStatus(EmploymentStatus.valueOf(employmentstatus));
		}
		
		employmentQueryFilter.setPageSize(pageSize);
		employmentQueryFilter.setPage(page-1);

		PageList<Employment> employments = employmentService.findEmployments(employmentQueryFilter);
		
		//List<Employment> employments = employmentService.find(employmentQueryFilter);


		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		List<EmploymentVO> employmentsVO = employmentCreditsEntriesService.buildEmploymentsVO(employments,Long.parseLong(reparticionId),currenUser);



		ObjectMapper mapper = new CustomObjectMapper();

		long total = employments.getTotalItems();


		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json;charset=utf-8");

		Map<String, Object> responseMap = new ResponseMap<EmploymentVO>().mapOK(employmentsVO,total);

		String serializedResponse = mapper.writeValueAsString(responseMap);


		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

	}
	
	


	@RequestMapping(value = "/rest/employments/statuses", method = RequestMethod.GET)
	public ResponseEntity<String> getAllEmploymentsStatus() throws JsonProcessingException {
		log.info("Start getAllEmploymentsStatus.");		

		ObjectMapper mapper = new CustomObjectMapper();

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.setContentType(MediaType.APPLICATION_JSON);

		List<EmploymentStatus> searcheableEmploymentStatusesEmploymentStatus = new ArrayList<EmploymentStatus>();

		searcheableEmploymentStatusesEmploymentStatus.add(EmploymentStatus.ACTIVO);
		searcheableEmploymentStatusesEmploymentStatus.add(EmploymentStatus.BAJA);

		Map<String, Object> responseMap = new ResponseMap<EmploymentStatus>().mapOK(searcheableEmploymentStatusesEmploymentStatus);

		String serializedResponse = mapper.writeValueAsString(responseMap);

		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

	}
	
	
    @RequestMapping(value = "/rest/reparticiones/{reparticionId}/employments", method = RequestMethod.POST)
    public ResponseEntity<String> saveEmployment(@PathVariable String reparticionId,@RequestBody EmploymentImpl employment) throws JsonProcessingException{
    	
    	/*if(true) {
    		throw new RuntimeException("Error de prueba");
    	}*/
    	
    	
    	
    	//retrieve the existing employments
    	EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
    	employmentQueryFilter.setReparticionId(Long.parseLong(reparticionId));
    	employmentQueryFilter.setEmploymentId(employment.getId());
    	
    	//TODO check if the employment exists
    	Employment existingEmployment = employmentService.findEmployments(employmentQueryFilter).get(0);
    			
    	//set the desired attributes
    	existingEmployment.getPerson().setApellidoNombre(employment.getPerson().getApellidoNombre());
    	existingEmployment.getPerson().setCuil(employment.getPerson().getCuil());
    	
    	//save the person. This is not necessary if we configure cascade save-update 
    	personService.saveOrUpdate(existingEmployment.getPerson());
    	
    	
    	//save the employment
    	employmentService.saveOrUpdate(existingEmployment);
        /*if (employment.getFirstName().length() <= 3 || employment.getLastName().length() <= 3) {
            throw new IllegalArgumentException("moreThan3Chars");
        }
        personService.addPerson(employment);*/
       // return new ResponseMessage(ResponseMessage.Type.success, "personAdded");
        
    	//TODO inject this as a Spring bean!!!
		ObjectMapper mapper = new CustomObjectMapper();

        
		Map<String, Object> responseMap = new ResponseMap<Employment>().mapOK(existingEmployment, "Employment saved");

		String serializedResponse = mapper.writeValueAsString(responseMap);

		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json;charset=utf-8");
		
				
		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);
    }




	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

}
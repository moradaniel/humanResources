package org.dpi.employment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.common.ResponseMap;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.department.Department;
import org.dpi.department.DepartmentController;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.occupationalGroup.OccupationalGroupQueryFilter;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.person.Person;
import org.dpi.person.PersonService;
import org.dpi.subDepartment.SubDepartment;
import org.dpi.subDepartment.SubDepartmentService;
import org.dpi.util.PageList;
import org.dpi.web.response.StatusResponse;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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



	@Resource(name = "subDepartmentService")
	private SubDepartmentService subDepartmentService;

	@Resource(name = "personService")
	private PersonService personService;
	
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper objectMapper;

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


	@RequestMapping(value = "/employments/{id}/deactivatePerson", method = RequestMethod.GET)
	public String deactivate(@PathVariable Long id, Model model) {
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setEmploymentId(id);

		List<Employment> employments = employmentService.findEmployments(employmentQueryFilter);

		Employment employmentToDeactivate = employments.get(0);


		//TODO check if the user has permission to deactivate THIS employment


		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		employmentCreditsEntriesService.deactivate(employmentToDeactivate,currentUser);

		return "redirect:/departments/department/showEmployments";

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

		return "redirect:/departments/department/showEmployments";

	}
	

	@RequestMapping(value = "/employments/{id}/promotePerson", method = RequestMethod.GET)
	public String setupFormPromotePerson(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long id, Model model) {


		final Department department = DepartmentController.getCurrentDepartment(request);

		if (department != null){

			EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
			employmentQueryFilter.setEmploymentId(id);

			List<Employment> employments = employmentService.findEmployments(employmentQueryFilter);

			Employment currentEmployment = employments.get(0);

			if(currentEmployment.getSubDepartment().getDepartment().getId().longValue()!=department.getId().longValue()){

				return "redirect:/departments/department/showEmployments";
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

		return "redirect:/departments/department/showEmployments";
	}


	@RequestMapping(value = "/employments/proposeNewEmploymentForm", method = RequestMethod.POST)
	public String proposeNewEmploymentFromForm(HttpServletRequest request, Model model) throws Exception {



		//String idEmpleoActual = ServletRequestUtils.getStringParameter(request, "idEmpleoActual");

		String subDepartmentId = ServletRequestUtils.getStringParameter(request, "subDepartmentId");
		String proposedCategoryCode = ServletRequestUtils.getStringParameter(request, "proposedCategoryCode");

		if (!StringUtils.hasText(subDepartmentId) || !StringUtils.hasText(proposedCategoryCode)){
			return "redirect:/employments/proposeNewEmploymentForm";
		}


		/*if(bindingResult.hasErrors()) { 
			model.addAttribute("accountVO", accountVO); 
			return VIEW_PAGE;
		}*/


		employmentCreditsEntriesService.proposeNewEmployment(proposedCategoryCode, Long.parseLong(subDepartmentId));

		return "redirect:/departments/department/showEmployments";
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

		//buscar los empleos de la department
		empleoQueryFilter = new EmploymentQueryFilter();

		Reparticion department = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(department.getId().toString());

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

		//buscar los empleos de la department
		empleoQueryFilter = new EmploymentQueryFilter();

		Reparticion department = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(department.getId().toString());

		empleos = empleoService.find(empleoQueryFilter);*/

		final Department department = DepartmentController.getCurrentDepartment(request);


		List<SubDepartment> subDepartmentsOfDepartment = subDepartmentService.findSubDepartments(department);

		model.addAttribute("subDepartmentsOfDepartment", subDepartmentsOfDepartment);

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


	public SubDepartmentService getSubDepartmentService() {
		return subDepartmentService;
	}


	public void setSubDepartmentService(SubDepartmentService subDepartmentService) {
		this.subDepartmentService = subDepartmentService;
	}

	/*@RequestMapping(value = "/departments/{id}", method = RequestMethod.POST)
	public String edit(ReparticionImpl department, BindingResult result,
			@RequestHeader(value = "X-Requested-With", required = false) String requestedWith) {
		if (result.hasErrors()) {
			return "departments/edit";
		}
		departmentService.saveOrUpdate(department);
		return (AjaxUtils.isAjaxRequest(requestedWith)) ? "departments/show" : "redirect:/departments/" + department.getId();
	}*/


	/**
	 * Search employments of a department
	 * @param departmentId
	 * @return
	 * @throws JsonProcessingException 
	 */

	@RequestMapping(value = "/rest/departments/employments", method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> employmentsSearch(

	        @RequestParam(value="departmentId", required=false) String departmentId,

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


		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		if(departmentId == null || "null".equals(departmentId)) {
		    departmentId = null;
		    //search in all departments
		    employmentQueryFilter.setDepartmentId(null);    
		}else {
		    employmentQueryFilter.setDepartmentId(Long.parseLong(departmentId));    
		}
		
		


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

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account currenUser = (Account)accountObj;
		
		List<EmploymentVO> employmentsVO = null;
		if(departmentId != null) {
		    //build with permision to do actions
		    employmentsVO = employmentCreditsEntriesService.buildEmploymentsVO(employments,Long.parseLong(departmentId),currenUser);
		}
		else {
	          //build with no permision to do no actions. Just for searching
		    employmentsVO = new ArrayList<>();
		    for(Employment employment : employments) {
	          EmploymentVO employmentVO = new EmploymentVO();
	            employmentVO.setEmployment(employment);
	            employmentsVO.add(employmentVO);
		    }
		}


		long total = employments.getTotalItems();


		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json;charset=utf-8");

		Map<String, Object> responseMap = new ResponseMap<EmploymentVO>().mapOK(employmentsVO,total);

		String serializedResponse = objectMapper.writeValueAsString(responseMap);


		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

	}
	
	


	@RequestMapping(value = "/rest/employments/statuses", method = RequestMethod.GET)
	public ResponseEntity<String> getAllEmploymentsStatus() throws JsonProcessingException {
		log.info("Start getAllEmploymentsStatus.");		

		List<EmploymentStatus> searcheableEmploymentStatusesEmploymentStatus = new ArrayList<EmploymentStatus>();

		searcheableEmploymentStatusesEmploymentStatus.add(EmploymentStatus.ACTIVO);
		searcheableEmploymentStatusesEmploymentStatus.add(EmploymentStatus.BAJA);

		Map<String, Object> responseMap = new ResponseMap<EmploymentStatus>().mapOK(searcheableEmploymentStatusesEmploymentStatus);

		String serializedResponse = objectMapper.writeValueAsString(responseMap);

	    HttpHeaders responseHeaders = new HttpHeaders();
	    responseHeaders.add("Content-Type", "application/json;charset=utf-8");
		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

	}
	
	
    @RequestMapping(value = "/rest/departments/{departmentId}/employments", method = RequestMethod.POST)
    public ResponseEntity<String> saveEmployment(@PathVariable String departmentId,@RequestBody EmploymentImpl modifiedEmployment) throws JsonProcessingException{
    	
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json;charset=utf-8");
        
    	/*if(true) {
    		throw new RuntimeException("Error de prueba");
    	}*/
    	    	
    	
    	//retrieve the existing employments
    	EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
    	//employmentQueryFilter.setDepartmentId(Long.parseLong(departmentId));
    	employmentQueryFilter.setEmploymentId(modifiedEmployment.getId());
    	
    	//TODO check if the employment exists
    	Employment existingEmployment = employmentService.findEmployments(employmentQueryFilter).get(0);

    	if(StringUtils.isEmpty(existingEmployment.getPerson().getCuil())) {
    	    //the Person is in database with null CUIL
    	    //Check if the provided CUIL is not already for another Person
    	    Person personAlreadyExisting = personService.findByCuil(modifiedEmployment.getPerson().getCuil());
    	    if(personAlreadyExisting!=null) {
    	        String errorMessageString = "Ya existe el CUIL "+personAlreadyExisting.getCuil()+" para el agente "+personAlreadyExisting.getApellidoNombre();
    	        Map<String, Object> responseMap = new ResponseMap<String>().mapError(errorMessageString);
    	        String serializedResponse = objectMapper.writeValueAsString(responseMap);
    	        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.BAD_REQUEST);
    	    }
    	}
    	
    	//set the desired attributes
    	existingEmployment.getPerson().setApellidoNombre(modifiedEmployment.getPerson().getApellidoNombre());
    	existingEmployment.getPerson().setCuil(modifiedEmployment.getPerson().getCuil());
    	
    	OccupationalGroup occupationalGroup = null;
    	if( modifiedEmployment.getOccupationalGroup() != null ) { 
    	    OccupationalGroupQueryFilter occupationalGroupQueryFilter = new OccupationalGroupQueryFilter();
    	    occupationalGroupQueryFilter.setCode(modifiedEmployment.getOccupationalGroup().getCode());
    	    occupationalGroup = occupationalGroupService.find(occupationalGroupQueryFilter).get(0);
    	}
    	
    	existingEmployment.setOccupationalGroup(occupationalGroup);
    	
    	//save the person. This is not necessary if we configure cascade save-update 
    	personService.saveOrUpdate(existingEmployment.getPerson());
    	
    	
    	//save the employment
    	employmentService.saveOrUpdate(existingEmployment);
        /*if (employment.getFirstName().length() <= 3 || employment.getLastName().length() <= 3) {
            throw new IllegalArgumentException("moreThan3Chars");
        }
        personService.addPerson(employment);*/
       // return new ResponseMessage(ResponseMessage.Type.success, "personAdded");


        
		Map<String, Object> responseMap = new ResponseMap<Employment>().mapOK(existingEmployment, "Employment saved");

		String serializedResponse = objectMapper.writeValueAsString(responseMap);


		
				
		return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);
    }

    
    @RequestMapping(value = "/rest/employments/findOccupationalGroups", method = RequestMethod.GET)
    public ResponseEntity<String> occupationalGroupsSearch() throws JsonProcessingException {
        log.info("Start findOccupationalGroups.");     
    
        OccupationalGroupQueryFilter occupationalGroupQueryFilter = new OccupationalGroupQueryFilter();
        occupationalGroupQueryFilter.setOnlyLeafsOccupationalGroups(true);
        List<OccupationalGroup> allLeafsOccupationalGroups = occupationalGroupService.find(occupationalGroupQueryFilter);


        Map<String, Object> responseMap = new ResponseMap<OccupationalGroup>().mapOK(allLeafsOccupationalGroups);

        String serializedResponse = objectMapper.writeValueAsString(responseMap);
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json;charset=utf-8");

        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

    }

    @RequestMapping(value = "/rest/departments/{departmentId}/employmentTransfers", method = RequestMethod.POST)
    public ResponseEntity<String> createEmploymentTransfer(@PathVariable String departmentId,@RequestBody EmploymentTransferImpl employmentTransfer) throws JsonProcessingException{
        
        /*if(true) {
            throw new RuntimeException("Error de prueba");
        }*/
                
        
        //retrieve the existing employments
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        employmentQueryFilter.setEmploymentId(employmentTransfer.getEmploymentToTransfer().getId());
        
        //TODO check if the employment exists
        Employment existingEmployment = employmentService.findEmployments(employmentQueryFilter).get(0);

        
        SubDepartment destination = subDepartmentService.findById(employmentTransfer.getDestination().getId());
        
        employmentCreditsEntriesService.transferEmployee(existingEmployment.getPerson(), 
                                                         existingEmployment.getSubDepartment(),
                                                         destination
                                                         );
        

        /*
        //set the desired attributes
        existingEmployment.getPerson().setApellidoNombre(modifiedEmployment.getPerson().getApellidoNombre());
        existingEmployment.getPerson().setCuil(modifiedEmployment.getPerson().getCuil());
        
        OccupationalGroup occupationalGroup = null;
        if( modifiedEmployment.getOccupationalGroup() != null ) { 
            OccupationalGroupQueryFilter occupationalGroupQueryFilter = new OccupationalGroupQueryFilter();
            occupationalGroupQueryFilter.setCode(modifiedEmployment.getOccupationalGroup().getCode());
            occupationalGroup = occupationalGroupService.find(occupationalGroupQueryFilter).get(0);
        }
        
        existingEmployment.setOccupationalGroup(occupationalGroup);
        
        //save the person. This is not necessary if we configure cascade save-update 
        personService.saveOrUpdate(existingEmployment.getPerson());
        
        
        //save the employment
        employmentService.saveOrUpdate(existingEmployment);*/
        /*if (employment.getFirstName().length() <= 3 || employment.getLastName().length() <= 3) {
            throw new IllegalArgumentException("moreThan3Chars");
        }
        personService.addPerson(employment);*/
       // return new ResponseMessage(ResponseMessage.Type.success, "personAdded");


        
        Map<String, Object> responseMap = new ResponseMap<EmploymentTransfer>().mapOK(employmentTransfer, "transfer completed");

        String serializedResponse = objectMapper.writeValueAsString(responseMap);

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
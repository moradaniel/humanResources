package org.dpi.empleo;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionController;
import org.dpi.web.response.StatusResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class EmploymentController {

	private EmploymentCreditsEntriesService employmentCreditsEntriesService;
	
	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "occupationalGroupService")
	private OccupationalGroupService occupationalGroupService;

	
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;



	@Inject
	public EmploymentController(EmploymentCreditsEntriesService employmentCreditsEntriesService) {
		this.employmentCreditsEntriesService = employmentCreditsEntriesService;
	}


	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}

	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
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

	@RequestMapping(value = "/empleos/{id}/baja", method = RequestMethod.GET)
	public String baja(@PathVariable Long id, Model model) {
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setEmpleoId(id.toString());

		List<Empleo> empleos = employmentService.find(empleoQueryFilter);

		Empleo empleoADarDeBaja = empleos.get(0);


		employmentCreditsEntriesService.darDeBaja(empleoADarDeBaja);

		Empleo empleoDadoDeBaja = empleoADarDeBaja;


		//buscar los empleos de la reparticion
		empleoQueryFilter = new EmploymentQueryFilter();

		Reparticion reparticion = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());

		empleos = employmentService.find(empleoQueryFilter);
		model.addAttribute("empleos", empleos);

		return "redirect:/reparticiones/" + reparticion.getId();
	}

	@RequestMapping(value = "/empleos/{id}/promoteEmployment", method = RequestMethod.GET)
	public String setupFormCambiarCategory(
			HttpServletRequest request, 
			HttpServletResponse response,
			@PathVariable Long id, Model model) {


		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		if (reparticion != null){

			EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
			empleoQueryFilter.setEmpleoId(id.toString());

			List<Empleo> empleos = employmentService.find(empleoQueryFilter);

			Empleo empleoActual = empleos.get(0);

			if(empleoActual.getCentroSector().getReparticion().getId().longValue()!=reparticion.getId().longValue()){

				return "redirect:/reparticiones/reparticion/showEmpleos";
			}

			model.addAttribute("empleoActual", empleoActual);
			model.addAttribute("availableCategoriesForPromotion", employmentService.getAvailableCategoriesForPromotion(empleoActual));

		}
		return "movimientos/promotePersonForm";
	}





	@RequestMapping(value = "/empleos/updateAscenso", method = RequestMethod.POST)
	public String updateAscensoFromForm(HttpServletRequest request, Model model) throws Exception {



		String idEmpleoActual = ServletRequestUtils.getStringParameter(request, "idEmpleoActual");

		String proposedCategoryCode = ServletRequestUtils.getStringParameter(request, "proposedCategoryCode");

		if (!StringUtils.hasText(proposedCategoryCode)){
			return "redirect:/empleos/"+idEmpleoActual+"/cambiarCategory";
		}


		/*if(bindingResult.hasErrors()) { 
			model.addAttribute("accountVO", accountVO); 
			return VIEW_PAGE;
		}*/

		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setEmpleoId(idEmpleoActual.toString());

		List<Empleo> empleos = employmentService.find(empleoQueryFilter);

		Empleo empleoActual = empleos.get(0);

		employmentCreditsEntriesService.promotePerson(empleoActual, proposedCategoryCode);

		return "redirect:/reparticiones/reparticion/showEmpleos";
	}

	
	@RequestMapping(value = "/empleos/proposeNewEmploymentForm", method = RequestMethod.POST)
	public String proposeNewEmploymentFromForm(HttpServletRequest request, Model model) throws Exception {



		//String idEmpleoActual = ServletRequestUtils.getStringParameter(request, "idEmpleoActual");

		String centroSectorId = ServletRequestUtils.getStringParameter(request, "centroSectorId");
		String proposedCategoryCode = ServletRequestUtils.getStringParameter(request, "proposedCategoryCode");

		if (!StringUtils.hasText(centroSectorId) || !StringUtils.hasText(proposedCategoryCode)){
			return "redirect:/empleos/proposeNewEmploymentForm";
		}


		/*if(bindingResult.hasErrors()) { 
			model.addAttribute("accountVO", accountVO); 
			return VIEW_PAGE;
		}*/


		employmentCreditsEntriesService.proposeNewEmployment(proposedCategoryCode, Long.parseLong(centroSectorId));

		return "redirect:/reparticiones/reparticion/showEmpleos";
	}






	@RequestMapping(value = "/empleos/altaEmpleoForm", method = RequestMethod.GET)
	public String setupFormAltaEmpleo(Model model) {
		/*if(!model.containsAttribute("nuevoEmpleo")){


			// Create initial object
			Empleo nuevoEmpleo = new EmpleoImpl();


			// Add to model so it can be display in view
			model.addAttribute("nuevoEmpleo", nuevoEmpleo);
		}*/

		/*
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setEmpleoId(id.toString());

		List<Empleo> empleos = empleoService.find(empleoQueryFilter);

		Empleo empleoActual = empleos.get(0);

		Agente agenteACambiarCategoria = empleoActual.getAgente();


		model.addAttribute("empleoActual", empleoActual);
		model.addAttribute("categoriasDisponiblesParaAscenso", getCategoriasDisponiblesParaAscenso(empleoActual));
		 */


		/*empleoService.darDeBaja(empleoACambiarCategoria);

		Empleo empleoDadoDeBaja = empleoACambiarCategoria;

		//buscar los empleos de la reparticion
		empleoQueryFilter = new EmpleoQueryFilter();

		Reparticion reparticion = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());

		empleos = empleoService.find(empleoQueryFilter);*/


		return "empleos/altaEmpleoForm";
	}

	
	@RequestMapping(value = "/empleos/proposeNewEmploymentForm", method = RequestMethod.GET)
	public String setupFormIngresarPropuestaAgente(
													HttpServletRequest request, 
													HttpServletResponse response,
													Model model) {
		/*if(!model.containsAttribute("nuevoEmpleo")){


			// Create initial  object
			Empleo nuevoEmpleo = new EmpleoImpl();


			// Add to model so it can be display in view
			model.addAttribute("nuevoEmpleo", nuevoEmpleo);
		}*/

		/*
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setEmpleoId(id.toString());

		List<Empleo> empleos = empleoService.find(empleoQueryFilter);

		Empleo empleoActual = empleos.get(0);

		Agente agenteACambiarCategoria = empleoActual.getAgente();


		model.addAttribute("empleoActual", empleoActual);
		model.addAttribute("categoriasDisponiblesParaAscenso", getCategoriasDisponiblesParaAscenso(empleoActual));
		 */


		/*empleoService.darDeBaja(empleoACambiarCategoria);

		Empleo empleoDadoDeBaja = empleoACambiarCategoria;

		//buscar los empleos de la reparticion
		empleoQueryFilter = new EmpleoQueryFilter();

		Reparticion reparticion = empleoDadoDeBaja.getCentroSector().getReparticion();
		empleoQueryFilter.setReparticionId(reparticion.getId().toString());

		empleos = empleoService.find(empleoQueryFilter);*/

		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);
		
		
		List<CentroSector> centroSectoresReparticion = centroSectorService.findCentroSectores(reparticion);
		
		model.addAttribute("centroSectoresDeReparticion", centroSectoresReparticion);
		
		return "empleos/proposeNewEmploymentForm";
	}
	
	@RequestMapping(value="/empleos/crearEmpleo", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse crearEmpleoFromForm(
			@RequestParam String idAgente,
			@RequestParam String idCentroSector){

		//User newUser = new User(username, password, firstName, lastName, new Role(role));
		//Boolean result = service.create(newUser);
		boolean result=true;

		StatusResponse response = new StatusResponse();
		response.setSuccess(result);
		if(result){
			response.setMessage("Empleo dado de alta con exito");
		}else{
			response.setMessage("Error al crear empleo");

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
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

}
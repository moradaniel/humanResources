package org.dpi.movimientoCreditos;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.empleo.EmploymentService;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.reparticion.Reparticion;
import org.dpi.reparticion.ReparticionController;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MovimientoCreditosController {
	
	private MovimientoCreditosService movimientoCreditosService;
	
	private EmploymentService employmentService;

	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
	    binder.setAutoGrowNestedPaths(false);
	}


	@Inject
	public MovimientoCreditosController(MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
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


	public void setEmploymentService(EmploymentService empleoService) {
		this.employmentService = empleoService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}


	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}
	
	
	@RequestMapping(value = "/reparticiones/movimientos/{id}/setupFormCambiarEstadoMovimientoCreditos", method = RequestMethod.GET)
	public String setupFormCambiarEstadoMovimientoCreditos(@PathVariable Long id, Model model){  
			      
          
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		
		movimientoCreditosQueryFilter.setId(id);
		
		
		List<MovimientoCreditos> movimientos = movimientoCreditosService.find(movimientoCreditosQueryFilter);

		MovimientoCreditos movimientoCreditos = movimientos.get(0);
          
		MovimientoCreditosForm movimientoCreditosForm = new MovimientoCreditosForm(movimientoCreditos);
		
		
		model.addAttribute("movimientoCreditosForm", movimientoCreditosForm);
		model.addAttribute("grantedStatuses", GrantedStatus.values());

        
        return "movimientos/cambioEstadoMovimientoForm";
    }
	
	
	@RequestMapping(value = "/movimientos/cambiarEstadoMovimiento", method = RequestMethod.POST)
	public String processFormCambiarEstadoMovimiento(HttpServletRequest request,
													@ModelAttribute("movimientoCreditosForm") MovimientoCreditosForm movimientoCreditosForm,
													Model model,
													BindingResult result) throws Exception {
		

		//retrieve the movimientoCreditos from database
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.setId(movimientoCreditosForm.getMovimientoCreditosId());
				
		List<MovimientoCreditos> movimientos = movimientoCreditosService.find(movimientoCreditosQueryFilter);
		MovimientoCreditos movimientoCreditos = movimientos.get(0);

          
		movimientoCreditosService.changeGrantedStatus(movimientoCreditos, movimientoCreditosForm.getGrantedStatus());  
          
        //String message = "Team was successfully edited.";  
        //modelAndView.addObject("message", message);  

        String creditsPeriodName =  movimientoCreditos.getCreditsPeriod().getName();
		return "redirect:/reparticiones/reparticion/showMovimientos/"+creditsPeriodName;
    }  
	
	
	/**
	 * Usado para AngularJS
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/reparticiones/reparticion/movimientos", method = RequestMethod.GET)
    @ResponseBody
	public List<MovimientoCreditosDTO> getMovimientos(
			HttpServletRequest request, 
			HttpServletResponse response/*,
			@PathVariable Long reparticionId, Model model*/) {

		// get the current reparticion in the session
		final Reparticion reparticion = ReparticionController.getCurrentReparticion(request);

		List<MovimientoCreditosDTO> movimientosCreditosDTO = new ArrayList<MovimientoCreditosDTO>();
		if (reparticion != null){


			EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
			empleoQueryFilter.setReparticionId(reparticion.getId().toString());

			MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
			movimientoCreditosQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.CargaInicialAgenteExistente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.BajaAgente);
			movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);

			List<MovimientoCreditos> movimientoCreditosReparticion = movimientoCreditosService.find(movimientoCreditosQueryFilter);
			
			Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Account currenUser = (Account)accountObj;
					
			List<MovimientoCreditosVO> movimientoCreditosVOReparticion = movimientoCreditosService.buildMovimientoCreditosVO(movimientoCreditosReparticion,currenUser);
			
			for(MovimientoCreditosVO movimientoCreditosAscensoVO :movimientoCreditosVOReparticion){
				MovimientoCreditosDTO movimientoCreditosDTO = new MovimientoCreditosDTO();
				movimientoCreditosDTO.setName(movimientoCreditosAscensoVO.getMovimientoCreditos().getEmpleo().getAgente().getApellidoNombre());
				movimientosCreditosDTO.add(movimientoCreditosDTO);
			}
					
			//model.addAttribute("movimientos", movimientoCreditosVOReparticion);
			
			//creditos disponibles
			//long creditosDisponibles = administradorCreditosService.getCreditosDisponiblesSegunSolicitado(reparticion.getId());
			
			//model.addAttribute("creditosDisponibles", creditosDisponibles);
			
		}
		return movimientosCreditosDTO;
	}
	
	
	
    @RequestMapping(value = "/reparticiones/movimientos/processCambiarMultipleEstadoMovimiento", method = RequestMethod.POST)
    public String processCambiarMultipleEstadoMovimiento(@ModelAttribute("cambiosMultiplesEstadoMovimientosForm") CambiosMultiplesEstadoMovimientosForm cambiosMultiplesEstadoMovimientosForm) {

        List<MovimientoCreditos> movimientos = cambiosMultiplesEstadoMovimientosForm.getMovimientos();
         
        for(MovimientoCreditos submittedMovimiento :movimientos){
        	if(submittedMovimiento!=null){
        		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
        		movimientoCreditosQueryFilter.setId(submittedMovimiento.getId());
        		List<MovimientoCreditos> listMovimientoCreditos = movimientoCreditosService.find(movimientoCreditosQueryFilter);
        		MovimientoCreditos movimientoCreditos = listMovimientoCreditos.get(0);
        		if(movimientoCreditos.getGrantedStatus()!=submittedMovimiento.getGrantedStatus()){
        			//movimientoCreditos.setGrantedStatus(submittedMovimiento.getGrantedStatus());
        			//movimientoCreditosService.saveOrUpdate(movimientoCreditos);
        			movimientoCreditosService.changeGrantedStatus(movimientoCreditos,submittedMovimiento.getGrantedStatus());
        		}
        		
        	}

        }
         
        return "redirect:/reparticiones/reparticion/showMovimientos/"+creditsPeriodService.getCurrentCreditsPeriodYear();

    }
	


}
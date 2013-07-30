package org.dpi.movimientoCreditos;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.empleo.EmpleoService;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MovimientoCreditosController {

	//private ReparticionService reparticionService;

	
	private MovimientoCreditosService movimientoCreditosService;
	
	//@Resource(name = "empleoService")
	private EmpleoService empleoService;

	

	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;

	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;

	
	


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

	public EmpleoService getEmpleoService() {
		return empleoService;
	}


	public void setEmpleoService(EmpleoService empleoService) {
		this.empleoService = empleoService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}


	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
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

		movimientoCreditos = movimientoCreditosForm.copyProperties(movimientoCreditos);
		
        //ModelAndView modelAndView = new ModelAndView("home"); 
          
		movimientoCreditosService.saveOrUpdate(movimientoCreditos);  
          
        //String message = "Team was successfully edited.";  
        //modelAndView.addObject("message", message);  

        
		return "redirect:/reparticiones/reparticion/showMovimientos";
    }  


}
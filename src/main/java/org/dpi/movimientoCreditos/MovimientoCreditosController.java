package org.dpi.movimientoCreditos;

import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.empleo.EmpleoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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


	
	
	@RequestMapping(value = "/movimientos/{id}/setupFormCambiarEstadoMovimientoCreditos", method = RequestMethod.GET)
	public String setupFormCambiarEstadoMovimientoCreditos(@PathVariable Long id, Model model){  
			      
          
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		
		movimientoCreditosQueryFilter.setId(id);
		
		
		List<MovimientoCreditos> movimientos = movimientoCreditosService.find(movimientoCreditosQueryFilter);

		MovimientoCreditos movimientoCreditos = movimientos.get(0);
          

        
        return "movimientos/cambioEstadoMovimientoForm";
    }
	
	
	@RequestMapping(value="/movimientos/cambiarEstadoMovimiento/{id}", method=RequestMethod.POST)  
    public String cambiarEstadoMovimiento(@ModelAttribute MovimientoCreditos movimientoCreditos, @PathVariable Integer id, Model model) {  
          
        ModelAndView modelAndView = new ModelAndView("home");  
          
        //teamService.updateTeam(team);  
          
        String message = "Team was successfully edited.";  
        modelAndView.addObject("message", message);  
          

        
		return "redirect:/reparticiones/reparticion/showMovimientos";
    }  


}
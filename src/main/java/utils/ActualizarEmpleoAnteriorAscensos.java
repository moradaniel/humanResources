package utils;

import java.util.List;

import org.dpi.agente.AgenteService;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmpleoService;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditosQueryFilter;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.dpi.reparticion.ReparticionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizarEmpleoAnteriorAscensos {
	
	
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	ReparticionService reparticionService;
	
	AgenteService agenteService;
	
	
	
	EmpleoService empleoService;
		
	MovimientoCreditosService movimientoCreditosService;

	//AdministradorCreditosService administradorCreditosService;
	

	
	TransactionTemplate transactionTemplate;
	
	public static void main( String[] args )
    {
		String[] contextFiles = new String[] {
				"classpath:spring/root-context.xml",
				"classpath:GenericDaoContext.xml",
				"classpath:SecurityContextGeneric.xml"
		};
		
		ApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);

		final ActualizarEmpleoAnteriorAscensos actualizador = new ActualizarEmpleoAnteriorAscensos();
		
		
		actualizador.setEmpleoService((EmpleoService)context.getBean("empleoService"));
		
		actualizador.setMovimientoCreditosService((MovimientoCreditosService)context.getBean("movimientoCreditosService"));
		
		
		//actualizador.setAdministradorCreditosService((AdministradorCreditosService)context.getBean("administradorCreditosService"));
		
		actualizador.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    actualizador.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				actualizador.actualizar();
				
			}
		});
		
    }

	/*
    public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService=administradorCreditosService;
		
	}

	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return this.administradorCreditosService;
	}*/

	
	/**
     * 
     */
	private void actualizar() throws RuntimeException{

		
		//obtener todos los movimientos de ascenso
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		
		List<MovimientoCreditos> movimientosAscenso = movimientoCreditosService.find(movimientoCreditosQueryFilter);
		for(MovimientoCreditos movimientoAscenso: movimientosAscenso){
			//buscar el empleo anterior al empleo del movimiento de ascenso
			Empleo empleoDelAscenso = movimientoAscenso.getEmpleo();
			Empleo empleoAnteriorDelAscenso = empleoService.findPreviousEmpleo(empleoDelAscenso);
			
			//al empleo del movimiento de ascenso setearle el empleo anterior
			empleoDelAscenso.setEmpleoAnterior(empleoAnteriorDelAscenso);
			
			//guardar empleo del ascenso
			empleoService.saveOrUpdate(empleoDelAscenso);
			
		}


	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	
	public EmpleoService getEmpleoService() {
		return empleoService;
	}

	public void setEmpleoService(EmpleoService empleoService) {
		this.empleoService = empleoService;
	}
	

	
	public MovimientoCreditosService getMovimientoCreditosService() {
		return movimientoCreditosService;
	}

	public void setMovimientoCreditosService(
			MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
	}
	



}

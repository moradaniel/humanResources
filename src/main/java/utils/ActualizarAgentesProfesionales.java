package utils;

import java.util.ArrayList;
import java.util.List;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteQueryFilter;
import org.dpi.agente.AgenteService;
import org.dpi.agente.CondicionAgente;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmploymentCreditsEntriesService;
import org.dpi.reparticion.ReparticionSearchInfo;
import org.dpi.reparticion.ReparticionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizarAgentesProfesionales {
	
	
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	ReparticionService reparticionService;
	
	AgenteService agenteService;
	
	
	
	EmploymentCreditsEntriesService employmentCreditsEntriesService;
		
	//MovimientoCreditosService movimientoCreditosService;

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

		final ActualizarAgentesProfesionales actualizador = new ActualizarAgentesProfesionales();
		
		actualizador.setReparticionService((ReparticionService)context.getBean("reparticionService"));
		actualizador.setAgenteService((AgenteService)context.getBean("agenteService"));
		
		actualizador.setEmploymentCreditsEntriesService((EmploymentCreditsEntriesService)context.getBean("employmentCreditsEntriesService"));
		
		//actualizador.setMovimientoCreditosService((MovimientoCreditosService)context.getBean("movimientoCreditosService"));
		
		
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

		int totalAgentesProfesionalesActivos = 0;
		int totalAgentesProfesionalesActivosAscendidosAutomaticamente = 0;
		
		//obtener todas las reparticiones
		
		List<ReparticionSearchInfo> reparticionSearchInfos = new ArrayList<ReparticionSearchInfo>();
		reparticionSearchInfos = reparticionService.findAllReparticiones();
		for(ReparticionSearchInfo reparticionSearchInfo: reparticionSearchInfos)
		{
			//por cada reparticion obtener los agentes activos y profesionales
			AgenteQueryFilter agenteQueryFilter = new AgenteQueryFilter();
			agenteQueryFilter.setReparticionId(reparticionSearchInfo.getReparticionId());
			//agenteQueryFilter.setEstadoAgente(EstadoAgente.ACTIVO);
			agenteQueryFilter.setCondicionAgente(CondicionAgente.Profesional);
			
			List<Agente> agentes = this.agenteService.find(agenteQueryFilter);
			totalAgentesProfesionalesActivos +=agentes.size();
			for(Agente agente: agentes){
				//si el agente no tiene movimientos de asenso pendientes y la categoria es menor a 21
				// ascenderlo a categoria 21
				
				Empleo empleoActivo = agente.getEmpleoActivo();
				Integer activeEmploymentCategoryCode = Integer.parseInt(empleoActivo.getCategory().getCode());
				
				if(true/*!empleoActivo.hasMovimientosAscensoPendientes() && codigoCategoryEmpleoActivo <21 */){
					employmentCreditsEntriesService.ascenderAgente( agente.getEmpleoActivo(), "21");
					totalAgentesProfesionalesActivosAscendidosAutomaticamente++;
				}
			}
			
		}
		
		LOGGER.info("totalAgentesProfesionalesActivos: "+totalAgentesProfesionalesActivos);
		LOGGER.info("totalAgentesProfesionalesActivosAscendidosAutomaticamente: "+totalAgentesProfesionalesActivosAscendidosAutomaticamente);

	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	
	public EmploymentCreditsEntriesService getEmploymentCreditsEntriesService() {
		return employmentCreditsEntriesService;
	}

	public void setEmploymentCreditsEntriesService(EmploymentCreditsEntriesService employmentCreditsEntriesService) {
		this.employmentCreditsEntriesService = employmentCreditsEntriesService;
	}
	
	/*
	
	public MovimientoCreditosService getMovimientoCreditosService() {
		return movimientoCreditosService;
	}

	public void setMovimientoCreditosService(
			MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
	}*/
	
	public ReparticionService getReparticionService() {
		return reparticionService;
	}

	public void setReparticionService(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}

	public AgenteService getAgenteService() {
		return agenteService;
	}

	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}


}

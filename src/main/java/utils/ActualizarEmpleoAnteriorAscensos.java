package utils;

import java.util.List;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.department.DepartmentService;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentService;
import org.dpi.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizarEmpleoAnteriorAscensos {
	
	
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	DepartmentService reparticionService;
	
	PersonService agenteService;
	
	
	
	EmploymentService employmentService;
		
	CreditsEntryService creditsEntryService;


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
		
		
		actualizador.setEmploymentService((EmploymentService)context.getBean("employmentService"));
		
		actualizador.setCreditsEntryService((CreditsEntryService)context.getBean("creditsEntryService"));
		
		
		actualizador.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    actualizador.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				actualizador.actualizar();
				
			}
		});
		
    }
	
	/**
     * 
     */
	private void actualizar() throws RuntimeException{

		
		//obtener todos los movimientos de ascenso
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		
		List<CreditsEntry> movimientosAscenso = creditsEntryService.find(creditsEntryQueryFilter);
		for(CreditsEntry movimientoAscenso: movimientosAscenso){
			//buscar el empleo anterior al empleo del movimiento de ascenso
			Employment empleoDelAscenso = movimientoAscenso.getEmployment();
			//Employment empleoAnteriorDelAscenso = employmentService.findPreviousEmployment(empleoDelAscenso);
			
			//al empleo del movimiento de ascenso setearle el empleo anterior
			//empleoDelAscenso.setPreviousEmployment(empleoAnteriorDelAscenso);
			
			//guardar empleo del ascenso
			employmentService.saveOrUpdate(empleoDelAscenso);
			
		}


	}
	
	public TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}
	
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}

	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}
	

	
	public CreditsEntryService getCreditsEntryService() {
		return creditsEntryService;
	}

	public void setCreditsEntryService(
			CreditsEntryService creditsEntryService) {
		this.creditsEntryService = creditsEntryService;
	}
	



}

package utils;

import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.employment.EmploymentService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizadorCantidadCreditosIngresosAscensos {
	
	
	EmploymentService employmentService;
	
	CreditsEntryService creditsEntryService;

	CreditsManagerService creditsManagerServiceService;
	
	
	TransactionTemplate transactionTemplate;
	
	public static void main( String[] args )
    {
		String[] contextFiles = new String[] {
				"classpath:spring/root-context.xml",
				"classpath:GenericDaoContext.xml",
				"classpath:SecurityContextGeneric.xml"
		};
		
		ApplicationContext context = new ClassPathXmlApplicationContext(contextFiles);

		final ActualizadorCantidadCreditosIngresosAscensos actualizador = new ActualizadorCantidadCreditosIngresosAscensos();
		
		actualizador.setEmploymentService((EmploymentService)context.getBean("employmentService"));
		actualizador.setCreditsEntryService((CreditsEntryService)context.getBean("creditsEntryService"));
		
		
		actualizador.setCreditsManagerService((CreditsManagerService)context.getBean("creditsManagerService"));
		
		actualizador.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    actualizador.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				actualizador.actualizar();
				
			}
		});
		
    }

    public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerServiceService=creditsManagerService;
		
	}

	
	public CreditsManagerService getCreditsManagerService() {
		return this.creditsManagerServiceService;
	}

	
	/**
     * 
     */
	private void actualizar() throws RuntimeException{
		
		this.creditsEntryService.actualizarCreditosPorAscenso();
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

package utils;

import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.empleo.EmploymentService;
import org.dpi.movimientoCreditos.MovimientoCreditosService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizadorCantidadCreditosIngresosAscensos {
	
	
	EmploymentService employmentService;
	
	MovimientoCreditosService movimientoCreditosService;

	AdministradorCreditosService administradorCreditosService;
	
	
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
		
		actualizador.setEmploymentService((EmploymentService)context.getBean("empleoService"));
		actualizador.setMovimientoCreditosService((MovimientoCreditosService)context.getBean("movimientoCreditosService"));
		
		
		actualizador.setAdministradorCreditosService((AdministradorCreditosService)context.getBean("administradorCreditosService"));
		
		actualizador.setTransactionTemplate((TransactionTemplate)context.getBean("transactionTemplate"));
		
		
	    actualizador.getTransactionTemplate().execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status){
				actualizador.actualizar();
				
			}
		});
		
    }

    public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService=administradorCreditosService;
		
	}

	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return this.administradorCreditosService;
	}

	
	/**
     * 
     */
	private void actualizar() throws RuntimeException{
		
		this.movimientoCreditosService.actualizarCreditosPorAscenso();
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

	public void setEmploymentService(EmploymentService empleoService) {
		this.employmentService = empleoService;
	}
	
	public MovimientoCreditosService getMovimientoCreditosService() {
		return movimientoCreditosService;
	}

	public void setMovimientoCreditosService(
			MovimientoCreditosService movimientoCreditosService) {
		this.movimientoCreditosService = movimientoCreditosService;
	}


}

package utils;

import java.util.ArrayList;
import java.util.List;

import org.dpi.department.DepartmentSearchInfo;
import org.dpi.department.DepartmentService;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentCreditsEntriesService;
import org.dpi.person.Person;
import org.dpi.person.PersonQueryFilter;
import org.dpi.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

public class ActualizarAgentesProfesionales {
	
	
	Logger LOGGER = LoggerFactory.getLogger(this.getClass());
	
	DepartmentService departmentService;
	
	PersonService personService;
	
	
	
	EmploymentCreditsEntriesService employmentCreditsEntriesService;
		
	
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
		
		actualizador.setDepartmentService((DepartmentService)context.getBean("departmentService"));
		actualizador.setAgenteService((PersonService)context.getBean("agenteService"));
		
		actualizador.setEmploymentCreditsEntriesService((EmploymentCreditsEntriesService)context.getBean("employmentCreditsEntriesService"));
		
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

		int totalAgentesProfesionalesActivos = 0;
		int totalAgentesProfesionalesActivosAscendidosAutomaticamente = 0;
		
		//obtener todas las departments
		
		List<DepartmentSearchInfo> departmentSearchInfos = new ArrayList<DepartmentSearchInfo>();
		departmentSearchInfos = departmentService.findAllDepartments();
		for(DepartmentSearchInfo departmentSearchInfo: departmentSearchInfos)
		{
			//por cada department obtener los agentes activos y profesionales
			PersonQueryFilter agenteQueryFilter = new PersonQueryFilter();
			agenteQueryFilter.setDepartmentId(departmentSearchInfo.getDepartmentId());
			//agenteQueryFilter.setEstadoAgente(EstadoAgente.ACTIVO);
			//agenteQueryFilter.setPersonCondition(PersonCondition.Profesional);
			
			List<Person> agentes = this.personService.find(agenteQueryFilter);
			totalAgentesProfesionalesActivos +=agentes.size();
			for(Person person: agentes){
				//si el agente no tiene movimientos de asenso pendientes y la categoria es menor a 21
				// ascenderlo a categoria 21
				
				Employment activeEmployment = null;
				Integer activeEmploymentCategoryCode = Integer.parseInt(activeEmployment.getCategory().getCode());
				
				if(true/*!activeEmployment.hasMovimientosAscensoPendientes() && codigoCategoryactiveEmployment <21 */){
					employmentCreditsEntriesService.promotePerson( activeEmployment, "21");
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
	
	public CreditsEntryService getCreditsEntryService() {
		return creditsEntryService;
	}

	public void setCreditsEntryService(
			CreditsEntryService creditsEntryService) {
		this.creditsEntryService = creditsEntryService;
	}*/
	
	public DepartmentService getDepartmentService() {
		return departmentService;
	}

	public void setDepartmentService(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}

	public PersonService getAgenteService() {
		return personService;
	}

	public void setAgenteService(PersonService agenteService) {
		this.personService = agenteService;
	}


}

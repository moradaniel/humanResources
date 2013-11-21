package org.dpi.employment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dpi.category.Category;
import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryImpl;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.person.Person;
import org.dpi.person.PersonImpl;
import org.dpi.person.PersonService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



public class EmploymentCreditsEntriesServiceImpl implements EmploymentCreditsEntriesService
{
	
	private static Logger log = LoggerFactory.getLogger(EmploymentCreditsEntriesServiceImpl.class);

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "creditsEntryService")
	private CreditsEntryService creditsEntryService;
	
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	private ApplicationContext applicationContext;
	
	public EmploymentCreditsEntriesServiceImpl() {

	}

	@Override
	public void promotePerson(Employment currentEmployment, String newCategoryCode){
		
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to promote person:");/*+ entry.getId()+
					" Type:"+entry.getTipoCreditsEntry().name()+
					" Agent name:"+ entry.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		Person employee = currentEmployment.getPerson();
		List<Long> employeeIds = new ArrayList<Long>();
		employeeIds.add(employee.getId());
		Set<Long>resultEmployeeIds = creditsEntryService.haveMovimientosSolicitados(employeeIds, currentEmployment.getCentroSector().getReparticion().getId(), currentCreditsPeriod.getId());
		
		if(resultEmployeeIds.contains(employee.getId())){
			return;
		}

		Category newCategory = categoryService.findByCode(newCategoryCode);
		Employment newEmployment = new EmploymentImpl();
		newEmployment.setPerson(currentEmployment.getPerson());
		newEmployment.setCategory(newCategory);
		newEmployment.setCentroSector(currentEmployment.getCentroSector());
		newEmployment.setOccupationalGroup(currentEmployment.getOccupationalGroup());
		newEmployment.setFechaInicio(new Date());
		newEmployment.setStatus(EmploymentStatus.PENDIENTE);
		
		
		//crear un entry de tipo ascenso 
		CreditsEntryImpl entryAscenso = new CreditsEntryImpl();
		entryAscenso.setCreditsEntryType(CreditsEntryType.AscensoAgente);
		int cantidadCreditosPorAscenso = administradorCreditosService.getCreditosPorAscenso(employee.getCondition(),currentEmployment.getCategory().getCode(),newCategoryCode);
		entryAscenso.setGrantedStatus(GrantedStatus.Solicitado);
		entryAscenso.setCreditsPeriod(currentCreditsPeriod);

		
		entryAscenso.setCantidadCreditos(cantidadCreditosPorAscenso);
		
		
		//setear empleo a entry  y agregar entry a empleo
		newEmployment.addCreditsEntry(entryAscenso);
		
		entryAscenso.setEmployment(newEmployment);

		//set previous employment
		newEmployment.setPreviousEmployment(currentEmployment);
		
		
		//guardar entry y empleo
		employmentService.saveOrUpdate(newEmployment);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: promote person - "+
					" centrosector: "+newEmployment.getCentroSector().toString()+
					" Employee :"+ newEmployment.getPerson().toString()+
					" creditsEntry: "+entryAscenso.toString());	
		}
	}
	
	@Override
	public void darDeBaja(Employment empleo) {
		
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to dar de baja:");/*+ entry.getId()+
					" Type:"+entry.getTipoCreditsEntry().name()+
					" Agent name:"+ entry.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}	
		
		
		//ponerle fecha fin la fecha actual
		empleo.setFechaFin(new Date());
		empleo.setStatus(EmploymentStatus.BAJA);
		
		//crear un entry de tipo baja 
		CreditsEntryImpl entryBaja = new CreditsEntryImpl();
		entryBaja.setCreditsEntryType(CreditsEntryType.BajaAgente);
		int cantidadCreditosPorBaja = administradorCreditosService.getCreditosPorBaja(empleo.getCategory().getCode());

		
		entryBaja.setCantidadCreditos(cantidadCreditosPorBaja);
		
		
		//setear empleo a entry  y agregar entry a empleo
		empleo.addCreditsEntry(entryBaja);
		
		entryBaja.setEmployment(empleo);
		
		//guardar entry y empleo
		employmentService.saveOrUpdate(empleo);
		
	}
	
	
	@Override
	public void proposeNewEmployment(String proposedCategoryCode,Long centroSectorId) {
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to ingresar propuesta agente:");/*+ entry.getId()+
					" Type:"+entry.getTipoCreditsEntry().name()+
					" Agent name:"+ entry.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		// crear agente nn
		Person nuevoAgentePropuesto = createPendingAgent();
		
		personService.save(nuevoAgentePropuesto);
		
		//crear empleo
		Employment nuevoEmpleoPropuesto = new EmploymentImpl();
		nuevoEmpleoPropuesto.setPerson(nuevoAgentePropuesto);
		//set proposed category to the employment
		nuevoEmpleoPropuesto.setCategory(categoryService.findByCode(proposedCategoryCode));
		//al empleo ponerlo en status pendiente
		nuevoEmpleoPropuesto.setStatus(EmploymentStatus.PENDIENTE);
		
		nuevoEmpleoPropuesto.setFechaInicio(new Date());
		
		//buscar centro sector
		CentroSector centroSector = centroSectorService.findById(centroSectorId);
		nuevoEmpleoPropuesto.setCentroSector(centroSector);
		
		//Crear entry de ingreso
		CreditsEntry creditsEntryIngreso = new CreditsEntryImpl();
		creditsEntryIngreso.setCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryIngreso.setGrantedStatus(GrantedStatus.Solicitado);
		creditsEntryIngreso.setCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod());
		
		creditsEntryIngreso.setEmployment(nuevoEmpleoPropuesto);
		nuevoEmpleoPropuesto.addCreditsEntry(creditsEntryIngreso);
		
		int creditosPorIngreso = administradorCreditosService.getCreditosPorIngreso(proposedCategoryCode);
		creditsEntryIngreso.setCantidadCreditos(creditosPorIngreso);
		
		employmentService.save(nuevoEmpleoPropuesto);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: ingresar propuesta agente - "+
					" centrosector: "+centroSector.toString()+
					" Employee :"+ nuevoAgentePropuesto.toString()+
					" creditsEntry: "+creditsEntryIngreso.toString());	
		}
		
	}
	
	
	private Person createPendingAgent(){
		PersonImpl newAgente = new PersonImpl();
		newAgente.setApellidoNombre("Ingreso Nuevo Propuesto");
		newAgente.setCuil("");
		return newAgente;
	}
	
	
	
	
	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	@Override
	public List<EmploymentVO> buildEmploymentsVO(List<Employment> activeEmployments, Long reparticionId,
			Account currenUser) {
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		List<Long> posiblesAgentesIds = new ArrayList<Long>();
		for(Employment employment:activeEmployments){
			posiblesAgentesIds.add(employment.getPerson().getId());
		}
		
		Set<Long> personsIds = new HashSet<Long>();
		if(canAccountPromotePerson(currenUser)){
			personsIds = creditsEntryService.haveMovimientosSolicitados(posiblesAgentesIds, reparticionId, currentCreditsPeriod.getId());
		}
		
		List<EmploymentVO> employmentsVO = new ArrayList<EmploymentVO>();
		for(Employment employment:activeEmployments){
			EmploymentVO employmentVO = new EmploymentVO();
			employmentVO.setEmployment(employment);
			
			employmentVO.setCanAccountPromotePerson(canAccountPromotePerson(currenUser) && !personsIds.contains(employment.getPerson().getId()));

			employmentsVO.add(employmentVO);
		}

			
		return employmentsVO;
	}
	
	public static boolean canAccountPromotePerson(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "CREATE")){
			return true;
		}
		return false;
	}
	
	public static boolean canAccountProposeNewEmployment(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "CREATE")){
			return true;
		}
		return false;
	}

}

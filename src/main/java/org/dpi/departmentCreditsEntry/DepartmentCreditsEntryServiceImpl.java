package org.dpi.departmentCreditsEntry;

import java.util.List;

import javax.annotation.Resource;

import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.departmentCreditsEntry.DepartmentCreditsEntry.GrantedStatus;
import org.dpi.employment.EmploymentService;
import org.dpi.person.PersonService;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



public class DepartmentCreditsEntryServiceImpl implements DepartmentCreditsEntryService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "departmentCreditsEntryDao")
	private DepartmentCreditsEntryDao departmentCreditsEntryDao;

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	@Resource(name = "creditsEntryService")
	private CreditsEntryService creditsEntryService;
	

	private ApplicationContext applicationContext;
	
	public DepartmentCreditsEntryServiceImpl() {

	}

	
	public List<DepartmentCreditsEntry> find(DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter){
		
		return departmentCreditsEntryDao.find(creditsEntryQueryFilter);
	}
	
	public PageList<DepartmentCreditsEntry> findCreditsEntries(final DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter){
	    
	    return departmentCreditsEntryDao.findCreditsEntries(creditsEntryQueryFilter);
	}

	
	public void saveOrUpdate(final DepartmentCreditsEntry entry)
	{
		departmentCreditsEntryDao.saveOrUpdate(entry);
	}

	public void delete(DepartmentCreditsEntry entry)
	{
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser==null){
			throw new RuntimeException("User must exist to delete an entry");
		}
		
		log.info("================ user:"+currentUser.getName()+" attempting to delete creditsEntry: "+entry.toString());


		if(entry.getDepartmentCreditsEntryType().equals(DepartmentCreditsEntryType.ReassignedFromRetention)){
			/*
			EmploymentQueryFilter filter = new EmploymentQueryFilter();
			filter.setEmploymentId(entry.getEmployment().getId());
			Employment employment = employmentService.findEmployments(filter).get(0);
			
			employment.setEndDate(null);
			employment.setStatus(EmploymentStatus.ACTIVO);
			

			for(CreditsEntry anEntry :employment.getCreditsEntries() ) {
				if(anEntry.getId().longValue()==entry.getId().longValue()) {
					entry = anEntry;
					employment.getCreditsEntries().remove(anEntry);
					break;
				}
			}
			
			creditsEntryDao.delete(entry);
			
			employmentService.saveOrUpdate(employment);*/
			
			
		}else
			if(entry.getDepartmentCreditsEntryType().equals(CreditsEntryType.AscensoAgente)){
/*
				//delete entry 
				Employment employmentPendiente = entry.getEmployment();
				creditsEntryDao.delete(entry);
				//delete pending employment
				employmentService.delete(employmentPendiente);*/
			}else

				if(entry.getDepartmentCreditsEntryType().equals(CreditsEntryType.IngresoAgente)){

					/*Person personToBeDeleted = entry.getEmployment().getPerson();

					employmentService.delete(entry.getEmployment());

					personService.delete(personToBeDeleted);*/

				}


		log.info("================ user:"+currentUser.getName()+" Successfully performed: delete creditsEntry - "+ entry.toString());	


	}

	public DepartmentCreditsEntryDao getDepartmentCreditsEntryDao()
	{
		return departmentCreditsEntryDao;
	}


	public List<DepartmentCreditsEntry> findAll(){
		return this.departmentCreditsEntryDao.findAll();
	}
	

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	
	public boolean canAccountChangeCreditsEntryStatus(CreditsEntry creditsEntry, Account account) {
		return canChangeCreditsEntryStatus(account) && creditsEntry.canStatusBeChanged(creditsEntryService,creditsPeriodService);
	}
	
	/*
	public boolean canCreditsEntryStatusBeChanged(
			CreditsEntry creditsEntry) {
		
		//only creditsEntries of Active periods can be changed 
		if(creditsEntry.getCreditsPeriod().getStatus()==CreditsPeriod.Status.Closed){
		    //if the period is closed then check if the employmnet has subsequent entries
		    
			return false;
		}else

		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.BajaAgente){
			 return true;
		 }else
		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.CargaInicialAgenteExistente){
			 return false;
		 }else
		 
		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.IngresoAgente){
			 if(creditsEntry.getNumberOfCredits()==0){
				 return false;
			 }
		 }

		 
		 return true;
	}*/

	
	public static boolean canChangeCreditsEntryStatus(Account account, CreditsPeriod creditsPeriod) {
		if(creditsPeriod.getStatus()!=Status.Active){
			return false;
		}else
		if(canChangeCreditsEntryStatus(account)){
			return true;
		}
		return false;
	}

	public static boolean canChangeCreditsEntryStatus(Account account) {
		
		if(account.hasPermissions("Manage_CreditsEntries", "UPDATE_STATUS")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeletedByAccount(DepartmentCreditsEntry entry, Account account){
		
		return canDeleteCreditsEntry(account) && canCreditsEntryBeDeleted(entry);
		
	}
	
	public boolean canDeleteCreditsEntry(Account account) {
		
		if(account.hasPermissions("Manage_CreditsEntries", "DELETE")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeleted(DepartmentCreditsEntry creditsEntry){
		
				
		if(isCreditsEntryClosed(creditsEntry)){
			return false;
		}
		
		if(creditsEntry.getGrantedStatus()==GrantedStatus.Otorgado) {
			return false;
		}
		
		if(creditsEntry.getDepartmentCreditsEntryType()==DepartmentCreditsEntryType.ReassignedFromRetention){
			return false;
		}
		
		if(creditsEntry.getDepartmentCreditsEntryType()==DepartmentCreditsEntryType.ReassignedFromRetention){
			return false;
		}
		
		if(creditsEntry.getDepartmentCreditsEntryType()==DepartmentCreditsEntryType.ReassignedFromRetention && creditsEntry.getGrantedStatus()==GrantedStatus.Otorgado){
			return false;
		}
		
		
		if(creditsEntry.getDepartmentCreditsEntryType()==DepartmentCreditsEntryType.ReassignedFromRetention /*&& creditsEntry.getEmployment().isClosed()*/){
			return false;
		}
				
	
		return true;
	}

	private boolean isCreditsEntryClosed(DepartmentCreditsEntry creditsEntry) {
		
		if(creditsEntry.getCreditsPeriod().getStatus() == Status.Closed ){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void actualizarCreditosPorAscenso(){
		//obtener entries de tipo Ascenso
		/*CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		List<DepartmentCreditsEntry> creditsEntries = this.find(creditsEntryQueryFilter);
		for(DepartmentCreditsEntry creditsEntry :creditsEntries){
			//Obtener el employment del entry de tipo ascenso (EmpleoaActualizar)
			Employment employmentaActualizar = creditsEntry.getEmployment();
			//Obtener el employment anterior al EmpleoaActualizar (EmpleoAnterior)
			Employment employmentAnterior = employmentaActualizar.getPreviousEmployment();
			//get the category of the previous employment
			//Obtener los creditos por ascenso para la categoria anterior y la categoria actual
			int nuevaCantidaddeCreditos = creditsManagerService.getCreditosPorAscenso(employmentAnterior.getCategory().getCode(),
																						employmentaActualizar.getCategory().getCode());
			creditsEntry.setNumberOfCredits(nuevaCantidaddeCreditos);
			this.saveOrUpdate(creditsEntry);
			
	
		}*/
	}
	
	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}


	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}
	
	@Override
	public void changeGrantedStatus(DepartmentCreditsEntry entry, GrantedStatus newStatus){
		

		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			/*log.info("user:"+currentUser.getName()+" attempting to change granted status of entry:"+ entry.getId()+
					" Type:"+entry.getDepartmentCreditsEntryType().name()+
					" Person name:"+ entry.getEmployment().getPerson().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newStatus.name());*/	
		}	
		
		if(entry.getDepartmentCreditsEntryType()==DepartmentCreditsEntryType.ReassignedFromRetention){
		
			if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
				if(newStatus==GrantedStatus.Otorgado){
					/*Employment employmentActual = entry.getEmployment();
					
					employmentActual = employmentService.findById(employmentActual.getId());
					employmentActual.setStatus(EmploymentStatus.ACTIVO);
					
					employmentService.saveOrUpdate(employmentActual);
				
					Employment employmentAnterior = employmentService.findById(employmentActual.getPreviousEmployment().getId());
					
					employmentAnterior.setStatus(EmploymentStatus.INACTIVO);
								
					
					employmentService.saveOrUpdate(employmentAnterior);*/
				}
				
			}else{
					if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newStatus==GrantedStatus.Solicitado){
							/*Employment employmentActual = entry.getEmployment();
							
							employmentActual = employmentService.findById(employmentActual.getId());
							
							employmentActual.setStatus(EmploymentStatus.INACTIVO);
							
							employmentService.saveOrUpdate(employmentActual);
						
							Employment employmentAnterior = employmentService.findById(employmentActual.getPreviousEmployment().getId());
							
							employmentAnterior.setStatus(EmploymentStatus.ACTIVO);
							
							employmentService.saveOrUpdate(employmentAnterior);*/
													
						}
					}
				}
		
		}/*else 
			if(entry.getCreditsEntryType()==CreditsEntryType.IngresoAgente){
				if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
					if(newStatus==GrantedStatus.Otorgado){
						Employment employment = entry.getEmployment();
						
						employment = employmentService.findById(employment.getId());
						
						employment.setStatus(EmploymentStatus.ACTIVO);
						employmentService.saveOrUpdate(employment);
					}
				}else
					if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newStatus==GrantedStatus.Solicitado){
							Employment employment = entry.getEmployment();
							
							employment = employmentService.findById(employment.getId());
							
							employment.setStatus(EmploymentStatus.INACTIVO);
							employmentService.saveOrUpdate(employment);
						}
					}

			}else 
				if(entry.getCreditsEntryType()==CreditsEntryType.BajaAgente){
					if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
						if(newStatus==GrantedStatus.Otorgado){
							Employment employment = entry.getEmployment();
							
							employment = employmentService.findById(employment.getId());
							
							employment.setStatus(EmploymentStatus.BAJA);
							employment.setEndDate(creditsPeriodService.getCurrentCreditsPeriod().getStartDate());
							employmentService.saveOrUpdate(employment);
						}
					}else
						if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
							if(newStatus==GrantedStatus.Solicitado){
								Employment employment = entry.getEmployment();
								
								employment = employmentService.findById(employment.getId());
								
								employment.setStatus(EmploymentStatus.ACTIVO);
								employment.setEndDate(null);
								employmentService.saveOrUpdate(employment);
							}
						}
				}*/
				
		
		entry.setGrantedStatus(newStatus);
		departmentCreditsEntryDao.saveOrUpdate(entry);
		
	}

	
	/*
	@Override
	public Set<Long> havePendingEntries(List<Long> personIds, Long departmentId,Long idCreditsPeriod) {
		
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setDepartmentId(departmentId);
		employmentQueryFilter.setPersonsIds(personIds);
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.PENDIENTE);
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);//an employment can be active with requested deactivation
		
		
		DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter = new DepartmentCreditsEntryQueryFilter();
		
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		creditsEntryQueryFilter.setIdCreditsPeriod(idCreditsPeriod);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		
		
		List<DepartmentCreditsEntry> entriesRequested = this.find(creditsEntryQueryFilter);

		Set<Long> resultPersonsIds = new HashSet<Long>();
		for(DepartmentCreditsEntry creditsEntry : entriesRequested){
			//resultPersonsIds.add(creditsEntry.getEmployment().getPerson().getId());
		}
		
		return resultPersonsIds;
	}*/
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

}

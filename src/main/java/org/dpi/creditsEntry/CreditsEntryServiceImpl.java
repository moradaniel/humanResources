package org.dpi.creditsEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.employment.EmploymentStatus;
import org.dpi.person.Person;
import org.dpi.person.PersonService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



public class CreditsEntryServiceImpl implements CreditsEntryService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final CreditsEntryDao creditsEntryDao;

	private EmploymentService employmentService;
	
	private final PersonService personService;
	private AdministradorCreditosService administradorCreditosService;
	

	private ApplicationContext applicationContext;
	
	public CreditsEntryServiceImpl(	final CreditsEntryDao creditsEntryDao,
											final PersonService personService,
											final AdministradorCreditosService administradorCreditosService) {
		this.creditsEntryDao = creditsEntryDao;
		this.personService = personService;
		this.administradorCreditosService = administradorCreditosService;
	}

	
	public List<CreditsEntry> find(CreditsEntryQueryFilter creditsEntryQueryFilter){
		
		return creditsEntryDao.find(creditsEntryQueryFilter);
	}

	
	public void saveOrUpdate(final CreditsEntry entry)
	{
		creditsEntryDao.saveOrUpdate(entry);
	}

	public void delete(final CreditsEntry entry)
	{
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to delete creditsEntry: "+entry.toString());/*+ entry.getId()+
					" Type:"+entry.getTipoMovimientoCreditos().name()+
					" Agent name:"+ entry.getEmpleo().getPerson().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		if(entry.getCreditsEntryType().equals(CreditsEntryType.BajaAgente)){
			entry.getEmployment().setFechaFin(null);
			entry.getEmployment().setStatus(EmploymentStatus.ACTIVO);
			
			employmentService.saveOrUpdate(entry.getEmployment());
			creditsEntryDao.delete(entry);
		}else
		if(entry.getCreditsEntryType().equals(CreditsEntryType.AscensoAgente)){
			
			//borrar entry 
			Employment employmentPendiente = entry.getEmployment();
			creditsEntryDao.delete(entry);
			//borrar employment pendiente
			employmentService.delete(employmentPendiente);
		}else
		
		if(entry.getCreditsEntryType().equals(CreditsEntryType.IngresoAgente)){
			
			Person personToBeDeleted = entry.getEmployment().getPerson();
			
			employmentService.delete(entry.getEmployment());
			
			personService.delete(personToBeDeleted);
					
		}
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: delete creditsEntry - "+ entry.toString());	
		}

	}

	public CreditsEntryDao getCreditsEntryDao()
	{
		return creditsEntryDao;
	}


	public List<CreditsEntry> findAll(){
		return this.creditsEntryDao.findAll();
	}
	

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
	
	
	public List<CreditsEntryVO> buildCreditsEntryVO(
			List<CreditsEntry> creditsEntryReparticion, Account account) {
		List<CreditsEntryVO> creditsEntriesVO = new ArrayList<CreditsEntryVO>();
		for(CreditsEntry creditsEntry:creditsEntryReparticion){
			CreditsEntryVO creditsEntryVO = new CreditsEntryVO();
			creditsEntryVO.setCreditsEntry(creditsEntry);
			if(creditsEntry.getCreditsEntryType()==CreditsEntryType.AscensoAgente){
				Employment employmentAnterior = creditsEntry.getEmployment().getPreviousEmployment();
				creditsEntryVO.setCurrentCategory(employmentAnterior.getCategory().getCode());
				creditsEntryVO.setProposedCategory(creditsEntry.getEmployment().getCategory().getCode());
			}else{
				creditsEntryVO.setCurrentCategory(creditsEntry.getEmployment().getCategory().getCode());
			}
			
			if(creditsEntry.getEmployment().getOccupationalGroup()!=null){
				creditsEntryVO.setOccupationalGroup(creditsEntry.getEmployment().getOccupationalGroup().getName());
				if(creditsEntry.getEmployment().getOccupationalGroup().getParentOccupationalGroup()!=null){
					creditsEntryVO.setParentOccupationalGroup(creditsEntry.getEmployment().getOccupationalGroup().getParentOccupationalGroup().getName());
				}
			}
			
			creditsEntryVO.setCanAccountBorrarMovimiento(canCreditsEntryBeDeletedByAccount(creditsEntry,account));
			creditsEntryVO.setCanAccountChangeCreditsEntryStatus(canAccountChangeCreditsEntryStatus(creditsEntry,account));
			
			creditsEntriesVO.add(creditsEntryVO);
		}

			
		return creditsEntriesVO;
	}
	
	public boolean canAccountChangeCreditsEntryStatus(CreditsEntry creditsEntry, Account account) {
		return canChangeCreditsEntryStatus(account) && canCreditsEntryStatusBeChanged(creditsEntry);
	}
	
	public boolean canCreditsEntryStatusBeChanged(
			CreditsEntry creditsEntry) {
		
		//only creditsEntries of Active periods can be changed 
		if(creditsEntry.getCreditsPeriod().getStatus()!=CreditsPeriod.Status.Active){
			return false;
		}else

		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.BajaAgente){
			 return false;
		 }else
		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.CargaInicialAgenteExistente){
			 return false;
		 }else
		 
		 if(creditsEntry.getCreditsEntryType()== CreditsEntryType.IngresoAgente){
			 if(creditsEntry.getCantidadCreditos()==0){
				 return false;
			 }
		 }

		 
		 return true;
	}

	
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
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "UPDATE_STATUS")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeletedByAccount(CreditsEntry entry, Account account){
		
		return canDeleteCreditsEntry(account) && canCreditsEntryBeDeleted(entry);
		
	}
	
	public boolean canDeleteCreditsEntry(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "DELETE")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeleted(CreditsEntry creditsEntry){
		
				
		if(isCreditsEntryClosed(creditsEntry)){
			return false;
		}
		
		if(creditsEntry.getCreditsEntryType()==CreditsEntryType.BajaAgente){
			return false;
		}
		
		if(creditsEntry.getCreditsEntryType()==CreditsEntryType.CargaInicialAgenteExistente){
			return false;
		}
		
		if(creditsEntry.getCreditsEntryType()==CreditsEntryType.IngresoAgente && creditsEntry.getGrantedStatus()==GrantedStatus.Otorgado){
			return false;
		}
		
		
		if(creditsEntry.getCreditsEntryType()==CreditsEntryType.AscensoAgente && creditsEntry.getEmployment().isClosed()){
			return false;
		}
				
	
		return true;
	}

	private boolean isCreditsEntryClosed(CreditsEntry creditsEntry) {
		
		if(creditsEntry.getCreditsPeriod().getStatus() == Status.Closed ){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void actualizarCreditosPorAscenso(){
		//obtener entries de tipo Ascenso
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		List<CreditsEntry> creditsEntries = this.find(creditsEntryQueryFilter);
		for(CreditsEntry creditsEntry :creditsEntries){
			//Obtener el employment del entry de tipo ascenso (EmpleoaActualizar)
			Employment employmentaActualizar = creditsEntry.getEmployment();
			//Obtener el employment anterior al EmpleoaActualizar (EmpleoAnterior)
			Employment employmentAnterior = employmentaActualizar.getPreviousEmployment();
			//get the category of the previous employment
			//Obtener los creditos por ascenso para la categoria anterior y la categoria actual
			int nuevaCantidaddeCreditos = administradorCreditosService.getCreditosPorAscenso(employmentAnterior.getPerson().getCondition(), 
																							employmentAnterior.getCategory().getCode(),
																							employmentaActualizar.getCategory().getCode());
			creditsEntry.setCantidadCreditos(nuevaCantidaddeCreditos);
			this.saveOrUpdate(creditsEntry);
			
	
		}
	}
	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}


	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}
	
	@Override
	public void changeGrantedStatus(CreditsEntry entry, GrantedStatus newEstado){
		

		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("user:"+currentUser.getName()+" attempting to change granted status of entry:"+ entry.getId()+
					" Type:"+entry.getCreditsEntryType().name()+
					" Agent name:"+ entry.getEmployment().getPerson().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());	
		}	
		
		if(entry.getCreditsEntryType()==CreditsEntryType.AscensoAgente){
		
			if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
				if(newEstado==GrantedStatus.Otorgado){
					Employment employmentActual = entry.getEmployment();
					
					employmentActual = employmentService.findById(employmentActual.getId());
					employmentActual.setStatus(EmploymentStatus.ACTIVO);
					
					employmentService.saveOrUpdate(employmentActual);
				
					Employment employmentAnterior = employmentService.findById(employmentActual.getPreviousEmployment().getId());
					
					employmentAnterior.setStatus(EmploymentStatus.INACTIVO);
								
					
					employmentService.saveOrUpdate(employmentAnterior);
				}
				
			}else{
					if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newEstado==GrantedStatus.Solicitado){
							Employment employmentActual = entry.getEmployment();
							
							employmentActual = employmentService.findById(employmentActual.getId());
							
							employmentActual.setStatus(EmploymentStatus.INACTIVO);
							
							employmentService.saveOrUpdate(employmentActual);
						
							Employment employmentAnterior = employmentService.findById(employmentActual.getPreviousEmployment().getId());
							
							employmentAnterior.setStatus(EmploymentStatus.ACTIVO);
							
							employmentService.saveOrUpdate(employmentAnterior);
													
						}
					}
				}
		
		}else 
			if(entry.getCreditsEntryType()==CreditsEntryType.IngresoAgente){
				if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
					if(newEstado==GrantedStatus.Otorgado){
						Employment employment = entry.getEmployment();
						
						employment = employmentService.findById(employment.getId());
						
						employment.setStatus(EmploymentStatus.ACTIVO);
						employmentService.saveOrUpdate(employment);
					}
				}else
					if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newEstado==GrantedStatus.Solicitado){
							Employment employment = entry.getEmployment();
							
							employment = employmentService.findById(employment.getId());
							
							employment.setStatus(EmploymentStatus.INACTIVO);
							employmentService.saveOrUpdate(employment);
						}
					}

			}
		
		entry.setGrantedStatus(newEstado);
		creditsEntryDao.saveOrUpdate(entry);
		
	}

	
	@Override
	public Set<Long> haveMovimientosSolicitados(List<Long> agentesIds, Long idReparticion,Long idCreditsPeriod) {
		
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setReparticionId(String.valueOf(idReparticion));
		employmentQueryFilter.setPersonsIds(agentesIds);
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.PENDIENTE);
		
		
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		creditsEntryQueryFilter.setIdCreditsPeriod(idCreditsPeriod);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		
		
		List<CreditsEntry> entriesSolicitados = this.find(creditsEntryQueryFilter);

		Set<Long> resultPersonsIds = new HashSet<Long>();
		for(CreditsEntry creditsEntry : entriesSolicitados){
			resultPersonsIds.add(creditsEntry.getEmployment().getPerson().getId());
		}
		
		return resultPersonsIds;
	}
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

}

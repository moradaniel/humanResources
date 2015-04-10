package org.dpi.creditsEntry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.Employment;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.employment.EmploymentStatus;
import org.dpi.person.Person;
import org.dpi.person.PersonService;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;



public class CreditsEntryServiceImpl implements CreditsEntryService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Resource(name = "creditsEntryDao")
	private CreditsEntryDao creditsEntryDao;

	@Resource(name = "employmentService")
	private EmploymentService employmentService;
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	

	private ApplicationContext applicationContext;
	
	public CreditsEntryServiceImpl() {

	}

	
	public List<CreditsEntry> find(CreditsEntryQueryFilter creditsEntryQueryFilter){
		
		return creditsEntryDao.find(creditsEntryQueryFilter);
	}
	
	public PageList<CreditsEntry> findCreditsEntries(final CreditsEntryQueryFilter creditsEntryQueryFilter){
	    
	    return creditsEntryDao.findCreditsEntries(creditsEntryQueryFilter);
	}

	
	public void saveOrUpdate(final CreditsEntry entry)
	{
		creditsEntryDao.saveOrUpdate(entry);
	}

	public void delete(CreditsEntry entry)
	{
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser==null){
			throw new RuntimeException("User must exist to delete an entry");
		}
		
		log.info("================ user:"+currentUser.getName()+" attempting to delete creditsEntry: "+entry.toString());


		if(entry.getCreditsEntryType().equals(CreditsEntryType.BajaAgente)){
			
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
			
			employmentService.saveOrUpdate(employment);
			
			
		}else
			if(entry.getCreditsEntryType().equals(CreditsEntryType.AscensoAgente)){

				//delete entry 
				Employment employmentPendiente = entry.getEmployment();
				creditsEntryDao.delete(entry);
				//delete pending employment
				employmentService.delete(employmentPendiente);
			}else

				if(entry.getCreditsEntryType().equals(CreditsEntryType.IngresoAgente)){

					Person personToBeDeleted = entry.getEmployment().getPerson();

					employmentService.delete(entry.getEmployment());

					personService.delete(personToBeDeleted);

				}


		log.info("================ user:"+currentUser.getName()+" Successfully performed: delete creditsEntry - "+ entry.toString());	


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
				Employment previousEmployment = creditsEntry.getEmployment().getPreviousEmployment();
				creditsEntryVO.setCurrentCategory(previousEmployment.getCategory().getCode());
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
			
			creditsEntryVO.setCanBeDeleted(canCreditsEntryBeDeletedByAccount(creditsEntry,account));
			
			creditsEntryVO.setCanAccountChangeCreditsEntryStatus(canAccountChangeCreditsEntryStatus(creditsEntry,account));
			
			creditsEntriesVO.add(creditsEntryVO);
		}

			
		return creditsEntriesVO;
	}
	
	public boolean canAccountChangeCreditsEntryStatus(CreditsEntry creditsEntry, Account account) {
		return canChangeCreditsEntryStatus(account) && creditsEntry.canStatusBeChanged(this,creditsPeriodService);
	}
	
	
	
	
	public static boolean canChangeCreditsEntryStatus(Account account, CreditsPeriod creditsPeriod) {
		/*if(creditsPeriod.getStatus()!=Status.Active){
			return false;
		}else*/
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


	public boolean canCreditsEntryBeDeletedByAccount(CreditsEntry entry, Account account){
		
		return canDeleteCreditsEntry(account) && canCreditsEntryBeDeleted(entry);
		
	}
	
	public boolean canDeleteCreditsEntry(Account account) {
		
		if(account.hasPermissions("Manage_CreditsEntries", "DELETE")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeleted(CreditsEntry creditsEntry){
		
				
		if(isCreditsEntryClosed(creditsEntry)){
			return false;
		}
		
		if(creditsEntry.getGrantedStatus()==GrantedStatus.Otorgado) {
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
			int nuevaCantidaddeCreditos = creditsManagerService.getCreditosPorAscenso(employmentAnterior.getCategory().getCode(),
																						employmentaActualizar.getCategory().getCode());
			creditsEntry.setNumberOfCredits(nuevaCantidaddeCreditos);
			this.saveOrUpdate(creditsEntry);
			
	
		}
	}
	
	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}


	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}
	
	@Override
	public void changeGrantedStatus(CreditsEntry entry, GrantedStatus newStatus){
		

		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("user:"+currentUser.getName()+" attempting to change granted status of entry:"+ entry.getId()+
					" Type:"+entry.getCreditsEntryType().name()+
					" Person name:"+ entry.getEmployment().getPerson().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newStatus.name());	
		}
		
		if(entry.getCreditsEntryType()==CreditsEntryType.AscensoAgente){
		
			if(entry.getGrantedStatus()==GrantedStatus.Solicitado){
				if(newStatus==GrantedStatus.Otorgado){
					Employment employmentActual = entry.getEmployment();
					
					employmentActual = employmentService.findById(employmentActual.getId());
					employmentActual.setStatus(EmploymentStatus.ACTIVO);
					
					employmentService.saveOrUpdate(employmentActual);
				
					Employment previousEmployment = employmentService.findById(employmentActual.getPreviousEmployment().getId());
					
					previousEmployment.setStatus(EmploymentStatus.PENDIENTE);
								
					
					employmentService.saveOrUpdate(previousEmployment);
				}
				
			}else{
					if(entry.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newStatus==GrantedStatus.Solicitado){
							Employment employmentActual = entry.getEmployment();
							
							employmentActual = employmentService.findById(employmentActual.getId());
							
							employmentActual.setStatus(EmploymentStatus.PENDIENTE);
							
							employmentService.saveOrUpdate(employmentActual);
						
							Employment previousEmployment = employmentService.findById(employmentActual.getPreviousEmployment().getId());
							
							previousEmployment.setStatus(EmploymentStatus.ACTIVO);
							
							employmentService.saveOrUpdate(previousEmployment);
													
						}
					}
				}
		
		}else 
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
							
							employment.setStatus(EmploymentStatus.PENDIENTE);
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
							employment.setEndDate(entry.getCreditsPeriod().getStartDate());
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
				}
				
		
		entry.setGrantedStatus(newStatus);
		creditsEntryDao.saveOrUpdate(entry);
		
	}

	
	@Override
	public Set<Long> havePendingEntries(List<Long> personIds, Long departmentId,Long idCreditsPeriod) {
		
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setDepartmentId(departmentId);
		employmentQueryFilter.setPersonsIds(personIds);
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.PENDIENTE);
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);//an employment can be active with requested deactivation
		
		
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		
		creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
		creditsEntryQueryFilter.setIdCreditsPeriod(idCreditsPeriod);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		
		
		List<CreditsEntry> entriesRequested = this.find(creditsEntryQueryFilter);

		Set<Long> resultPersonsIds = new HashSet<Long>();
		for(CreditsEntry creditsEntry : entriesRequested){
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


    @Override
    public List<CreditsEntry> findSubsequentEntries(CreditsEntry creditsEntry) {
        String currentCreditsEntryPeriodName = creditsPeriodService.getCurrentCreditsPeriod().getName();
        String creditsEntryPeriodName = creditsEntry.getCreditsPeriod().getName();
        int currentCreditsEntryPeriodNameInt = Integer.parseInt(currentCreditsEntryPeriodName);
        int creditsEntryPeriodNameInt = Integer.parseInt(creditsEntryPeriodName);
        creditsEntryPeriodNameInt = creditsEntryPeriodNameInt + 1;
        List<String> pastCreditsPeriodNames = new ArrayList<String>();
        while(creditsEntryPeriodNameInt < currentCreditsEntryPeriodNameInt) {
            pastCreditsPeriodNames.add(String.valueOf(creditsEntryPeriodNameInt));
            creditsEntryPeriodNameInt = creditsEntryPeriodNameInt + 1;
            
        }
                    
        List<GrantedStatus> pastGrantedStatus = new ArrayList<GrantedStatus>();
        pastGrantedStatus.add(GrantedStatus.Otorgado);
        
        List<String> paramNames = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        
        StringBuffer queryBuilder = new StringBuffer();

        queryBuilder.append(" Select entry FROM CreditsEntryImpl entry ");
        queryBuilder.append(" LEFT OUTER JOIN entry.employment employment ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH employment.person person ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH subDepartment.department department ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH employment.category category ");
        queryBuilder.append(" LEFT OUTER JOIN entry.creditsPeriod creditsPeriod ");
        queryBuilder.append(" LEFT OUTER JOIN employment.previousEmployment previousEmployment ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH previousEmployment.category previousCategory ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup ");
        //queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");
        
        queryBuilder.append(" WHERE ");
        queryBuilder.append(" previousEmployment.id = :previousEmploymentId");
        queryBuilder.append(" and ");
        queryBuilder.append(" ( 1<>1");
        if(!CollectionUtils.isEmpty(pastCreditsPeriodNames)) {
            queryBuilder.append("  or (creditsPeriod.name IN (:pastCreditsPeriodNames) ");
            queryBuilder.append("       and entry.grantedStatus IN (:pastGrantedStatus) )");
            
            paramNames.add("pastCreditsPeriodNames");
            values.add(pastCreditsPeriodNames);

            paramNames.add("pastGrantedStatus");
            values.add(pastGrantedStatus);
        }
        
        queryBuilder.append("   or ");
        queryBuilder.append("   creditsPeriod.name = :currentCreditsEntryPeriodName ");
        queryBuilder.append("  )");

        
        paramNames.add("previousEmploymentId");
        values.add(creditsEntry.getEmployment().getId());
        


        paramNames.add("currentCreditsEntryPeriodName");
        values.add(currentCreditsEntryPeriodName);
        

                            
        List<CreditsEntry> subsequentEntries = creditsEntryDao.findByNamedParam(queryBuilder.toString(),paramNames,values, null, null);
        return subsequentEntries;
       // return !CollectionUtils.isEmpty(subsequentEntries);

    }
	
    
    protected CreditsEntryDao getCreditsEntryDao()
    {
        return creditsEntryDao;
    }
	

}

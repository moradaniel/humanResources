package org.dpi.employment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.dpi.category.Category;
import org.dpi.category.CategoryService;
import org.dpi.creditsEntry.CreditsEntry;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsEntry.CreditsEntryImpl;
import org.dpi.creditsEntry.CreditsEntryQueryFilter;
import org.dpi.creditsEntry.CreditsEntryService;
import org.dpi.creditsEntry.CreditsEntryType;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.person.Person;
import org.dpi.person.PersonImpl;
import org.dpi.person.PersonService;
import org.dpi.subDepartment.SubDepartment;
import org.dpi.subDepartment.SubDepartmentService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;



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
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "subDepartmentService")
	private SubDepartmentService subDepartmentService;
	
	private ApplicationContext applicationContext;
	
	public EmploymentCreditsEntriesServiceImpl() {

	}

	@Override
	public void promotePerson(Employment currentEmployment, String newCategoryCode){
		
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to promote person:"+currentEmployment.getPerson().toString());	
		}
		
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		Person employee = currentEmployment.getPerson();
		List<Long> employeeIds = new ArrayList<Long>();
		employeeIds.add(employee.getId());
		Set<Long>resultEmployeeIds = creditsEntryService.havePendingEntries(employeeIds, currentEmployment.getSubDepartment().getDepartment().getId(), currentCreditsPeriod.getId());
		
		if(resultEmployeeIds.contains(employee.getId())){
			return;
		}

		Category newCategory = categoryService.findByCode(newCategoryCode);
		Employment newEmployment = new EmploymentImpl();
		newEmployment.setPerson(currentEmployment.getPerson());
		newEmployment.setCategory(newCategory);
		newEmployment.setSubDepartment(currentEmployment.getSubDepartment());
		newEmployment.setOccupationalGroup(currentEmployment.getOccupationalGroup());
		newEmployment.setStartDate(new Date());
		newEmployment.setStatus(EmploymentStatus.PENDIENTE);
		
		
		//crear un entry de tipo ascenso 
		CreditsEntryImpl entryAscenso = new CreditsEntryImpl();
		entryAscenso.setCreditsEntryType(CreditsEntryType.AscensoAgente);
		int cantidadCreditosPorAscenso = creditsManagerService.getCreditosPorAscenso(currentEmployment.getCategory().getCode(),newCategoryCode);
		entryAscenso.setGrantedStatus(GrantedStatus.Solicitado);
		entryAscenso.setCreditsPeriod(currentCreditsPeriod);

		
		entryAscenso.setNumberOfCredits(cantidadCreditosPorAscenso);
		
		
		//setear empleo a entry  y agregar entry a empleo
		newEmployment.addCreditsEntry(entryAscenso);
		
		entryAscenso.setEmployment(newEmployment);

		//set previous employment
		newEmployment.setPreviousEmployment(currentEmployment);
		
		
		//guardar entry y empleo
		employmentService.saveOrUpdate(newEmployment);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: promote person - "+
					" subDepartment: "+newEmployment.getSubDepartment().toString()+
					" Employee :"+ newEmployment.getPerson().toString()+
					" creditsEntry: "+entryAscenso.toString());	
		}
	}
	
	@Override
	public void deactivate(Employment employment, Account account) {
		
		if(account!=null){
			log.info("================ user:"+account.getName()+" attempting to deactivate:"+employment.toString());	
		}	
		
		
		//check user has permission to deactivate employments
		
		if(!canAccountDeactivateEmployments(account)) {
			throw new RuntimeException("Account '"+account.getName()+ "' has no permission to deactivate employments ");
					
		}
		
		
		//employment endDate is the current creditsEntry startdDate
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		//employment.setEndDate(currentCreditsPeriod.getStartDate());
		//employment.setStatus(EmploymentStatus.BAJA);
		
		//create an entry of type DEACTIVATION 
		CreditsEntryImpl deactivationEntry = new CreditsEntryImpl();
		deactivationEntry.setCreditsPeriod(currentCreditsPeriod);
		deactivationEntry.setGrantedStatus(GrantedStatus.Solicitado);
		deactivationEntry.setCreditsEntryType(CreditsEntryType.BajaAgente);
		
		int cantidadCreditosPorBaja = creditsManagerService.getCreditosPorBaja(employment.getCategory().getCode());

		
		deactivationEntry.setNumberOfCredits(cantidadCreditosPorBaja);
		
		
		//setear empleo a entry  y agregar entry a empleo
		employment.addCreditsEntry(deactivationEntry);
		
		deactivationEntry.setEmployment(employment);
		
		//save creditsEntry and employment
		employmentService.saveOrUpdate(employment);
		
		if(account!=null){
			log.info("================ user:"+account.getName()+" Successfully performed: employment deactivated - "+employment.toString()
					/*" subDepartment: "+subDepartment.toString()+
					" Employee :"+ nuevoAgentePropuesto.toString()+
					" creditsEntry: "+creditsEntryIngreso.toString()*/);	
		}
		
	}
	
	@Override
	public void undoDeactivation(Employment employment, Account account) {
		
		if(account!=null){
			log.info("================ user:"+account.getName()+" attempting to UNDO deactivation:"+employment.toString());	
		}	
		
		
		//check user has permission to undo deactivate employments
		
		if(!canAccountDeactivateEmployments(account)) {
			throw new RuntimeException("Account '"+account.getName()+ "' has no permission to undo deactivation employments ");
					
		}
		
		if(!canAccountUndoDeactivationEmployments(account,employment)) {
			throw new RuntimeException("Employment '"+employment.toString()+ "' deactivation can not be undone ");
					
		}
		
		CreditsEntry undoableDeactivationCreditEntry = this.getUndoableCreditEntry(employment, CreditsEntryType.BajaAgente);
		
		if(undoableDeactivationCreditEntry!=null) {
			creditsEntryService.delete(undoableDeactivationCreditEntry);
		}
	
		
		
		
		/*
		
		//employment endDate is the current creditsEntry startdDate
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		//employment.setEndDate(currentCreditsPeriod.getStartDate());
		//employment.setStatus(EmploymentStatus.BAJA);
		
		//create an entry of type DEACTIVATION 
		CreditsEntryImpl deactivationEntry = new CreditsEntryImpl();
		deactivationEntry.setCreditsPeriod(currentCreditsPeriod);
		deactivationEntry.setGrantedStatus(GrantedStatus.Solicitado);
		deactivationEntry.setCreditsEntryType(CreditsEntryType.BajaAgente);
		
		int cantidadCreditosPorBaja = creditsManagerService.getCreditosPorBaja(employment.getCategory().getCode());

		
		deactivationEntry.setCantidadCreditos(cantidadCreditosPorBaja);
		
		
		//setear empleo a entry  y agregar entry a empleo
		employment.addCreditsEntry(deactivationEntry);
		
		deactivationEntry.setEmployment(employment);
		
		//save creditsEntry and employment
		employmentService.saveOrUpdate(employment);*/
		
		if(account!=null){
			log.info("================ user:"+account.getName()+" Successfully performed: employment UNDO deactivation - "+employment.toString()
					/*" subDepartment: "+subDepartment.toString()+
					" Employee :"+ nuevoAgentePropuesto.toString()+
					" creditsEntry: "+creditsEntryIngreso.toString()*/);	
		}
		
	}
	
	
	@Override
	public void proposeNewEmployment(String proposedCategoryCode,Long subDepartmentId) {
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to add proposed employee:");/*+ entry.getId()+
					" Type:"+entry.getTipoCreditsEntry().name()+
					" Agent name:"+ entry.getEmpleo().getAgente().getApellidoNombre()+
					" from status: "+entry.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		// create employee nn
		Person newProposedPerson = createPendingPerson();
		
		personService.save(newProposedPerson);
		
		//create employment
		Employment newProposedEmployee = new EmploymentImpl();
		newProposedEmployee.setPerson(newProposedPerson);
		//set proposed category to the employment
		newProposedEmployee.setCategory(categoryService.findByCode(proposedCategoryCode));
		//al empleo ponerlo en status pendiente
		newProposedEmployee.setStatus(EmploymentStatus.PENDIENTE);
		
		newProposedEmployee.setStartDate(new Date());
		
		SubDepartment subDepartment = subDepartmentService.findById(subDepartmentId);
		newProposedEmployee.setSubDepartment(subDepartment);
		
		//Crear entry de ingreso
		CreditsEntry creditsEntryIngreso = new CreditsEntryImpl();
		creditsEntryIngreso.setCreditsEntryType(CreditsEntryType.IngresoAgente);
		creditsEntryIngreso.setGrantedStatus(GrantedStatus.Solicitado);
		creditsEntryIngreso.setCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod());
		
		creditsEntryIngreso.setEmployment(newProposedEmployee);
		newProposedEmployee.addCreditsEntry(creditsEntryIngreso);
		
		int creditosPorIngreso = creditsManagerService.getCreditosPorIngreso(proposedCategoryCode);
		creditsEntryIngreso.setNumberOfCredits(creditosPorIngreso);
		
		employmentService.save(newProposedEmployee);
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: add proposed employee - "+
					" subDepartment: "+subDepartment.toString()+
					" Employee :"+ newProposedPerson.toString()+
					" creditsEntry: "+creditsEntryIngreso.toString());	
		}
		
	}
	
	
	private Person createPendingPerson(){
		PersonImpl newPerson = new PersonImpl();
		newPerson.setApellidoNombre("Ingreso Nuevo Propuesto "+creditsPeriodService.getCurrentCreditsPeriod().getName());
		newPerson.setCuil("");
		return newPerson;
	}
	
	
	
	
	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	@Override
	public List<EmploymentVO> buildEmploymentsVO(List<Employment> employments, Long departmentId,
			Account currentUser) {
		
		CreditsPeriod currentCreditsPeriod = creditsPeriodService.getCurrentCreditsPeriod();
		
		List<Long> posiblesAgentesIds = new ArrayList<Long>();
		for(Employment employment:employments){
			posiblesAgentesIds.add(employment.getPerson().getId());
		}
		
		Set<Long> personsIds = new HashSet<Long>();
		
		//havePendingEntries is a heavy task so we check if it is really needed
		if(	canAccountPromotePerson(currentUser) ||
			canAccountDeactivateEmployments(currentUser) ||
			canAccountDeactivateEmployments(currentUser)){
			personsIds = creditsEntryService.havePendingEntries(posiblesAgentesIds, departmentId, currentCreditsPeriod.getId());
		}
		
		
		//get promotions in the last two periods for the persons
		Map<Long, List<CreditsEntry>> lastPersonPromotionsMap = new HashMap<Long, List<CreditsEntry>>();
				
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.setPersonIds(posiblesAgentesIds);
		
		creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.AscensoAgente);
		creditsEntryQueryFilter.addGrantedStatus(GrantedStatus.Otorgado);
		
		//last 2 periods	
		creditsEntryQueryFilter.addCreditsPeriodName(String.valueOf(Integer.parseInt(currentCreditsPeriod.getName())-1));
		creditsEntryQueryFilter.addCreditsPeriodName(String.valueOf(Integer.parseInt(currentCreditsPeriod.getName())-2));
		
		List<CreditsEntry> lastPromotionsCreditsEntries = creditsEntryService.find(creditsEntryQueryFilter);
		
		for(CreditsEntry lastPromotionCreditsEntry : lastPromotionsCreditsEntries) {
		    Long personId = lastPromotionCreditsEntry.getEmployment().getPerson().getId();
		    if(lastPersonPromotionsMap.get(personId)==null) {
		        lastPersonPromotionsMap.put(personId, new ArrayList<CreditsEntry>());
		    }
		    lastPersonPromotionsMap.get(personId).add(lastPromotionCreditsEntry);
		}
		
		
		List<EmploymentVO> employmentsVO = new ArrayList<EmploymentVO>();
		for(Employment employment:employments){
			EmploymentVO employmentVO = new EmploymentVO();
			employmentVO.setEmployment(employment);
			
			if(personsIds.contains(employment.getPerson().getId())) {
				employmentVO.addNote("Tiene movimientos pendientes de otorgar.");
			}
			
			if(hasPendingDeactivationEntry(employment)) {
				employmentVO.addNote("Baja pendiente de otorgar.");
			}
			
			boolean promotedInLast2Periods = lastPersonPromotionsMap.get(employment.getPerson().getId())!=null;
			if(promotedInLast2Periods) {
			    List<CreditsEntry> lastPromotions = lastPersonPromotionsMap.get(employment.getPerson().getId());
			    for(CreditsEntry lastPromotion :lastPromotions) {
			        employmentVO.addNote("Promovido en: "+lastPromotion.getCreditsPeriod().getName());    
			    }
			    
			}
			
			//a person can not be promoted if has pending promotions
	         //a person can not be promoted if was already promoted in the las two periods
			employmentVO.setCanBePromoted(  !promotedInLast2Periods &&
			                                employment.getStatus()==EmploymentStatus.ACTIVO && 
			                                canAccountPromotePerson(currentUser) && 
			                                !personsIds.contains(employment.getPerson().getId()));
			
			//a person can not be deactivated if has pending promotions
			//a person can not be deactivated if has pending deactivation entry in REQUESTED status in current period
			employmentVO.setCanBeDeactivated(employment.getStatus()==EmploymentStatus.ACTIVO 
											&& canAccountDeactivateEmployments(currentUser) 
											&& employment.getOccupationalGroup() != null
											&& !StringUtils.isEmpty(employment.getPerson().getCuil())
											&& !personsIds.contains(employment.getPerson().getId())
											&& !hasPendingDeactivationEntry(employment));
			
			employmentVO.setCanPersonBeModified(canAccountModifyPerson(currentUser));
			
			employmentVO.setCanUndoDeactivation(canAccountUndoDeactivationEmployments(currentUser,employment));
			
			employmentVO.setCanPersonBeTransfered(canAccountTransferPerson(currentUser));

			employmentsVO.add(employmentVO);
		}

			
		return employmentsVO;
	}
	
	
	
	private boolean hasPendingDeactivationEntry(Employment employment/*,CreditsPeriod period*/) {
		/*for(CreditsEntry entry : employment.getCreditsEntries()) {
			if(entry.getCreditsEntryType()==CreditsEntryType.BajaAgente
				&& entry.getGrantedStatus()==GrantedStatus.Solicitado
				&& entry.getCreditsPeriod().getName().equalsIgnoreCase(period.getName())) {
					return true;
				}
		}*/
		
		CreditsEntry undoableDectivationEntry = getUndoableCreditEntry(employment,CreditsEntryType.BajaAgente);
		if(undoableDectivationEntry==null) {
			return false;
		}
		
		return true;
	}
	
	
	public boolean canAccountUndoDeactivationEmployments(Account account, Employment employment) {
		
		if(!canAccountDeactivateEmployments(account)) {
			return false;
		}
		
		if(!hasPendingDeactivationEntry(employment)) {
			return false;
		}


		return true;
	}
	
	
	public CreditsEntry getUndoableCreditEntry(Employment employment, CreditsEntryType creditsEntryType) {

		CreditsEntry undoableCreditEntry = null;

		//only creditsENtry in current period can be undone
		Set<CreditsEntry> deactivationEntriesSet = employment.getCreditsEntries(creditsPeriodService.getCurrentCreditsPeriod(), creditsEntryType);

		if(CollectionUtils.isEmpty(deactivationEntriesSet)) {
			//if empty nothing to undo
			undoableCreditEntry = null;
		}else {

			for (CreditsEntry entry : deactivationEntriesSet) {
				if(entry.getGrantedStatus()==GrantedStatus.Solicitado) {
					undoableCreditEntry = entry;
				}
			}
		}

		return undoableCreditEntry;


	}

	public static boolean canAccountPromotePerson(Account account) {
		
		if(account.hasPermissions("Manage_CreditsEntries", "CREATE")){
			return true;
		}
		return false;
	}
	
	
	
	public static boolean canAccountDeactivateEmployments(Account account) {
		
		if(account.hasPermissions("Manage_Employments", "DEACTIVATE")){
			return true;
		}
		return false;
	}
	
	public static boolean canAccountModifyPerson(Account account) {
		
		if(account.hasPermissions("Manage_Employments", "UPDATE")){
			return true;
		}
		return false;
	}
	
	
	
   public static boolean canAccountTransferPerson(Account account) {
        
        if(account.hasPermissions("Manage_Employments", "TRANSFER")){
            return true;
        }
        return false;
    }
	
	public static boolean canAccountProposeNewEmployment(Account account) {
		
		if(account.hasPermissions("Manage_CreditsEntries", "CREATE")){
			return true;
		}
		return false;
	}
	
    @Override
    public void transferEmployee(Person personToBeTransfered,
            SubDepartment sourceSubdepartment,
            SubDepartment destinationSubdepartment) {
        
        String destinationSubdepartmentName = "";
        if(destinationSubdepartment==null) {
            destinationSubdepartmentName = "External subdepartment"; 
        }else {
            destinationSubdepartmentName =  destinationSubdepartment.toString();
        }
        
        Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(currentUser!=null){
            log.info("================ user:"+currentUser.getName()+" attempting to transfer person:"+personToBeTransfered.toString()+
                       " from subDepartment:"+sourceSubdepartment.toString()+" to subDepartment:"+destinationSubdepartmentName ); 
        }

        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
        employmentQueryFilter.setCodigoCentro(sourceSubdepartment.getCodigoCentro());
        employmentQueryFilter.setCodigoSector(sourceSubdepartment.getCodigoSector());
        employmentQueryFilter.addEmploymentStatus(EmploymentStatus.ACTIVO);
        employmentQueryFilter.setCuil(personToBeTransfered.getCuil());
        
        //get current employment
        Employment currentEmployment = employmentService.findEmployments(employmentQueryFilter).get(0);
       
        //check if employment is active
        
        if(!currentEmployment.getStatus().equals(EmploymentStatus.ACTIVO)) {
            throw new RuntimeException("Cannot transfer Employee. Current employment is not active ."+currentEmployment.toString());
        }
        
        //check if current subdepartment is equal to sourceSubdepartment
        
        if(!currentEmployment.getSubDepartment().getId().equals(sourceSubdepartment.getId())){
            throw new RuntimeException("Cannot transfer Employee. Employment is not in the given subdepartment."+currentEmployment.toString());
        }
        
        Employment newEmployment = new EmploymentImpl();
        if(destinationSubdepartment!=null) {
            Category category = categoryService.findByCode(currentEmployment.getCategory().getCode());

            newEmployment.setPerson(currentEmployment.getPerson());
            newEmployment.setCategory(category);
            newEmployment.setSubDepartment(destinationSubdepartment);
            newEmployment.setOccupationalGroup(currentEmployment.getOccupationalGroup());
            newEmployment.setStartDate(new Date());
            newEmployment.setStatus(EmploymentStatus.ACTIVO);
            newEmployment.setPreviousEmployment(currentEmployment);
            
            //create an entry of type ingreso 
            CreditsEntryImpl entryIngreso = new CreditsEntryImpl();
            entryIngreso.setCreditsEntryType(CreditsEntryType.IngresoAgente);
            int cantidadCreditosPorTransferencia = 0;
            entryIngreso.setNumberOfCredits(cantidadCreditosPorTransferencia);
            entryIngreso.setGrantedStatus(GrantedStatus.Otorgado);
            entryIngreso.setCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod());
            entryIngreso.setNotes("Transferencia desde: ("+sourceSubdepartment.getCodigoCentro()+"-"+sourceSubdepartment.getCodigoSector()+")"+sourceSubdepartment.getNombreCentro()+"-"+sourceSubdepartment.getNombreSector());
            
            //set employment to entry and entry to employment
            newEmployment.addCreditsEntry(entryIngreso);
            
            entryIngreso.setEmployment(newEmployment);

            //set previous employment
            newEmployment.setPreviousEmployment(currentEmployment);
            
            
            //save entry and employment
            employmentService.saveOrUpdate(newEmployment);

            
        }
        
        //deactivate current employment because of transfer
        
        currentEmployment.setStatus(EmploymentStatus.BAJA);
        currentEmployment.setEndDate(new Date());
                 
        CreditsEntryImpl entryBaja = new CreditsEntryImpl();
        entryBaja.setCreditsEntryType(CreditsEntryType.BajaAgente);
        int cantidadCreditosPorTransferencia = 0;
        entryBaja.setNumberOfCredits(cantidadCreditosPorTransferencia);
        entryBaja.setGrantedStatus(GrantedStatus.Otorgado);
        entryBaja.setCreditsPeriod(creditsPeriodService.getCurrentCreditsPeriod());
        String notes = "";
        if(destinationSubdepartment!= null) {
            notes="("+destinationSubdepartment.getCodigoCentro()+"-"+destinationSubdepartment.getCodigoSector()+")"+destinationSubdepartment.getNombreCentro()+"-"+destinationSubdepartment.getNombreSector();
        }else {
            notes="Centro Sector externo";
        }
        entryBaja.setNotes("Transferencia hacia: "+notes);
        
        currentEmployment.addCreditsEntry(entryBaja);
        entryBaja.setEmployment(currentEmployment);
        
        //save entry and employment
        employmentService.saveOrUpdate(currentEmployment);
        
        
        
        if(currentUser!=null){
            log.info("================ user:"+currentUser.getName()+" Successfully performed: transfer of person - "+
                    " from subDepartment: "+sourceSubdepartment.toString()+
                    " to subDepartment: "+destinationSubdepartmentName); 
        }
        
    }

}

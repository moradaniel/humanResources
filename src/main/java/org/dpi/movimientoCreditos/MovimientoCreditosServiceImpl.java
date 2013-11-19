package org.dpi.movimientoCreditos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.empleo.EmploymentService;
import org.dpi.empleo.EmploymentStatus;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.person.Person;
import org.dpi.person.PersonService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



public class MovimientoCreditosServiceImpl implements MovimientoCreditosService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final MovimientoCreditosDao movimientoCreditosDao;

	private EmploymentService employmentService;
	
	private final PersonService personService;
	private AdministradorCreditosService administradorCreditosService;
	

	private ApplicationContext applicationContext;
	
	public MovimientoCreditosServiceImpl(	final MovimientoCreditosDao movimientoCreditosDao,
											final PersonService personService,
											final AdministradorCreditosService administradorCreditosService) {
		this.movimientoCreditosDao = movimientoCreditosDao;
		this.personService = personService;
		this.administradorCreditosService = administradorCreditosService;
	}

	
	public List<MovimientoCreditos> find(MovimientoCreditosQueryFilter movimientoCreditosQueryFilter){
		
		return movimientoCreditosDao.find(movimientoCreditosQueryFilter);
	}

	
	public void saveOrUpdate(final MovimientoCreditos movimiento)
	{
		movimientoCreditosDao.saveOrUpdate(movimiento);
	}

	public void delete(final MovimientoCreditos movimiento)
	{
		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" attempting to delete creditsEntry: "+movimiento.toString());/*+ movimiento.getId()+
					" Type:"+movimiento.getTipoMovimientoCreditos().name()+
					" Agent name:"+ movimiento.getEmpleo().getPerson().getApellidoNombre()+
					" from status: "+movimiento.getGrantedStatus().name() + " to "+newEstado.name());*/	
		}
		
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.BajaAgente)){
			movimiento.getEmpleo().setFechaFin(null);
			movimiento.getEmpleo().setEstado(EmploymentStatus.ACTIVO);
			
			employmentService.saveOrUpdate(movimiento.getEmpleo());
			movimientoCreditosDao.delete(movimiento);
		}else
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.AscensoAgente)){
			
			//borrar movimiento 
			Empleo empleoPendiente = movimiento.getEmpleo();
			movimientoCreditosDao.delete(movimiento);
			//borrar empleo pendiente
			employmentService.delete(empleoPendiente);
		}else
		
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.IngresoAgente)){
			
			Person personToBeDeleted = movimiento.getEmpleo().getPerson();
			
			employmentService.delete(movimiento.getEmpleo());
			
			personService.delete(personToBeDeleted);
					
		}
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: delete creditsEntry - "+ movimiento.toString());	
		}

	}

	public MovimientoCreditosDao getMovimientoCreditosDao()
	{
		return movimientoCreditosDao;
	}


	public List<MovimientoCreditos> findAll(){
		return this.movimientoCreditosDao.findAll();
	}
	

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
	
	
	public List<MovimientoCreditosVO> buildMovimientoCreditosVO(
			List<MovimientoCreditos> movimientoCreditosReparticion, Account account) {
		List<MovimientoCreditosVO> movimientosCreditosVO = new ArrayList<MovimientoCreditosVO>();
		for(MovimientoCreditos movimientoCreditos:movimientoCreditosReparticion){
			MovimientoCreditosVO movimientoCreditosVO = new MovimientoCreditosVO();
			movimientoCreditosVO.setMovimientoCreditos(movimientoCreditos);
			if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente){
				Empleo empleoAnterior = movimientoCreditos.getEmpleo().getEmpleoAnterior();
				movimientoCreditosVO.setCurrentCategory(empleoAnterior.getCategory().getCode());
				movimientoCreditosVO.setProposedCategory(movimientoCreditos.getEmpleo().getCategory().getCode());
			}else{
				movimientoCreditosVO.setCurrentCategory(movimientoCreditos.getEmpleo().getCategory().getCode());
			}
			
			if(movimientoCreditos.getEmpleo().getOccupationalGroup()!=null){
				movimientoCreditosVO.setOccupationalGroup(movimientoCreditos.getEmpleo().getOccupationalGroup().getName());
				if(movimientoCreditos.getEmpleo().getOccupationalGroup().getParentOccupationalGroup()!=null){
					movimientoCreditosVO.setParentOccupationalGroup(movimientoCreditos.getEmpleo().getOccupationalGroup().getParentOccupationalGroup().getName());
				}
			}
			
			movimientoCreditosVO.setCanAccountBorrarMovimiento(canCreditsEntryBeDeletedByAccount(movimientoCreditos,account));
			movimientoCreditosVO.setCanAccountChangeCreditsEntryStatus(canAccountChangeCreditsEntryStatus(movimientoCreditos,account));
			
			movimientosCreditosVO.add(movimientoCreditosVO);
		}

			
		return movimientosCreditosVO;
	}
	
	public boolean canAccountChangeCreditsEntryStatus(MovimientoCreditos creditsEntry, Account account) {
		return canChangeCreditsEntryStatus(account) && canCreditsEntryStatusBeChanged(creditsEntry);
	}
	
	public boolean canCreditsEntryStatusBeChanged(
			MovimientoCreditos creditsEntry) {
		
		//only creditsEntries of Active periods can be changed 
		if(creditsEntry.getCreditsPeriod().getStatus()!=CreditsPeriod.Status.Active){
			return false;
		}else

		 if(creditsEntry.getTipoMovimientoCreditos()== TipoMovimientoCreditos.BajaAgente){
			 return false;
		 }else
		 if(creditsEntry.getTipoMovimientoCreditos()== TipoMovimientoCreditos.CargaInicialAgenteExistente){
			 return false;
		 }else
		 
		 if(creditsEntry.getTipoMovimientoCreditos()== TipoMovimientoCreditos.IngresoAgente){
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


	public boolean canCreditsEntryBeDeletedByAccount(MovimientoCreditos movimiento, Account account){
		
		return canDeleteCreditsEntry(account) && canCreditsEntryBeDeleted(movimiento);
		
	}
	
	public boolean canDeleteCreditsEntry(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "DELETE")){
			return true;
		}
		return false;
	}


	public boolean canCreditsEntryBeDeleted(MovimientoCreditos creditsEntry){
		
				
		if(isCreditsEntryClosed(creditsEntry)){
			return false;
		}
		
		if(creditsEntry.getTipoMovimientoCreditos()==TipoMovimientoCreditos.BajaAgente){
			return false;
		}
		
		if(creditsEntry.getTipoMovimientoCreditos()==TipoMovimientoCreditos.CargaInicialAgenteExistente){
			return false;
		}
		
		if(creditsEntry.getTipoMovimientoCreditos()==TipoMovimientoCreditos.IngresoAgente && creditsEntry.getGrantedStatus()==GrantedStatus.Otorgado){
			return false;
		}
		
		
		if(creditsEntry.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente && creditsEntry.getEmpleo().isClosed()){
			return false;
		}
				
	
		return true;
	}

	private boolean isCreditsEntryClosed(MovimientoCreditos creditsEntry) {
		
		if(creditsEntry.getCreditsPeriod().getStatus() == Status.Closed ){
			return true;
		}
		
		return false;
	}
	
	@Override
	public void actualizarCreditosPorAscenso(){
		//obtener movimientos de tipo Ascenso
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		List<MovimientoCreditos> movimientosCreditos = this.find(movimientoCreditosQueryFilter);
		for(MovimientoCreditos movimientoCreditos :movimientosCreditos){
			//Obtener el empleo del movimiento de tipo ascenso (EmpleoaActualizar)
			Empleo empleoaActualizar = movimientoCreditos.getEmpleo();
			//Obtener el empleo anterior al EmpleoaActualizar (EmpleoAnterior)
			Empleo empleoAnterior = empleoaActualizar.getEmpleoAnterior();
			//get the category of the previous employment
			//Obtener los creditos por ascenso para la categoria anterior y la categoria actual
			int nuevaCantidaddeCreditos = administradorCreditosService.getCreditosPorAscenso(empleoAnterior.getPerson().getCondition(), 
																							empleoAnterior.getCategory().getCode(),
																							empleoaActualizar.getCategory().getCode());
			movimientoCreditos.setCantidadCreditos(nuevaCantidaddeCreditos);
			this.saveOrUpdate(movimientoCreditos);
			
	
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
	public void changeGrantedStatus(MovimientoCreditos movimiento, GrantedStatus newEstado){
		

		Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(currentUser!=null){
			log.info("user:"+currentUser.getName()+" attempting to change granted status of movimiento:"+ movimiento.getId()+
					" Type:"+movimiento.getTipoMovimientoCreditos().name()+
					" Agent name:"+ movimiento.getEmpleo().getPerson().getApellidoNombre()+
					" from status: "+movimiento.getGrantedStatus().name() + " to "+newEstado.name());	
		}	
		
		if(movimiento.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente){
		
			if(movimiento.getGrantedStatus()==GrantedStatus.Solicitado){
				if(newEstado==GrantedStatus.Otorgado){
					Empleo empleoActual = movimiento.getEmpleo();
					
					empleoActual = employmentService.findById(empleoActual.getId());
					empleoActual.setEstado(EmploymentStatus.ACTIVO);
					
					employmentService.saveOrUpdate(empleoActual);
				
					Empleo empleoAnterior = employmentService.findById(empleoActual.getEmpleoAnterior().getId());
					
					empleoAnterior.setEstado(EmploymentStatus.INACTIVO);
								
					
					employmentService.saveOrUpdate(empleoAnterior);
				}
				
			}else{
					if(movimiento.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newEstado==GrantedStatus.Solicitado){
							Empleo empleoActual = movimiento.getEmpleo();
							
							empleoActual = employmentService.findById(empleoActual.getId());
							
							empleoActual.setEstado(EmploymentStatus.INACTIVO);
							
							employmentService.saveOrUpdate(empleoActual);
						
							Empleo empleoAnterior = employmentService.findById(empleoActual.getEmpleoAnterior().getId());
							
							empleoAnterior.setEstado(EmploymentStatus.ACTIVO);
							
							employmentService.saveOrUpdate(empleoAnterior);
													
						}
					}
				}
		
		}else 
			if(movimiento.getTipoMovimientoCreditos()==TipoMovimientoCreditos.IngresoAgente){
				if(movimiento.getGrantedStatus()==GrantedStatus.Solicitado){
					if(newEstado==GrantedStatus.Otorgado){
						Empleo empleo = movimiento.getEmpleo();
						
						empleo = employmentService.findById(empleo.getId());
						
						empleo.setEstado(EmploymentStatus.ACTIVO);
						employmentService.saveOrUpdate(empleo);
					}
				}else
					if(movimiento.getGrantedStatus()==GrantedStatus.Otorgado){
						if(newEstado==GrantedStatus.Solicitado){
							Empleo empleo = movimiento.getEmpleo();
							
							empleo = employmentService.findById(empleo.getId());
							
							empleo.setEstado(EmploymentStatus.INACTIVO);
							employmentService.saveOrUpdate(empleo);
						}
					}

			}
		
		movimiento.setGrantedStatus(newEstado);
		movimientoCreditosDao.saveOrUpdate(movimiento);
		
	}

	
	@Override
	public Set<Long> haveMovimientosSolicitados(List<Long> agentesIds, Long idReparticion,Long idCreditsPeriod) {
		
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setReparticionId(String.valueOf(idReparticion));
		empleoQueryFilter.setPersonsIds(agentesIds);
		empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.PENDIENTE);
		
		
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		
		movimientoCreditosQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.setIdCreditsPeriod(idCreditsPeriod);
		movimientoCreditosQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		
		
		List<MovimientoCreditos> movimientosSolicitados = this.find(movimientoCreditosQueryFilter);

		Set<Long> resultPersonsIds = new HashSet<Long>();
		for(MovimientoCreditos movimientoCreditos : movimientosSolicitados){
			resultPersonsIds.add(movimientoCreditos.getEmpleo().getPerson().getId());
		}
		
		return resultPersonsIds;
	}
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService empleoService) {
		this.employmentService = empleoService;
	}

}

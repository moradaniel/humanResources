package org.dpi.movimientoCreditos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.empleo.EmploymentService;
import org.dpi.empleo.EmploymentStatus;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
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
	//private final EmpleoService empleoService;
	
	//@Resource(name = "empleoService")
	private EmploymentService employmentService;
	
	private final AgenteService agenteService;
	private AdministradorCreditosService administradorCreditosService;
	


	private ApplicationContext applicationContext;
	
	public MovimientoCreditosServiceImpl(	final MovimientoCreditosDao movimientoCreditosDao,
											/*final EmpleoService empleoService,*/
											final AgenteService agenteService,
											final AdministradorCreditosService administradorCreditosService) {
		this.movimientoCreditosDao = movimientoCreditosDao;
		/*this.empleoService = empleoService;*/
		this.agenteService = agenteService;
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
					" Agent name:"+ movimiento.getEmpleo().getAgente().getApellidoNombre()+
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
			
			Agente agenteABorrar = movimiento.getEmpleo().getAgente();
			
			employmentService.delete(movimiento.getEmpleo());
			
			agenteService.delete(agenteABorrar);
					
		}
		
		if(currentUser!=null){
			log.info("================ user:"+currentUser.getName()+" Successfully performed: delete creditsEntry - "+ movimiento.toString());	
		}

	}


	/*public Hotel findByCode(String code) {
		return reparticionDao.findByCode(code);
	}


	public Hotel loadByCode(String code) throws EntityNotFoundException {
		return reparticionDao.loadByCode(code);
	}
	
	public Hotel loadByCode(String code,List<HotelFacet> facets) throws EntityNotFoundException {
		return reparticionDao.loadByCode(code,facets);
	}
	
	public void attachClean(Hotel hotel, List<HotelFacet> facets ){
		reparticionDao.attachClean(hotel, facets);
	}
	
	public List<Hotel> findByHotelAttribute(final String attrKey, final String attrValue)
	{
		return reparticionDao.findByHotelAttribute(attrKey, attrValue);
	}*/
	
	
	/*public Hotel findBySourceHotelCode(final ReservationChannel aSource, final String aRemoteHotelCode)
	{
	    return reparticionDao.findBySourceHotelCode(aSource, aRemoteHotelCode);	
	}*/
	
	/*
	public Hotel loadById(Integer id) throws EntityNotFoundException {
		return hotelDao.load(id);
	}
	*/
	
	/*
	public List loadAll() {
		return hotelDao.loadAll();
	}
	*/

/*
	public Hotel newHotel() {
		return reparticionDao.newHotel();
	}


	public Hotel newHotel(String code) {
		return reparticionDao.newHotel(code);
	}


	public RoomType newRoomType() {
		return reparticionDao.newRoomType();
	}


	public RoomType newRoomType(String code, String name) {
		return reparticionDao.newRoomType(code, name);
	}


	public RatePlan newRatePlan() {
		return reparticionDao.newRatePlan();
	}


	public RatePlan newRatePlan(String code, String name) {
		return reparticionDao.newRatePlan(code, name);
	}*/


	// private void removeOrphanPolicies(final Hotel aHotel) 
	// {
	// 	if (aHotel instanceof HotelImpl)
	// 	{
	// 		final HotelImpl hotel = (HotelImpl)aHotel;
	// 		final Iterator<Policy> it = hotel.getDeletedPolicies().iterator();
	// 		while (it.hasNext())
	// 		{
	// 	        final Policy policy = it.next();
	// 	        hotelDao.delete(policy);
	// 		}
	// 		
	// 		hotelDao.flush();
	// 	}
	// }

	public MovimientoCreditosDao getMovimientoCreditosDao()
	{
		return movimientoCreditosDao;
	}

	/*public List loadAllChains()
	{
		return reparticionDao.loadAllChains();
	}

	public HotelChain findChainByCode(String chainCode)
	{
		return reparticionDao.findChainByCode(chainCode);
	}*/


/*
	
	public HotelGroup findHotelGroup(final String aAccountCode, final String aGroupCode)
	{
		return reparticionDao.findHotelGroup(aAccountCode, aGroupCode);
	}
	
	
	public HotelGroup findHotelGroup(final String aGroupCode)
	{
		return reparticionDao.findHotelGroup(aGroupCode);
	}
	
	
	@SuppressWarnings("unchecked")
	public List<HotelGroup> findHotelGroups()
	{
		return reparticionDao.findHotelGroups();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<HotelGroup> findHotelGroups(final String aAccountCode)
	{
		return reparticionDao.findHotelGroups(aAccountCode);
	}
	
	
	public Object saveHotelGroup(final HotelGroup aHotelGroup)
	{
		return reparticionDao.saveHotelGroup(aHotelGroup);
	}
	
	public void updateHotelGroup(final HotelGroup aHotelGroup)
	{
		reparticionDao.updateHotelGroup(aHotelGroup);
	}
	
	public void deleteHotelGroup(final HotelGroup aHotelGroup)
	{
		reparticionDao.deleteHotelGroup(aHotelGroup);
	}

	public List<HotelSearchInfo> search(HotelSearchParameters parameters)
	{
		return reparticionDao.search(parameters);
	}
	*/
	

	
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
				movimientoCreditosVO.setCategoriaActual(empleoAnterior.getCategoria().getCodigo());
				movimientoCreditosVO.setCategoriaPropuesta(movimientoCreditos.getEmpleo().getCategoria().getCodigo());
			}else{
				movimientoCreditosVO.setCategoriaActual(movimientoCreditos.getEmpleo().getCategoria().getCodigo());
			}
			
			movimientoCreditosVO.setCanAccountBorrarMovimiento(canMovimientoBeDeletedByAccount(movimientoCreditos,account));
			movimientoCreditosVO.setCanAccountCambiarEstadoMovimiento(canAccountCambiarEstadoDelMovimiento(movimientoCreditos,account));
			
			movimientosCreditosVO.add(movimientoCreditosVO);
		}

			
		return movimientosCreditosVO;
	}
	
	public boolean canAccountCambiarEstadoDelMovimiento(MovimientoCreditos movimientoCreditos, Account account) {
		return canAccountCambiarEstadoMovimientos(account) && canMovimientoBeStatusChanged(movimientoCreditos);
	}
	
	public boolean canMovimientoBeStatusChanged(
			MovimientoCreditos movimientoCreditos) {

		 if(movimientoCreditos.getTipoMovimientoCreditos()== TipoMovimientoCreditos.BajaAgente){
			 return false;
		 }
		 if(movimientoCreditos.getTipoMovimientoCreditos()== TipoMovimientoCreditos.CargaInicialAgenteExistente){
			 return false;
		 }
		 
		 if(movimientoCreditos.getTipoMovimientoCreditos()== TipoMovimientoCreditos.IngresoAgente){
			 if(movimientoCreditos.getCantidadCreditos()==0){
				 return false;
			 }
		 }

		 
		 return true;
	}


	public static boolean canAccountCambiarEstadoMovimientos(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "UPDATE_STATUS")){
			return true;
		}
		return false;
	}


	public boolean canMovimientoBeDeletedByAccount(MovimientoCreditos movimiento, Account account){
		
		return canAccountDeleteMovimientos(account) && canMovimientoBeDeleted(movimiento);
		
	}
	
	public boolean canAccountDeleteMovimientos(Account account) {
		
		if(account.hasPermissions("Manage_MovimientoCreditos", "DELETE")){
			return true;
		}
		return false;
	}


	public boolean canMovimientoBeDeleted(MovimientoCreditos movimientoCreditos){
		
				
		if(isMovimientoCerrado(movimientoCreditos)){
			return false;
		}
		
		if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.BajaAgente){
			return false;
		}
		
		if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.CargaInicialAgenteExistente){
			return false;
		}
		
		if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.IngresoAgente && movimientoCreditos.getGrantedStatus()==GrantedStatus.Otorgado){
			return false;
		}
		
		
		if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente && movimientoCreditos.getEmpleo().isClosed()){
			return false;
		}
				
	
		return true;
	}

	private boolean isMovimientoCerrado(MovimientoCreditos movimientoCreditos) {
		
		if(movimientoCreditos.getCreditsPeriod().getStatus() == Status.Closed ){
			return true;
		}
		
		return false;
	}


	/*public void save(final MovimientoCreditos movimientoCreditos) 
	{
		movimientoCreditosDao.save(movimientoCreditos);
	}*/
	
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
			//Obtener la categoria del EmpleoAnterior
			//Obtener los creditos por ascenso para la categoria anterior y la categoria actual
			int nuevaCantidaddeCreditos = administradorCreditosService.getCreditosPorAscenso(empleoAnterior.getAgente().getCondicion(), 
																							empleoAnterior.getCategoria().getCodigo(),
																							empleoaActualizar.getCategoria().getCodigo());
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
					" Agent name:"+ movimiento.getEmpleo().getAgente().getApellidoNombre()+
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
		empleoQueryFilter.setAgentesIds(agentesIds);
		empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.PENDIENTE);
		
		
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		
		movimientoCreditosQueryFilter.setEmploymentQueryFilter(empleoQueryFilter);
		movimientoCreditosQueryFilter.setIdCreditsPeriod(idCreditsPeriod);
		movimientoCreditosQueryFilter.addGrantedStatus(GrantedStatus.Solicitado);
		
		
		List<MovimientoCreditos> movimientosSolicitados = this.find(movimientoCreditosQueryFilter);

		Set<Long> resultAgentesIds = new HashSet<Long>();
		for(MovimientoCreditos movimientoCreditos : movimientosSolicitados){
			resultAgentesIds.add(movimientoCreditos.getEmpleo().getAgente().getId());
		}
		
		return resultAgentesIds;
	}
	
	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService empleoService) {
		this.employmentService = empleoService;
	}

}

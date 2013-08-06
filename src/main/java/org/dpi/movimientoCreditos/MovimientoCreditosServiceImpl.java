package org.dpi.movimientoCreditos;

import java.util.ArrayList;
import java.util.List;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmpleoQueryFilter;
import org.dpi.empleo.EmpleoService;
import org.dpi.empleo.EstadoEmpleo;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class MovimientoCreditosServiceImpl implements MovimientoCreditosService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final MovimientoCreditosDao movimientoCreditosDao;
	private final EmpleoService empleoService;
	private final AgenteService agenteService;
	private AdministradorCreditosService administradorCreditosService;
	


	private ApplicationContext applicationContext;
	
	public MovimientoCreditosServiceImpl(	final MovimientoCreditosDao movimientoCreditosDao,
											final EmpleoService empleoService,
											final AgenteService agenteService,
											final AdministradorCreditosService administradorCreditosService) {
		this.movimientoCreditosDao = movimientoCreditosDao;
		this.empleoService = empleoService;
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
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.BajaAgente)){
			movimiento.getEmpleo().setFechaFin(null);
			movimiento.getEmpleo().setEstado(EstadoEmpleo.ACTIVO);
			
			empleoService.saveOrUpdate(movimiento.getEmpleo());
			movimientoCreditosDao.delete(movimiento);
		}
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.AscensoAgente)){
			//encontrar empleo anterior
			EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
			empleoQueryFilter.setCuil(movimiento.getEmpleo().getAgente().getCuil());
			empleoQueryFilter.setReparticionId(movimiento.getEmpleo().getCentroSector().getReparticion().getId().toString());
			
			List<Empleo> listaEmpleosAnteriores = empleoService.findEmpleosInactivos(empleoQueryFilter);
			
			Empleo empleoAnterior = listaEmpleosAnteriores.get(0);
			
			//borrar fecha fin
			empleoAnterior.setFechaFin(null);
			empleoService.saveOrUpdate(empleoAnterior);
			
			//borrar movimiento actual
			Empleo empleoActual = movimiento.getEmpleo();
			movimientoCreditosDao.delete(movimiento);
			//borrar empleo actual
			empleoService.delete(empleoActual);
		}
		
		if(movimiento.getTipoMovimientoCreditos().equals(TipoMovimientoCreditos.IngresoAgente)){
			
			Agente agenteABorrar = movimiento.getEmpleo().getAgente();
			
			empleoService.delete(movimiento.getEmpleo());
			
			agenteService.delete(agenteABorrar);
					
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
	
	
	public List<MovimientoCreditosAscensoVO> buildMovimientoCreditosVO(
			List<MovimientoCreditos> movimientoCreditosReparticion, Account account) {
		List<MovimientoCreditosAscensoVO> movimientosCreditosVO = new ArrayList<MovimientoCreditosAscensoVO>();
		for(MovimientoCreditos movimientoCreditos:movimientoCreditosReparticion){
			MovimientoCreditosAscensoVO movimientoCreditosVO = new MovimientoCreditosAscensoVO();
			movimientoCreditosVO.setMovimientoCreditos(movimientoCreditos);
			if(movimientoCreditos.getTipoMovimientoCreditos()==TipoMovimientoCreditos.AscensoAgente){
				//Empleo empleoPrevio = empleoService.findPreviousEmpleo(movimientoCreditos.getEmpleo());
				Empleo empleoPrevio = movimientoCreditos.getEmpleo().getEmpleoAnterior();
				movimientoCreditosVO.setCategoriaActual(empleoPrevio.getCategoria().getCodigo());
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

		 
		 return true;
	}


	public boolean canAccountCambiarEstadoMovimientos(Account account) {
		
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
	
	public void actualizarCreditosPorAscenso(){
		//obtener movimientos de tipo Ascenso
		MovimientoCreditosQueryFilter movimientoCreditosQueryFilter = new MovimientoCreditosQueryFilter();
		movimientoCreditosQueryFilter.addTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		List<MovimientoCreditos> movimientosCreditos = this.find(movimientoCreditosQueryFilter);
		for(MovimientoCreditos movimientoCreditos :movimientosCreditos){
			//Obtener el empleo del movimiento de tipo ascenso (EmpleoaActualizar)
			Empleo empleoaActualizar = movimientoCreditos.getEmpleo();
			//Obtener el empleo anterior al EmpleoaActualizar (EmpleoAnterior)
			Empleo empleoAnterior = empleoService.findPreviousEmpleo(empleoaActualizar);
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
	

}

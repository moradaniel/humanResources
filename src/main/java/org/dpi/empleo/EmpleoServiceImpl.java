package org.dpi.empleo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.agente.Agente;
import org.dpi.agente.AgenteImpl;
import org.dpi.agente.AgenteQueryFilter;
import org.dpi.agente.AgenteService;
import org.dpi.agente.EstadoAgente;
import org.dpi.categoria.Categoria;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSector;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.movimientoCreditos.MovimientoCreditos;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
import org.dpi.movimientoCreditos.MovimientoCreditosImpl;
import org.dpi.movimientoCreditos.TipoMovimientoCreditos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;



public class EmpleoServiceImpl implements EmpleoService
{
	//private static Log log = LogFactory.getLog(EmpleoServiceImpl.class);
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final EmpleoDao empleoDao;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "agenteService")
	private AgenteService agenteService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	

	private ApplicationContext applicationContext;
	
	public EmpleoServiceImpl(final EmpleoDao empleoDao) {
		this.empleoDao = empleoDao;
	}

	
	public List<Empleo> find(EmpleoQueryFilter empleoQueryFilter){
		
		return empleoDao.find(empleoQueryFilter);
	}

	public Empleo findById(Long id){
		return empleoDao.findById(id);
	}
	
	public void save(final Empleo empleo) 
	{
		empleoDao.save(empleo);
		//hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void saveOrUpdate(final Empleo empleo) 
	{
		//empleoDao.saveOrUpdate(empleo);
		if (empleo.getId() != null) { // it is an update
			 empleoDao.merge(empleo);
			} else { // you are saving a new one
			 empleoDao.saveOrUpdate(empleo);
			}
		
	//	hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void delete(Empleo empleo){
		empleoDao.delete(empleo);
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

	public EmpleoDao getEmpleoDao()
	{
		return empleoDao;
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
	

	
	public List<Empleo> findAll(){
		return this.empleoDao.findAll();
	}
	
	



	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public void darDeBaja(Empleo empleo) {
		
		//encontrar empleo
		
		
		//ponerle fecha fin la fecha actual
		empleo.setFechaFin(new Date());
		empleo.setEstado(EstadoEmpleo.BAJA);
		
		//crear un movimiento de tipo baja 
		MovimientoCreditosImpl movimientoBaja = new MovimientoCreditosImpl();
		movimientoBaja.setTipoMovimientoCreditos(TipoMovimientoCreditos.BajaAgente);
		int cantidadCreditosPorBaja = this.getAdministradorCreditosService().getCreditosPorBaja(empleo.getCategoria().getCodigo());

		
		movimientoBaja.setCantidadCreditos(cantidadCreditosPorBaja);
		
		
		//setear empleo a movimiento  y agregar movimiento a empleo
		empleo.addMovimientoCreditos(movimientoBaja);
		
		movimientoBaja.setEmpleo(empleo);
		
		//guardar movimiento y empleo
		saveOrUpdate(empleo);
		
	}
	
	@Override
	public void ascenderAgente(Empleo empleoActual, String codigoCategoriaNueva){
		
		
		if(hasMovimientosAscensoPendientes(empleoActual.getAgente().getId(), empleoActual.getCentroSector().getReparticion().getId())){
			return;
		}
		
		
		//ponerle fecha fin la fecha actual
		//empleoActual.setFechaFin(new Date());
		
		Agente agente = empleoActual.getAgente();
		
		//guardar empleo
		//saveOrUpdate(empleoActual);

		Categoria categoriaNueva = categoriaService.findByCodigo(codigoCategoriaNueva);
		Empleo empleoNuevo = new EmpleoImpl();
		empleoNuevo.setAgente(empleoActual.getAgente());
		empleoNuevo.setCategoria(categoriaNueva);
		empleoNuevo.setCentroSector(empleoActual.getCentroSector());
		empleoNuevo.setFechaInicio(new Date());
		empleoNuevo.setEstado(EstadoEmpleo.PENDIENTE);
		
		//crear un movimiento de tipo ascenso 
		MovimientoCreditosImpl movimientoAscenso = new MovimientoCreditosImpl();
		movimientoAscenso.setTipoMovimientoCreditos(TipoMovimientoCreditos.AscensoAgente);
		int cantidadCreditosPorAscenso = this.getAdministradorCreditosService().getCreditosPorAscenso(agente.getCondicion(),empleoActual.getCategoria().getCodigo(),codigoCategoriaNueva);
		movimientoAscenso.setGrantedStatus(GrantedStatus.Solicitado);

		
		movimientoAscenso.setCantidadCreditos(cantidadCreditosPorAscenso);
		
		
		//setear empleo a movimiento  y agregar movimiento a empleo
		empleoNuevo.addMovimientoCreditos(movimientoAscenso);
		
		movimientoAscenso.setEmpleo(empleoNuevo);
		empleoNuevo.setEmpleoAnterior(empleoActual);
		
		
		//guardar movimiento y empleo
		saveOrUpdate(empleoNuevo);
		
	}
	
	private boolean hasMovimientosAscensoPendientes(Long idAgente, Long idReparticion) {
		AgenteQueryFilter agenteQueryFilter = new AgenteQueryFilter();
		agenteQueryFilter.setAgenteId(idAgente);
		agenteQueryFilter.setReparticionId(idReparticion);
		agenteQueryFilter.setEstadoAgente(EstadoAgente.ACTIVO);
		

		List<Agente> agentes = agenteService.find(agenteQueryFilter);
		
		Agente agente = agentes.get(0);
		
		if (agente.hasMovimientosAscensoPendientes()){
			return true;
		}

		return false;
	}


	public List<Empleo> findEmpleosInactivos(final EmpleoQueryFilter empleoQueryFilter){
		empleoQueryFilter.addEstadoEmpleo(EstadoEmpleo.INACTIVO);
		
		return this.empleoDao.findEmpleosInactivos(empleoQueryFilter);
	}
	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}


	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public CategoriaService getCategoriaService() {
		return categoriaService;
	}


	public void setCategoriaService(CategoriaService categoriaService) {
		this.categoriaService = categoriaService;
	}


	@Override
	public Empleo findPreviousEmpleo(Empleo empleo){
		EmpleoQueryFilter empleoQueryFilter = new EmpleoQueryFilter();
		empleoQueryFilter.setCuil(empleo.getAgente().getCuil());
		empleoQueryFilter.setReparticionId(empleo.getCentroSector().getReparticion().getId().toString());
		
		empleoQueryFilter.setEstadosEmpleo( CollectionUtils.arrayToList(EstadoEmpleo.values()));
		empleoQueryFilter.setFechaFin(empleo.getFechaInicio());

		return this.empleoDao.findPreviousEmpleo(empleoQueryFilter);
	}


	@Override
	public void ingresarPropuestaAgente(String codigoCategoriaPropuesta,Long centroSectorId) {
		// crear agente nn
		Agente nuevoAgentePropuesto = createPendingAgent();
		
		agenteService.save(nuevoAgentePropuesto);
		
		//al agente ponerlo en estado pendiente
		
		//crear empleo
		Empleo nuevoEmpleoPropuesto = new EmpleoImpl();
		nuevoEmpleoPropuesto.setAgente(nuevoAgentePropuesto);
		//setear categoria propuesta al empleo
		nuevoEmpleoPropuesto.setCategoria(categoriaService.findByCodigo(codigoCategoriaPropuesta));
		//al empleo ponerlo en estado pendiente
		nuevoEmpleoPropuesto.setEstado(EstadoEmpleo.PENDIENTE);
		
		nuevoEmpleoPropuesto.setFechaInicio(new Date());
		
		//buscar centro sector
		CentroSector centroSector = centroSectorService.findById(centroSectorId);
		nuevoEmpleoPropuesto.setCentroSector(centroSector);
		
		//Crear movimiento de ingreso
		MovimientoCreditos movimientoCreditosIngreso = new MovimientoCreditosImpl();
		movimientoCreditosIngreso.setTipoMovimientoCreditos(TipoMovimientoCreditos.IngresoAgente);
		
		movimientoCreditosIngreso.setEmpleo(nuevoEmpleoPropuesto);
		nuevoEmpleoPropuesto.addMovimientoCreditos(movimientoCreditosIngreso);
		
		int creditosPorIngreso = administradorCreditosService.getCreditosPorIngreso(codigoCategoriaPropuesta);
		movimientoCreditosIngreso.setCantidadCreditos(creditosPorIngreso);
		
		this.save(nuevoEmpleoPropuesto);
		
	}
	
	public AgenteService getAgenteService() {
		return agenteService;
	}


	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}
	
	private Agente createPendingAgent(){
		AgenteImpl newAgente = new AgenteImpl();
		newAgente.setApellidoNombre("Ingreso Nuevo Propuesto");
		newAgente.setCuil("");
		return newAgente;
	}
	
	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}
	

	
}

package org.dpi.empleo;

import java.util.List;

import javax.annotation.Resource;

import org.dpi.agente.AgenteService;
import org.dpi.categoria.CategoriaService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;



public class EmploymentServiceImpl implements EmploymentService
{
	//private static Log log = LogFactory.getLog(EmpleoServiceImpl.class);
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final EmploymentDao empleoDao;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
	@Resource(name = "categoriaService")
	private CategoriaService categoriaService;
	
	@Resource(name = "agenteService")
	private AgenteService agenteService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	private ApplicationContext applicationContext;
	
	public EmploymentServiceImpl(final EmploymentDao empleoDao) {
		this.empleoDao = empleoDao;
	}

	
	public List<Empleo> find(EmploymentQueryFilter empleoQueryFilter){
		
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

	public EmploymentDao getEmploymentDao()
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




	



	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter){
		empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.INACTIVO);
		
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
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setCuil(empleo.getAgente().getCuil());
		empleoQueryFilter.setReparticionId(empleo.getCentroSector().getReparticion().getId().toString());
		
		empleoQueryFilter.setEstadosEmpleo( CollectionUtils.arrayToList(EmploymentStatus.values()));
		empleoQueryFilter.setFechaFin(empleo.getFechaInicio());

		return this.empleoDao.findPreviousEmpleo(empleoQueryFilter);
	}


	
	public AgenteService getAgenteService() {
		return agenteService;
	}


	public void setAgenteService(AgenteService agenteService) {
		this.agenteService = agenteService;
	}
	

	
	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}

	
}

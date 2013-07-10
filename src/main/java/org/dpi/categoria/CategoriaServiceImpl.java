package org.dpi.categoria;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class CategoriaServiceImpl implements CategoriaService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final CategoriaDao categoriaDao;
	private ApplicationContext applicationContext;
	
	public CategoriaServiceImpl(final CategoriaDao categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

	
	public Categoria findByCodigo(String codigo){
		
		return categoriaDao.findByCodigo(codigo);
	}

	
	public void save(final Categoria categoria) 
	{
		categoriaDao.save(categoria);
		//hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void saveOrUpdate(final Categoria categoria) 
	{
		categoriaDao.saveOrUpdate(categoria);
	//	hotelDao.removeOrphanPolicies(aHotel);
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

	public CategoriaDao getCategoriaDao()
	{
		return categoriaDao;
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
	

	
	public List<Categoria> findAll(){
		return this.categoriaDao.findAll();
	}
	
	



	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
}

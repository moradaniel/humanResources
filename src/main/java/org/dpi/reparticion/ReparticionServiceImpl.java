package org.dpi.reparticion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.travel.SearchCriteria;



public class ReparticionServiceImpl implements ReparticionService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final ReparticionDao reparticionDao;
	private ApplicationContext applicationContext;
	
	public ReparticionServiceImpl(final ReparticionDao reparticionDao) {
		this.reparticionDao = reparticionDao;
	}
	
	public Reparticion findById(Long id){
		return this.reparticionDao.findById(id);
	}
	
	public Reparticion findByNombre(String nombre){
		return this.reparticionDao.findByNombre(nombre);
	}
	
	public List<ReparticionSearchInfo> findAllReparticiones(){
		return reparticionDao.findAllReparticiones();
	}

	public void save(final Reparticion aReparticion) 
	{
		reparticionDao.save(aReparticion);
		//hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void saveOrUpdate(final Reparticion aReparticion) 
	{
		reparticionDao.saveOrUpdate(aReparticion);
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

	public ReparticionDao getReparticionDao()
	{
		return reparticionDao;
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
	

	
	public List<Reparticion> findAll(){
		return reparticionDao.findAll();
	}
	
	



	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	/*@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")*/
	public List<Reparticion> findReparticiones(SearchCriteria criteria) {
		return reparticionDao.findReparticiones(criteria);
	}
	

}

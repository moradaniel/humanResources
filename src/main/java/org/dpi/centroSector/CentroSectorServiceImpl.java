package org.dpi.centroSector;

import java.util.List;

import org.dpi.reparticion.Reparticion;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class CentroSectorServiceImpl implements CentroSectorService
{
	Logger log = LoggerFactory.getLogger(this.getClass());;
	private final CentroSectorDao centroSectorDao;
	private ApplicationContext applicationContext;
	
	public CentroSectorServiceImpl(final CentroSectorDao reparticionDao) {
		this.centroSectorDao = reparticionDao;
	}

	
	public CentroSector findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector){
		
		return centroSectorDao.findByCodigoCentroCodigoSector(codigoCentro,codigoSector);
	}

	
	public void save(final CentroSector aReparticion) 
	{
		centroSectorDao.save(aReparticion);
		//hotelDao.removeOrphanPolicies(aHotel);
	}
	
	public void saveOrUpdate(final CentroSector aReparticion) 
	{
		centroSectorDao.saveOrUpdate(aReparticion);
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
	
	
	public CentroSector findById(Long id) throws EntityNotFoundException {
		return centroSectorDao.findById(id);
	}
	
	
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

	public CentroSectorDao getCentroSectorDao()
	{
		return centroSectorDao;
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
	

	
	public List<CentroSector> findAll(){
		return this.centroSectorDao.findAll();
	}
	
	



	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public PageList<CentroSector> findCentroSectores(QueryBind bind,
			CentroSectorQueryFilter filter, boolean isForExcel) {
		return this.centroSectorDao.findCentroSectores(bind, filter, isForExcel);
	}
	
	@Override
	public List<CentroSector> findCentroSectores(final Reparticion reparticion){
		return this.centroSectorDao.findCentroSectores(reparticion);
	}
}

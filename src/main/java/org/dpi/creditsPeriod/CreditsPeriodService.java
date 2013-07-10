package org.dpi.creditsPeriod;

import java.util.Date;
import java.util.List;

import org.dpi.empleo.Empleo;
import org.dpi.empleo.EmpleoDao;
import org.dpi.empleo.EmpleoQueryFilter;
import org.springframework.context.ApplicationContextAware;

import biz.janux.calendar.DateRange;


/**
 * Used to create, save, retrieve, update and delete Hotel objects from
 * persistent storage
 *
 */
public interface CreditsPeriodService extends ApplicationContextAware
{
	
	/**
	 * DateRange that represents the period of time representing a Ejercicio Anual
	 * @param year
	 * @return
	 */
	public DateRange getDateRangeForEjercicioAnual(int year);
	
	
	public boolean isDateWithinEjercicioAnual(Date date, int year);
	
	public int getCurrentEjercicioAnual();
	
	
	/**
	 * Returns a possibly lightweight representation of the corresponding Hotel, which may not
	 * contain all associated objects, or <code>null</code> if the Hotel is not found.
	 *
	 * @param code a business code that uniquely identifies this Hotel
	 */
	public List<Empleo> find(EmpleoQueryFilter empleoQueryFilter);


	public void darDeBaja(Empleo empleoQueryFilter);
	
	public void ascenderAgente(Empleo empleo, String codigoCategoriaNueva);
	
	public List<Empleo> findEmpleosInactivos(final EmpleoQueryFilter empleoQueryFilter);
	
	public void delete(Empleo empleo);
	
	/**
	 * Deep loads an Hotel object, or throws exception if Hotel with
	 * that code is not found
	 *
	 * @param code a business code that uniquely identifies this Hotel
	 * @return an object graph representing this Hotel and including associated objects
	 *
	 * @throws EntityNotFoundException if a Hotel object with that id is not found
	 */
	//public Hotel loadByCode(String code) throws EntityNotFoundException;
	
	/**
	 * Load a hotel by the given hotel code and load a list o relations
	 * @param code
	 * @param facets the list of relations to load. If null, only the default set of relations
	 * will be loaded
	 * @return
	 */
	//public Hotel loadByCode(String code,List<HotelFacet> facets) throws EntityNotFoundException;
	
	/**
	 * Reattach an object with the current hibernate session. 
	 * This will not execute a select to the database so we have to be sure
	 * that the detached instance has not been modified.
	 * 
	 * @param hotel the hotel to reattach
	 * @param facets list of relations to load
	 */
	
	//public void attachClean(final Hotel hotel, List<HotelFacet> facets);

	//public Hotel loadById(Integer id) throws EntityNotFoundException;
	
	/**
	 * Returns a list of all hotels with a HotelAttribute matching the specified 
	 * attribute key and value.
	 */
	//public List<Hotel> findByHotelAttribute(final String attrKey, final String attrValue);
	
	//public Hotel findBySourceHotelCode(final ReservationChannel aSource, final String aRemoteHotelCode);
	
	/** 
	 * returns a list of all hotels in the system; this is here to facilitate
	 * development for the time being, but is highly unlikely to be here in in
	 * this form in the production release and so should not be used other than
	 * for temporary purposes (such as loading hotel test records)
	 */
	// public List loadAll();

	/** returns a new Hotel instance */
	//public Hotel newHotel();

	/** returns a new Hotel instance with the given identifier */
	//public Hotel newHotel(String code);


	/** returns a new RoomType instance */
	//public RoomType newRoomType();

	/** returns a new RoomType instance with the given identifier and name */
	//public RoomType newRoomType(String code, String name);


	/** returns a new RatePlan instance */
	//public RatePlan newRatePlan();

	/** returns a new RatePlan instance with the given identifier and name */
	//public RatePlan newRatePlan(String code, String name);
	
	public void save(final Empleo empleo); 
	
	public void saveOrUpdate(final Empleo empleo); 
	
	public EmpleoDao getEmpleoDao();


	public Empleo findPreviousEmpleo(Empleo empleo);


	public void ingresarPropuestaAgente(String codigoCategoriaPropuesta,Long centroSectorId);

	/*public HotelChain findChainByCode(String chainCode);
	public List loadAllChains();
	
	public void assureValid(HotelAdminInfo hotelAdminInfo) throws IllegalStateException;
	
	public HotelGroup findHotelGroup(final String aAccountCode, final String aGroupCode);
	public HotelGroup findHotelGroup(final String aGroupCode);
	public List<HotelGroup> findHotelGroups();
	public List<HotelGroup> findHotelGroups(final String aAccountCode);
	
	public Object saveHotelGroup(final HotelGroup aHotelGroup);
	public void updateHotelGroup(final HotelGroup aHotelGroup);
	public void deleteHotelGroup(final HotelGroup aHotelGroup);
	
	public List<HotelSearchInfo> search(HotelSearchParameters parameters);
	
	
	public Set<NotifyTemplate> getHotelNotifications(final String aHotelCode, final String aTemplateType);
	
	public void saveHotelNotification(final String aHotelCode, final NotifyTemplate aNotifyTemplate);
	
	public void deleteHotelNotification(final String aHotelCode, final String aTemplateType, final String aCode);
	
	public List<String> findHotelsByStatus(HotelStatus status);
	
	public List<HotelSearchInfo> findAll();
	
	public HotelCopyResults copyHotel(final HotelCopyCommand aCommand);*/
}

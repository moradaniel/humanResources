package org.dpi.empleo;

import java.util.List;

import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Hotel objects from
 * persistent storage
 *
 */
public interface EmpleoService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding Hotel, which may not
	 * contain all associated objects, or <code>null</code> if the Hotel is not found.
	 *
	 * @param code a business code that uniquely identifies this Hotel
	 */
	public List<Empleo> find(EmpleoQueryFilter empleoQueryFilter);

	public Empleo findById(Long id);
	
	public void darDeBaja(Empleo empleoQueryFilter);
	
	public void ascenderAgente(Empleo empleo, String codigoCategoriaNueva);
	
	public List<Empleo> findEmpleosInactivos(final EmpleoQueryFilter empleoQueryFilter);
	
	public void delete(Empleo empleo);
	
	
	
	public void save(final Empleo empleo); 
	
	public void saveOrUpdate(final Empleo empleo); 
	
	public EmpleoDao getEmpleoDao();


	//Deprecated
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

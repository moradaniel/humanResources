package org.dpi.reparticion;

import java.util.List;

import org.springframework.context.ApplicationContextAware;
import org.springframework.samples.travel.SearchCriteria;


/**
 * Used to create, save, retrieve, update and delete Reparticion objects from
 * persistent storage
 *
 */
public interface ReparticionService extends ApplicationContextAware
{
	
	public Reparticion findById(Long id);
	
	
	/**
	 * Returns a possibly lightweight representation of the corresponding Reparticion, which may not
	 * contain all associated objects, or <code>null</code> if the Reparticion is not found.
	 *
	 * @param code a business code that uniquely identifies this Reparticion
	 */
	public Reparticion findByNombre(String nombre);
	
	public List<ReparticionSearchInfo> findAllReparticiones();
	
	public void save(final Reparticion aReparticion); 
	
	public void saveOrUpdate(final Reparticion aReparticion); 
	
	public ReparticionDao getReparticionDao();

	public List<Reparticion> findReparticiones(SearchCriteria criteria);

}

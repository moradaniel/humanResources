package org.dpi.agente;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Agente objects from
 * persistent storage
 *
 */
public interface AgenteService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding Agente, which may not
	 * contain all associated objects, or <code>null</code> if the Agente is not found.
	 *
	 * @param code a business code that uniquely identifies this Agente
	 */
	public Agente findByCuil(String cuil);
	
	
	public PageList<Agente> findAgentes(final QueryBind bind,
			   final AgenteQueryFilter filter,
			   boolean isForExcel);

	public void delete(Agente agente);
	
	public void save(final Agente agente); 
	
	public void saveOrUpdate(final Agente agente); 
	
	public AgenteDao getAgenteDao();

	public List<Agente> find(AgenteQueryFilter agenteQueryFilter);


}

package org.dpi.agente;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface AgenteDao extends HibernateDataAccessObject
{

	public List<Agente> findAll();
	
	public Agente findByCuil(String cuil);
	
	public PageList<Agente> findAgentes(final QueryBind bind,
			   final AgenteQueryFilter filter,
			   boolean isForExcel);

	public List<Agente> find(AgenteQueryFilter agenteQueryFilter);
	
}

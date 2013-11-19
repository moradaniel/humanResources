package org.dpi.person;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface PersonDao extends HibernateDataAccessObject
{

	public List<Person> findAll();
	
	public Person findByCuil(String cuil);
	
	public PageList<Person> findAgentes(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel);

	public List<Person> find(PersonQueryFilter agenteQueryFilter);
	
}

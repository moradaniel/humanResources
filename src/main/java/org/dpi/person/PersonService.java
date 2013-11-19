package org.dpi.person;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Agente objects from
 * persistent storage
 *
 */
public interface PersonService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding Agente, which may not
	 * contain all associated objects, or <code>null</code> if the Agente is not found.
	 *
	 * @param code a business code that uniquely identifies this Agente
	 */
	public Person findByCuil(String cuil);
	
	
	public PageList<Person> findAgentes(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel);

	public void delete(Person agente);
	
	public void save(final Person agente); 
	
	public void saveOrUpdate(final Person agente); 
	
	public PersonDao getAgenteDao();

	public List<Person> find(PersonQueryFilter agenteQueryFilter);


}

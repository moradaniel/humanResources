package org.dpi.person;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Person objects from
 * persistent storage
 *
 */
public interface PersonService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding Person, which may not
	 * contain all associated objects, or <code>null</code> if the Person is not found.
	 *
	 * @param code a business code that uniquely identifies this Person
	 */
	public Person findByCuil(String cuil);
	
	
	public PageList<Person> findPersons(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel);

	public void delete(Person person);
	
	public void save(final Person person); 
	
	public void saveOrUpdate(final Person person); 
	
	public PersonDao getPersonDao();

	public List<Person> find(PersonQueryFilter personQueryFilter);


}

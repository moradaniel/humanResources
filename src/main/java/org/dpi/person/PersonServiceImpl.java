package org.dpi.person;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class PersonServiceImpl implements PersonService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final PersonDao personDao;
	private ApplicationContext applicationContext;
	
	public PersonServiceImpl(final PersonDao personDao) {
		this.personDao = personDao;
	}

	
	public Person findByCuil(String cuil){
		
		return personDao.findByCuil(cuil);
	}

	
	public void save(final Person person) 
	{
		personDao.save(person);
	}
	
	public void delete(Person person){
		personDao.delete(person);
	}
	
	public void saveOrUpdate(final Person person) 
	{
		personDao.saveOrUpdate(person);
	}


	public PersonDao getPersonDao()
	{
		return personDao;
	}

	
	public List<Person> findAll(){
		return this.personDao.findAll();
	}
	
	public PageList<Person> findPersons(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel){
		
		return this.personDao.findPersons(bind, filter, isForExcel);
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public List<Person> find(PersonQueryFilter personQueryFilter) {
		return this.personDao.find(personQueryFilter);
	}
}

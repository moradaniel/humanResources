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
	private final PersonDao agenteDao;
	private ApplicationContext applicationContext;
	
	public PersonServiceImpl(final PersonDao agenteDao) {
		this.agenteDao = agenteDao;
	}

	
	public Person findByCuil(String cuil){
		
		return agenteDao.findByCuil(cuil);
	}

	
	public void save(final Person agente) 
	{
		agenteDao.save(agente);
	}
	
	public void delete(Person agente){
		agenteDao.delete(agente);
	}
	
	public void saveOrUpdate(final Person agente) 
	{
		agenteDao.saveOrUpdate(agente);
	}


	public PersonDao getAgenteDao()
	{
		return agenteDao;
	}

	
	public List<Person> findAll(){
		return this.agenteDao.findAll();
	}
	
	public PageList<Person> findAgentes(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel){
		
		return this.agenteDao.findAgentes(bind, filter, isForExcel);
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public List<Person> find(PersonQueryFilter agenteQueryFilter) {
		return this.agenteDao.find(agenteQueryFilter);
	}
}

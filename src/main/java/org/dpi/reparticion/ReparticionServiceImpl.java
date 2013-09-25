package org.dpi.reparticion;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.samples.travel.SearchCriteria;



public class ReparticionServiceImpl implements ReparticionService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final ReparticionDao reparticionDao;
	private ApplicationContext applicationContext;
	
	public ReparticionServiceImpl(final ReparticionDao reparticionDao) {
		this.reparticionDao = reparticionDao;
	}
	
	public Reparticion findById(Long id){
		return this.reparticionDao.findById(id);
	}
	
	public Reparticion findByNombre(String nombre){
		return this.reparticionDao.findByNombre(nombre);
	}
	
	public List<ReparticionSearchInfo> findAllReparticiones(){
		return reparticionDao.findAllReparticiones();
	}

	public void save(final Reparticion aReparticion) 
	{
		reparticionDao.save(aReparticion);
	}
	
	public void saveOrUpdate(final Reparticion aReparticion) 
	{
		reparticionDao.saveOrUpdate(aReparticion);
	}

	public ReparticionDao getReparticionDao()
	{
		return reparticionDao;
	}

	public List<Reparticion> findAll(){
		return reparticionDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	/*@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")*/
	public List<Reparticion> findReparticiones(SearchCriteria criteria) {
		return reparticionDao.findReparticiones(criteria);
	}
	

}

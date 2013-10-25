package org.dpi.occupationalGroup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class OccupationalGroupServiceImpl implements OccupationalGroupService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final OccupationalGroupDao occupationalGroupDao;
	

	private ApplicationContext applicationContext;
	
	public OccupationalGroupServiceImpl(final OccupationalGroupDao occupationalGroupDao) {
		this.occupationalGroupDao = occupationalGroupDao;
	}
	

	public OccupationalGroup findById(Long id){
		return occupationalGroupDao.findById(id);
	}

	
	public List<OccupationalGroup> find(OccupationalGroupQueryFilter occupationalGroupQueryFilter){
		
		return occupationalGroupDao.find(occupationalGroupQueryFilter);
	}


	
	public void save(final OccupationalGroup occupationalGroup) 
	{
		occupationalGroupDao.save(occupationalGroup);
	}
	
	public void saveOrUpdate(final OccupationalGroup occupationalGroup) 
	{
		if (occupationalGroup.getId() != null) { // it is an update
			 occupationalGroupDao.merge(occupationalGroup);
			} else { // you are saving a new one
			 occupationalGroupDao.saveOrUpdate(occupationalGroup);
			}
	}
	
	public void delete(OccupationalGroup occupationalGroup){
		occupationalGroupDao.delete(occupationalGroup);
	}

	public OccupationalGroupDao getOccupationalGroupDao()
	{
		return occupationalGroupDao;
	}

	public List<OccupationalGroup> findAll(){
		return this.occupationalGroupDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	
}

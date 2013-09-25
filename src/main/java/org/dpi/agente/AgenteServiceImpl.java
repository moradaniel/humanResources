package org.dpi.agente;

import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class AgenteServiceImpl implements AgenteService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final AgenteDao agenteDao;
	private ApplicationContext applicationContext;
	
	public AgenteServiceImpl(final AgenteDao agenteDao) {
		this.agenteDao = agenteDao;
	}

	
	public Agente findByCuil(String cuil){
		
		return agenteDao.findByCuil(cuil);
	}

	
	public void save(final Agente agente) 
	{
		agenteDao.save(agente);
	}
	
	public void delete(Agente agente){
		agenteDao.delete(agente);
	}
	
	public void saveOrUpdate(final Agente agente) 
	{
		agenteDao.saveOrUpdate(agente);
	}


	public AgenteDao getAgenteDao()
	{
		return agenteDao;
	}

	
	public List<Agente> findAll(){
		return this.agenteDao.findAll();
	}
	
	public PageList<Agente> findAgentes(final QueryBind bind,
			   final AgenteQueryFilter filter,
			   boolean isForExcel){
		
		return this.agenteDao.findAgentes(bind, filter, isForExcel);
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public List<Agente> find(AgenteQueryFilter agenteQueryFilter) {
		return this.agenteDao.find(agenteQueryFilter);
	}
}

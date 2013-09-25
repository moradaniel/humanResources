package org.dpi.categoria;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class CategoriaServiceImpl implements CategoriaService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final CategoriaDao categoriaDao;
	private ApplicationContext applicationContext;
	
	public CategoriaServiceImpl(final CategoriaDao categoriaDao) {
		this.categoriaDao = categoriaDao;
	}

	
	public Categoria findByCodigo(String codigo){
		
		return categoriaDao.findByCodigo(codigo);
	}

	
	public void save(final Categoria categoria) 
	{
		categoriaDao.save(categoria);
	}
	
	public void saveOrUpdate(final Categoria categoria) 
	{
		categoriaDao.saveOrUpdate(categoria);
	}

	public CategoriaDao getCategoriaDao()
	{
		return categoriaDao;
	}

	public List<Categoria> findAll(){
		return this.categoriaDao.findAll();
	}


	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
}

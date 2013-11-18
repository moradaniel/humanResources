package org.dpi.category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class CategoryServiceImpl implements CategoryService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final CategoryDao categoryDao;
	private ApplicationContext applicationContext;
	
	public CategoryServiceImpl(final CategoryDao categoryDao) {
		this.categoryDao = categoryDao;
	}

	
	public Category findByCode(String code){
		
		return categoryDao.findByCode(code);
	}

	
	public void save(final Category category) 
	{
		categoryDao.save(category);
	}
	
	public void saveOrUpdate(final Category category) 
	{
		categoryDao.saveOrUpdate(category);
	}

	public CategoryDao getCategoryDao()
	{
		return categoryDao;
	}

	public List<Category> findAll(){
		return this.categoryDao.findAll();
	}


	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
}

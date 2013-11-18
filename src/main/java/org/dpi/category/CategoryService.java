package org.dpi.category;

import java.util.List;

import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete categoria objects from
 * persistent storage
 *
 */
public interface CategoryService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding category, which may not
	 * contain all associated objects, or <code>null</code> if the category is not found.
	 *
	 * @param code a business code that uniquely identifies this category
	 */
	public Category findByCode(String code);
	
	public List<Category> findAll();
	
	public void save(final Category category); 
	
	public void saveOrUpdate(final Category category); 
	
	public CategoryDao getCategoryDao();

}

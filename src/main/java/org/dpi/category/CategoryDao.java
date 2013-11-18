package org.dpi.category;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface CategoryDao extends HibernateDataAccessObject
{

	public List<Category> findAll();
	
	public Category findByCode(String code);
	
}

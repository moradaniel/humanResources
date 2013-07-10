package org.dpi.categoria;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface CategoriaDao extends HibernateDataAccessObject
{

	public List<Categoria> findAll();
	
	public Categoria findByCodigo(String codigo);
	
}

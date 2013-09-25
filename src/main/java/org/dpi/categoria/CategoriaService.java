package org.dpi.categoria;

import java.util.List;

import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete categoria objects from
 * persistent storage
 *
 */
public interface CategoriaService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding categoria, which may not
	 * contain all associated objects, or <code>null</code> if the categoria is not found.
	 *
	 * @param code a business code that uniquely identifies this categoria
	 */
	public Categoria findByCodigo(String codigo);
	
	public List<Categoria> findAll();
	
	public void save(final Categoria categoria); 
	
	public void saveOrUpdate(final Categoria categoria); 
	
	public CategoriaDao getCategoriaDao();

}

package org.dpi.empleo;

import java.util.List;

import org.dpi.categoria.Categoria;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Employment objects from
 * persistent storage
 *
 */
public interface EmploymentService extends ApplicationContextAware
{
	/**
	 * Returns a possibly lightweight representation of the corresponding Employment, which may not
	 * contain all associated objects, or <code>null</code> if the Employment is not found.
	 *
	 * @param code a business code that uniquely identifies this Employment
	 */
	public List<Empleo> find(EmploymentQueryFilter empleoQueryFilter);

	public Empleo findById(Long id);
	
	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter);
	
	public void delete(Empleo empleo);
		
	public void save(final Empleo empleo); 
	
	public void saveOrUpdate(final Empleo empleo); 
	
	public EmploymentDao getEmploymentDao();

	//Deprecated
	public Empleo findPreviousEmpleo(Empleo empleo);

	public List<Categoria> getAvailableCategoriesForPromotion(Empleo empleoActual);

}

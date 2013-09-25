package org.dpi.empleo;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface EmploymentDao extends HibernateDataAccessObject
{
	
	public Empleo findById(Long id);

	public List<Empleo> findAll();
	
	public List<Empleo> find(EmploymentQueryFilter empleoQueryFilter);
	
	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter);
	
	public Empleo findPreviousEmpleo(EmploymentQueryFilter empleoQueryFilter);
	
}

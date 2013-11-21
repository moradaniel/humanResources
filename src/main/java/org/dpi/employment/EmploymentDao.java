package org.dpi.employment;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface EmploymentDao extends HibernateDataAccessObject
{
	
	public Employment findById(Long id);

	public List<Employment> findAll();
	
	public List<Employment> find(EmploymentQueryFilter employmentQueryFilter);
	
	public List<Employment> findInactivEmployments(final EmploymentQueryFilter employmentQueryFilter);
	
	public Employment findPreviousEmployment(EmploymentQueryFilter employmentQueryFilter);
	
}

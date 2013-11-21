package org.dpi.employment;

import java.util.List;

import org.dpi.category.Category;
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
	public List<Employment> find(EmploymentQueryFilter employmentQueryFilter);

	public Employment findById(Long id);
	
	public List<Employment> findInactiveEmployments(final EmploymentQueryFilter employmentQueryFilter);
	
	public void delete(Employment employment);
		
	public void save(final Employment employment); 
	
	public void saveOrUpdate(final Employment employment); 
	
	public EmploymentDao getEmploymentDao();

	//Deprecated
	public Employment findPreviousEmployment(Employment employment);

	public List<Category> getAvailableCategoriesForPromotion(Employment currentEmployment);

}

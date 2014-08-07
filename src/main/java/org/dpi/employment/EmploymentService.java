package org.dpi.employment;

import java.util.List;

import org.dpi.category.Category;
import org.dpi.util.PageList;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete Employment objects from
 * persistent storage
 *
 */
public interface EmploymentService extends ApplicationContextAware
{

	
	//public List<Employment> find(EmploymentQueryFilter employmentQueryFilter);
	
	public PageList<Employment> findEmployments(final EmploymentQueryFilter employmentQueryFilter);
	
	public Employment findById(Long id);
	
	public void delete(Employment employment);
		
	public void save(final Employment employment); 
	
	public void saveOrUpdate(final Employment employment); 
	
	public EmploymentDao getEmploymentDao();


	public List<Category> getAvailableCategoriesForPromotion(Employment currentEmployment);

}

package org.dpi.department;

import java.util.List;

import org.springframework.context.ApplicationContextAware;
import org.springframework.samples.travel.SearchCriteria;


/**
 * Used to create, save, retrieve, update and delete Department objects from
 * persistent storage
 *
 */
public interface DepartmentService extends ApplicationContextAware
{
	
	public Department findById(Long id);
	
	
	/**
	 * Returns a possibly lightweight representation of the corresponding Department, which may not
	 * contain all associated objects, or <code>null</code> if the Department is not found.
	 *
	 * @param code a business code that uniquely identifies this Department
	 */
	public Department findByName(String name);
	
	public List<DepartmentSearchInfo> findAllDepartments();
	
	public void save(final Department aDepartment); 
	
	public void saveOrUpdate(final Department aDepartment); 
	
	public DepartmentDao getDepartmentDao();

	public List<Department> findDepartments(SearchCriteria criteria);

}

package org.dpi.department;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;
import org.springframework.samples.travel.SearchCriteria;


public interface DepartmentDao extends HibernateDataAccessObject
{

	public List<Department> findAll();

	public Department findByName(String name);

	public Department findById(Long id);

	public List<Department> findDepartments(SearchCriteria criteria);
	
	public List<DepartmentSearchInfo> findAllDepartments();
	
}

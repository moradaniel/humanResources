package org.dpi.department;

import java.util.List;

import org.dpi.util.tree.GenericTreeNode;
import org.janux.bus.persistence.HibernateDataAccessObject;
import org.janux.bus.security.Account;


public interface DepartmentDao extends HibernateDataAccessObject
{

	public List<Department> findAll();

	public Department findByName(String name);

	public Department findById(Long id);

	//public List<Department> findDepartments(SearchCriteria criteria);
	
	public List<DepartmentSearchInfo> findAllDepartments();

	public GenericTreeNode<Department> getSubTree(Long departmentId);
	
	public List<Department> findUserDepartments(Account account);
	
}

package org.dpi.department;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class DepartmentServiceImpl implements DepartmentService
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final DepartmentDao departmentDao;
	private ApplicationContext applicationContext;
	
	public DepartmentServiceImpl(final DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}
	
	public Department findById(Long id){
		return this.departmentDao.findById(id);
	}
	
	public Department findByName(String name){
		return this.departmentDao.findByName(name);
	}
	
	public List<DepartmentSearchInfo> findAllDepartments(){
		return departmentDao.findAllDepartments();
	}

	public void save(final Department aDepartment) 
	{
		departmentDao.save(aDepartment);
	}
	
	public void saveOrUpdate(final Department aDepartment) 
	{
		departmentDao.saveOrUpdate(aDepartment);
	}

	public DepartmentDao getDepartmentDao()
	{
		return departmentDao;
	}

	public List<Department> findAll(){
		return departmentDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	/*@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")*/
	/*public List<Department> findDepartments(SearchCriteria criteria) {
		return departmentDao.findDepartments(criteria);
	}
	*/

}

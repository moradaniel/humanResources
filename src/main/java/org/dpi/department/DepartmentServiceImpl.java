package org.dpi.department;

import java.util.List;

import org.dpi.util.tree.GenericTreeNode;
import org.janux.bus.security.Account;
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

    @Override
    public GenericTreeNode<Department> getSubTree(Long departmentId) {
        
        return departmentDao.getSubTree(departmentId);
    }
    
    @Override
    public boolean canGenerateRetainedCreditsTree(Department department) {
        if(isMinisterio(department) || isPoderEjecutivo(department)) {
            return true;
        }
        return false;
    }
    
    
    @Override
    public boolean isMinisterio(Department department) {
        if(!department.getCode().startsWith("1")) {
            return false;
        }
        if(isPoderEjecutivo(department.getParent()) && department.getChildren().size() > 0){
            //is ministerio or secretaria
            return true;
        } 
        return false;
    }
    
    @Override
    public boolean isPoderEjecutivo(Department department) {
        if(department!=null && department.getParent()==null && department.getCode().equals("1000000000")){
            return true;
        } 
        return false;
    }
    

    public List<Department> findUserDepartments(Account account){
        return departmentDao.findUserDepartments(account);
    }

	/*@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")*/
	/*public List<Department> findDepartments(SearchCriteria criteria) {
		return departmentDao.findDepartments(criteria);
	}
	*/

}

package org.dpi.department;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dpi.util.tree.GenericTreeNode;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;





public class DepartmentServiceImpl implements DepartmentService {
    
	Logger log = LoggerFactory.getLogger(this.getClass());
	private final DepartmentDao departmentDao;
	private ApplicationContext applicationContext;
	
	
	Map<Long, Department> reparticion_ministerioOSecretariaDeEstadoMap = new HashMap<>();
	
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
    
    @Override
    public boolean isPoderEjecutivoChildButNotMinisterio(Department department) {
        if(isPoderEjecutivo(department.getParent()) && !isMinisterio(department)){
            return true;
        } 
        return false;
    }

    public List<Department> findUserDepartments(Account account){
        return departmentDao.findUserDepartments(account);
    }
    
    

    @Override
    public boolean isORGANISMOS_CONSTITUCIONALES(Department department) {
        if(department!=null && department.getParent()==null && department.getCode().equals("4000000000")){
            return true;
        } 
        return false;
    }
    
    @Override
    public Department getMinisterioOSecretariaDeEstado(Department childDepartment) {
        
        //TODO implementar una cache para guardar esto que se actualice cada 1 hora
        Department ministerioOSecretariaDeEstadoDeLaReparticionDepartment =  reparticion_ministerioOSecretariaDeEstadoMap.get(childDepartment.getId());
        if(ministerioOSecretariaDeEstadoDeLaReparticionDepartment==null) {
            ministerioOSecretariaDeEstadoDeLaReparticionDepartment = findMinisterioOSecretariaDeEstado(childDepartment);
            reparticion_ministerioOSecretariaDeEstadoMap.put(childDepartment.getId(),ministerioOSecretariaDeEstadoDeLaReparticionDepartment);
        }
        return ministerioOSecretariaDeEstadoDeLaReparticionDepartment;
        
        //por ahora buscamos pero hay que hacer cache
       // return findMinisterioOSecretariaDeEstado(childDepartment);
    }

    public Department findMinisterioOSecretariaDeEstado(Department childDepartment){
        //Department ministerioOSecretariaDeEstadoDeLaReparticionDepartment = null;
        /*if(childDepartment == null) {
            return null;
        }*/
        if(isPoderEjecutivo(childDepartment) || isORGANISMOS_CONSTITUCIONALES(childDepartment)) {
            return childDepartment;
        }
        
        if(isPoderEjecutivo(childDepartment.getParent()) || isORGANISMOS_CONSTITUCIONALES(childDepartment.getParent())) {
            return childDepartment;
        }
        if(childDepartment.getParent()==null){
            return childDepartment;
        }
        return findMinisterioOSecretariaDeEstado(departmentDao.findById(childDepartment.getParent().getId()));
    }
    
	/*@Transactional(readOnly = true)
	@SuppressWarnings("unchecked")*/
	/*public List<Department> findDepartments(SearchCriteria criteria) {
		return departmentDao.findDepartments(criteria);
	}
	*/
    
}

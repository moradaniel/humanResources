package org.dpi.department;

import java.util.List;

import org.dpi.util.tree.GenericTreeNode;
import org.janux.bus.security.Account;
import org.springframework.context.ApplicationContextAware;


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
	
	public List<Department> findUserDepartments(Account account);
	
	public void save(final Department aDepartment); 
	
	public void saveOrUpdate(final Department aDepartment); 
	
	public DepartmentDao getDepartmentDao();


    public GenericTreeNode<Department> getSubTree(Long departmentId);
    

	public boolean canGenerateRetainedCreditsTree(Department department);


    boolean isMinisterio(Department department);


    boolean isPoderEjecutivo(Department department);
    
    public boolean isPoderEjecutivoChildButNotMinisterio(Department department);
    
    public Department getMinisterioOSecretariaDeEstado(Department childDepartment);


    boolean isORGANISMOS_CONSTITUCIONALES(Department department);

}

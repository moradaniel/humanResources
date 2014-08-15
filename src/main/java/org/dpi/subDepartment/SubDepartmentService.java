package org.dpi.subDepartment;

import java.util.List;

import org.dpi.department.Department;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete CentroSector objects from
 * persistent storage
 *
 */
public interface SubDepartmentService extends ApplicationContextAware
{

	public SubDepartment findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector);

	public PageList<SubDepartment> findSubDepartments(final QueryBind bind,
			   final SubDepartmentQueryFilter filter,
			   boolean isForExcel);
	
	public List<SubDepartment> findSubDepartments(final Department department);
	
	public SubDepartment findById(Long id) throws EntityNotFoundException;

	public void save(final SubDepartment aSubDepartment); 
	
	public void saveOrUpdate(final SubDepartment aSubDepartment); 
	
	public SubDepartmentDao getCentroSectorDao();

}

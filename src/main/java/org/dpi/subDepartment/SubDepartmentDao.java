package org.dpi.subDepartment;

import java.util.List;

import org.dpi.department.Department;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface SubDepartmentDao extends HibernateDataAccessObject
{

	public List<SubDepartment> findAll();
	
	public SubDepartment findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector);
	
	public PageList<SubDepartment> findSubDepartments(final QueryBind bind,
			   final SubDepartmentQueryFilter filter,
			   boolean isForExcel);
	
	public List<SubDepartment> findSubDepartments(final Department department);
	
	public SubDepartment findById(Long id);
	
	
}

package org.dpi.subDepartment;

import java.util.List;

import org.dpi.department.Department;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class SubDepartmentServiceImpl implements SubDepartmentService
{
	Logger log = LoggerFactory.getLogger(this.getClass());;
	private final SubDepartmentDao subDepartmentDao;
	private ApplicationContext applicationContext;
	
	public SubDepartmentServiceImpl(final SubDepartmentDao subDepartmentDao) {
		this.subDepartmentDao = subDepartmentDao;
	}

	public SubDepartment findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector){
		
		return subDepartmentDao.findByCodigoCentroCodigoSector(codigoCentro,codigoSector);
	}

	
	public void save(final SubDepartment aReparticion) 
	{
		subDepartmentDao.save(aReparticion);
	}
	
	public void saveOrUpdate(final SubDepartment aReparticion) 
	{
		subDepartmentDao.saveOrUpdate(aReparticion);
	}

	
	public SubDepartment findById(Long id) throws EntityNotFoundException {
		return subDepartmentDao.findById(id);
	}


	public SubDepartmentDao getCentroSectorDao()
	{
		return subDepartmentDao;
	}

	public List<SubDepartment> findAll(){
		return this.subDepartmentDao.findAll();
	}
	

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public PageList<SubDepartment> findSubDepartments(QueryBind bind,
			SubDepartmentQueryFilter filter, boolean isForExcel) {
		return this.subDepartmentDao.findSubDepartments(bind, filter, isForExcel);
	}
	
	@Override
	public List<SubDepartment> findSubDepartments(final Department department){
		return this.subDepartmentDao.findSubDepartments(department);
	}
}

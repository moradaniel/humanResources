package org.dpi.department;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.SQLQuery;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;

/**
 * Used to create, save, retrieve, update and delete department objects from
 * persistent storage
 *
 */
public class DepartmentDaoHibImpl extends BaseDAOHibernate implements DepartmentDao
{
	@SuppressWarnings("unchecked")
	public List<Department> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all departments");

		List<Department> list = getHibernateTemplate().find("from DepartmentImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " departments in " + timer.printElapsedTime());

		return list;
	}
	
	@Override
	public Department findByName(String name)
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Department with name '" + name + "'");

		List list = getHibernateTemplate().find("from DepartmentImpl where name=?", name);

		Department department = (list.size() > 0) ? (Department)list.get(0) : null;

		if (department == null) {
			log.warn("Unable to find Department with name: '" + name + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved department with name: '" + name + "' in " + timer.printElapsedTime());

		return department;
	}

	@Override
	public Department findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Department with id: '" + id + "'");

		List list = getHibernateTemplate().find("from DepartmentImpl where id=?", id);

		Department department = (list.size() > 0) ? (Department)list.get(0) : null;

		if (department == null) {
			log.warn("Unable to find Department with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved Department with id: '" + id + "' in " + timer.printElapsedTime());

		return department;
	}

	/*
	@SuppressWarnings( "unchecked" )
	@Override
	public List<Department> findDepartments(final SearchCriteria criteria) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Department with filter '" + criteria.toString() + "'" );

		List<Department> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String pattern = getSearchPattern(criteria);
				
				String where = " WHERE 1=1 "+buildWhereClause(criteria);
				
				String select = "Select department ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM  DepartmentImpl department ");
				sb.append(where);
				
				Query q = sess.createQuery(select+sb.toString());
		
				List<Department> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved Department with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}*/
	
	/*private String buildWhereClause(SearchCriteria criteria) {
		StringBuffer sb = new StringBuffer();
		if(criteria!=null) {
			String searchString = criteria.getSearchString();
			if(StringUtils.hasText(searchString)) {
				sb.append(" AND department.name = '").append(searchString).append("'");
			}
			

		}
		return sb.toString();
	}
	
	private String getSearchPattern(SearchCriteria criteria) {
		if (StringUtils.hasText(criteria.getSearchString())) {
			return "'%"
					+ criteria.getSearchString().toLowerCase()
							.replace('*', '%') + "%'";
		} else {
			return "'%'";
		}
	}*/
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<DepartmentSearchInfo> findAllDepartments()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT department.id as departmentId, ");
		sb.append(" department.name as departmentName, ");
		sb.append(" department.code as departmentCode ");
		sb.append(" FROM department ");
		
		SQLQuery aQuery = this.getSession().createSQLQuery(sb.toString());
		
		List<Object[]> rawObjects = aQuery.list();
		
		List<DepartmentSearchInfo> departmentSearchInfos = new ArrayList<DepartmentSearchInfo>();
		
		for(Object[] object : rawObjects)
		{
			DepartmentSearchInfo searchInfo = new DepartmentSearchInfo();
			Long departmentId = ((BigDecimal)object[0]).longValue();
			searchInfo.setDepartmentId(departmentId);
			searchInfo.setDepartmentName(ObjectUtils.toString(object[1]));
			searchInfo.setDepartmentCode(ObjectUtils.toString(object[2]));

			
			departmentSearchInfos.add(searchInfo);
		}
		
		return departmentSearchInfos;
	}

}

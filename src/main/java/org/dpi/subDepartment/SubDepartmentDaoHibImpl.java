package org.dpi.subDepartment;

import java.sql.SQLException;
import java.util.List;

import org.dpi.department.Department;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class SubDepartmentDaoHibImpl extends BaseDAOHibernate implements SubDepartmentDao
{
	
	private static final String LIST_QUERY = "SELECT subDepartments "
			+ "FROM SubDepartmentImpl subDepartments ";

	private static final String LIST_QUERY_COUNT =  "SELECT count(subDepartments) "
			+ "FROM SubDepartmentImpl subDepartments ";
	
	@SuppressWarnings("unchecked")
	public List<SubDepartment> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all subDepartments");

		List<SubDepartment> list = getHibernateTemplate().find("from SubDepartmentImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " subDepartments in " + timer.printElapsedTime());

		return list;
	}	
	
	public SubDepartment findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find SubDepartment with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "'");

		String queryString = "from SubDepartmentImpl where codigoCentro=:codigoCentro and codigoSector=:codigoSector ";
		
		String[] paramNames = new String[2];
		Object[] paramValues = new Object[2];
		paramNames[0] = "codigoCentro";
		paramNames[1] = "codigoSector";
		paramValues[0] = codigoCentro;
		paramValues[1] = codigoSector;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		SubDepartment subDepartment = (list.size() > 0) ? (SubDepartment)list.get(0) : null;

		if (subDepartment == null) {
			log.warn("Unable to find SubDepartment with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved subDepartment with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "' in " + timer.printElapsedTime());

		return subDepartment;
	}
	
	@Override
	public SubDepartment findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find SubDepartment with id: '" + id + "'");

		List list = getHibernateTemplate().find("from SubDepartmentImpl where id=?", id);

		SubDepartment subDepartment = (list.size() > 0) ? (SubDepartment)list.get(0) : null;

		if (subDepartment == null) {
			log.warn("Unable to find SubDepartment with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved SubDepartment with id: '" + id + "' in " + timer.printElapsedTime());

		return subDepartment;
	}

	@Override
	public PageList<SubDepartment> findSubDepartments(QueryBind bind,
			SubDepartmentQueryFilter filter, boolean isForExcel) {
		return doInHibernate(LIST_QUERY, LIST_QUERY_COUNT,
		buildWhereClauseSubDepartmentRequest(null, "", bind, filter), bind);
	}
	
	private String buildWhereClauseSubDepartmentRequest(Integer id, String KeyId, QueryBind bind, SubDepartmentQueryFilter filter) {

		StringBuilder query_where = new StringBuilder();
//		 if the id is  null that means we want to retrieve all loans for all Area Manager
		if(id!=null){
			query_where.append("WHERE ").append(KeyId).append(".id = '").append(id).append("'");
		}else{
			// this is only to Start with a Where statement and follow with "AND" statement
			query_where.append("WHERE 1 = 1");
		}
		
		if(filter !=null){
			if(StringUtils.hasText(filter.getCodigoCentro())){
				
				query_where.append(" AND upper(subDepartments.codigoCentro) like '%").append(filter.getCodigoCentro().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getNombreCentro())){
				
				query_where.append(" AND upper(subDepartments.nombreCentro) like '%").append(filter.getNombreCentro().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getCodigoSector())){
				
				query_where.append(" AND upper(subDepartments.codigoSector) like '%").append(filter.getCodigoSector().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getNombreSector())){
				
				query_where.append(" AND upper(subDepartments.nombreSector) like '%").append(filter.getNombreSector().toUpperCase()).append("%' ");
			}

			
		}



		if (bind!=null){
			query_where.append(" ").append(bind.buildHQLOrderBy());
		}

		return query_where.toString();
	}
	
	private PageList doInHibernate(final String query, final String countQuery, final String whereClause, final QueryBind bind) {
		return (PageList) getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {

				if (log.isDebugEnabled()) {
					log.debug("HQL: " + query + whereClause);
				}

				Query q = sess.createQuery(query + whereClause);

				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}

				PageList pageList = new PageList(q.list());

				if (bind != null) {
					Query qCount = sess.createQuery(countQuery + whereClause);
					Long totalItems = (Long)qCount.uniqueResult();
					
					//Integer size = ((Long) qCount.uniqueResult()).intValue();
					pageList.setTotalItems(totalItems);
					pageList.setPageItems(bind.getCountElement());
					
					int pageItems =bind.getCountElement();
					long totalPageCount = totalItems / pageItems+1;
					long mod = totalItems % pageItems;
					if(mod == 0) totalPageCount -= 1;					
					pageList.setTotalPageCount(totalPageCount);
				}

				return pageList;
			}
		});
	}

	
	@SuppressWarnings( "unchecked" )
	@Override
	public List<SubDepartment> findSubDepartments(final Department department){
		Chronometer timer = new Chronometer();

		//if (log.isDebugEnabled()) log.debug("attempting to find employment with filter '" + employmentQueryFilter.toString() + "'" );

		List<SubDepartment> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				//String where = " WHERE 1=1 "+buildWhereClause(employmentQueryFilter);
				
				String select = "Select subDepartment ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM SubDepartmentImpl subDepartment ");
				sb.append(" INNER JOIN FETCH subDepartment.department department ");


				sb.append(" WHERE department.id = :departmentId ");
				
				sb.append(" ORDER BY subDepartment.codigoCentro asc, subDepartment.codigoSector asc ");
				
				Query q = sess.createQuery(select+sb.toString());
				q.setParameter("departmentId", department.getId());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<SubDepartment> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
}

package org.dpi.centroSector;

import java.sql.SQLException;
import java.util.List;

import org.dpi.reparticion.Reparticion;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CentroSectorDaoHibImpl extends DataAccessHibImplAbstract implements CentroSectorDao
{
	
	private static final String LIST_QUERY = "SELECT centroSectores "
			+ "FROM CentroSectorImpl centroSectores ";

	private static final String LIST_QUERY_COUNT =  "SELECT count(centroSectores) "
			+ "FROM CentroSectorImpl centroSectores ";
	
	@SuppressWarnings("unchecked")
	public List<CentroSector> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all centrosectores");

		List<CentroSector> list = getHibernateTemplate().find("from CentroSectorImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " centrosector in " + timer.printElapsedTime());

		return list;
	}	
	
	public CentroSector findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find CentroSector with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "'");

		String queryString = "from CentroSectorImpl where codigoCentro=:codigoCentro and codigoSector=:codigoSector ";
		
		String[] paramNames = new String[2];
		Object[] paramValues = new Object[2];
		paramNames[0] = "codigoCentro";
		paramNames[1] = "codigoSector";
		paramValues[0] = codigoCentro;
		paramValues[1] = codigoSector;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		CentroSector centroSector = (list.size() > 0) ? (CentroSector)list.get(0) : null;

		if (centroSector == null) {
			log.warn("Unable to find CentroSector with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved centrosector with codigoCentro '" + codigoCentro + "' codigoSector '" + codigoSector + "' in " + timer.printElapsedTime());

		return centroSector;
	}
	
	@Override
	public CentroSector findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find CentroSector with id: '" + id + "'");

		List list = getHibernateTemplate().find("from CentroSectorImpl where id=?", id);

		CentroSector centroSector = (list.size() > 0) ? (CentroSector)list.get(0) : null;

		if (centroSector == null) {
			log.warn("Unable to find Reparticion with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved centroSector with id: '" + id + "' in " + timer.printElapsedTime());

		return centroSector;
	}

	@Override
	public PageList<CentroSector> findCentroSectores(QueryBind bind,
			CentroSectorQueryFilter filter, boolean isForExcel) {
		return doInHibernate(LIST_QUERY, LIST_QUERY_COUNT,
		buildWhereClauseCentroSectorRequest(null, "", bind, filter), bind);
	}
	
	private String buildWhereClauseCentroSectorRequest(Integer id, String KeyId, QueryBind bind, CentroSectorQueryFilter filter) {

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
				
				query_where.append(" AND upper(centroSectores.codigoCentro) like '%").append(filter.getCodigoCentro().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getNombreCentro())){
				
				query_where.append(" AND upper(centroSectores.nombreCentro) like '%").append(filter.getNombreCentro().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getCodigoSector())){
				
				query_where.append(" AND upper(centroSectores.codigoSector) like '%").append(filter.getCodigoSector().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getNombreSector())){
				
				query_where.append(" AND upper(centroSectores.nombreSector) like '%").append(filter.getNombreSector().toUpperCase()).append("%' ");
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
	public List<CentroSector> findCentroSectores(final Reparticion reparticion){
		Chronometer timer = new Chronometer();

		//if (log.isDebugEnabled()) log.debug("attempting to find Empleo with filter '" + empleoQueryFilter.toString() + "'" );

		List<CentroSector> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				//String where = " WHERE 1=1 "+buildWhereClause(empleoQueryFilter);
				
				String select = "Select centroSector ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM CentroSectorImpl centroSector ");
				sb.append(" INNER JOIN FETCH centroSector.reparticion reparticion ");


				sb.append(" WHERE reparticion.id = :reparticionId ");
				
				sb.append(" ORDER BY centroSector.codigoCentro asc, centroSector.codigoSector asc ");
				
				Query q = sess.createQuery(select+sb.toString());
				q.setParameter("reparticionId", reparticion.getId());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<CentroSector> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
}

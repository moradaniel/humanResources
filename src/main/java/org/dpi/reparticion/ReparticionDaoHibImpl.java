package org.dpi.reparticion;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.samples.travel.SearchCriteria;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete Hotel objects from
 * persistent storage
 *
 */
public class ReparticionDaoHibImpl extends DataAccessHibImplAbstract implements ReparticionDao
{
	@SuppressWarnings("unchecked")
	public List<Reparticion> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all hotels");

		List<Reparticion> list = getHibernateTemplate().find("from ReparticionImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " hotels in " + timer.printElapsedTime());

		return list;
	}
	
	public Reparticion findByNombre(String nombre)
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with code '" + nombre + "'");

		List list = getHibernateTemplate().find("from ReparticionImpl where nombre=?", nombre);

		Reparticion reparticion = (list.size() > 0) ? (Reparticion)list.get(0) : null;

		if (reparticion == null) {
			log.warn("Unable to find Reparticion with nombre: '" + nombre + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with nombre: '" + nombre + "' in " + timer.printElapsedTime());

		return reparticion;
	}

	@Override
	public Reparticion findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with id: '" + id + "'");

		List list = getHibernateTemplate().find("from ReparticionImpl where id=?", id);

		Reparticion reparticion = (list.size() > 0) ? (Reparticion)list.get(0) : null;

		if (reparticion == null) {
			log.warn("Unable to find Reparticion with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved reparticion with id: '" + id + "' in " + timer.printElapsedTime());

		return reparticion;
	}

	
	@SuppressWarnings( "unchecked" )
	public List<Reparticion> findReparticiones(final SearchCriteria criteria) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Reparticion with filter '" + criteria.toString() + "'" );

		List<Reparticion> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String pattern = getSearchPattern(criteria);
				
				String where = " WHERE 1=1 "+buildWhereClause(criteria);
				
				String select = "Select reparticion ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM ReparticionImpl reparticion ");
				sb.append(where);
				
				Query q = sess.createQuery(select+sb.toString());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<Reparticion> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved Reparticion with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private String buildWhereClause(SearchCriteria criteria) {
		StringBuffer sb = new StringBuffer();
		if(criteria!=null) {
			String searchString = criteria.getSearchString();
			if(StringUtils.hasText(searchString)) {
				sb.append(" AND reparticion.nombre = '").append(searchString).append("'");
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
	}
	
	
	@SuppressWarnings("unchecked")
	public List<ReparticionSearchInfo> findAllReparticiones()
	{
		StringBuffer sb = new StringBuffer();
		
		sb.append("SELECT hh.id as hotelId, ");
		sb.append(" hh.nombre as reparticionName ");
		sb.append(" FROM reparticion hh ");
		
		SQLQuery aQuery = this.getSession().createSQLQuery(sb.toString());
		
		List<Object[]> rawObjects = aQuery.list();
		
		List<ReparticionSearchInfo> hotelSearchInfos = new ArrayList<ReparticionSearchInfo>();
		
		for(Object[] object : rawObjects)
		{
			ReparticionSearchInfo searchInfo = new ReparticionSearchInfo();
			Long idReparticion = ((BigDecimal)object[0]).longValue();
			searchInfo.setReparticionId(idReparticion);
			searchInfo.setReparticionName(ObjectUtils.toString(object[1]));
			//final String sHotelStatus = ObjectUtils.toString(object[3]);
			/*if (StringUtils.hasText(sHotelStatus))
			{
				searchInfo.setHotelStatus(HotelStatus.valueOf(sHotelStatus));
			}*/
			
			hotelSearchInfos.add(searchInfo);
		}
		
		return hotelSearchInfos;
	}

}

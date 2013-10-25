package org.dpi.occupationalGroup;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class OccupationalGroupDaoHibImpl extends DataAccessHibImplAbstract implements OccupationalGroupDao
{
	@Override
	@SuppressWarnings("unchecked")
	public List<OccupationalGroup> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all OccupationalGroups");

		List<OccupationalGroup> list = getHibernateTemplate().find("from OccupationalGroupImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " OccupationalGroup in " + timer.printElapsedTime());

		return list;
	}	

	
	@Override
	@SuppressWarnings("unchecked")
	public OccupationalGroup findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find OccupationalGroup with id: '" + id + "'");

		StringBuffer sb = new StringBuffer();
		sb.append("from OccupationalGroupImpl occupationalGroup");
		sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
		sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
		sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");
		
		sb.append(" where occupationalGroup.id=? ");
		
		List list = getHibernateTemplate().find(sb.toString(), id);

		OccupationalGroup occupationalGroup = (list.size() > 0) ? (OccupationalGroup)list.get(0) : null;

		if (occupationalGroup == null) {
			log.warn("Unable to find OccupationalGroup with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved OccupationalGroup with id: '" + id + "' in " + timer.printElapsedTime());

		return occupationalGroup;
	}


	@Override
	@SuppressWarnings( "unchecked" )
	public List<OccupationalGroup> find(final OccupationalGroupQueryFilter occupationalGroupQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find occupationalgroup with filter '" + occupationalGroupQueryFilter.toString() + "'" );

		List<OccupationalGroup> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(occupationalGroupQueryFilter);
				
				String select = "Select occupationalGroup ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM OccupationalGroupImpl occupationalGroup ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");

				sb.append(where);
				
				Query q = sess.createQuery(select+sb.toString());
								
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<OccupationalGroup> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private static  String buildWhereClause(OccupationalGroupQueryFilter occupationalGroupQueryFilter) {
		StringBuffer sb = new StringBuffer();
		


		if(occupationalGroupQueryFilter.getId()!=null){
			sb.append(" AND occupationalGroup.id = ").append(occupationalGroupQueryFilter.getId()).append(" ");
		}
		
		if(occupationalGroupQueryFilter.getCode()!=null){
			sb.append(" AND occupationalGroup.code = ").append(occupationalGroupQueryFilter.getCode()).append(" ");
		}
		
		
		return sb.toString();
	}


	
}

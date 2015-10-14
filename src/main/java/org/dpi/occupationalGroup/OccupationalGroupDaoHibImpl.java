package org.dpi.occupationalGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dpi.util.PageList;
import org.hibernate.HibernateException;
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
public class OccupationalGroupDaoHibImpl extends BaseDAOHibernate implements OccupationalGroupDao
{
	@Override
	@SuppressWarnings("unchecked")
	public List<OccupationalGroup> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all OccupationalGroups");

		List<OccupationalGroup> list = (List<OccupationalGroup>)getHibernateTemplate().find("from OccupationalGroupImpl order by code");

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

		List<OccupationalGroup> list = (List<OccupationalGroup>)getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
			    
                List<String> wheres = new ArrayList<String>();
                List<String> paramNames = new ArrayList<String>();
                List<Object> values = new ArrayList<Object>();
				
				//String where = " WHERE 1=1 "+buildWhereClause(occupationalGroupQueryFilter);
				
				//String select = "Select occupationalGroup ";
				
				StringBuffer queryBuilder = new StringBuffer();
				queryBuilder.append(" FROM OccupationalGroupImpl occupationalGroup ");
				queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
				queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
				queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");
				

                    
				
                buildWhereClause2(occupationalGroupQueryFilter,wheres,paramNames,values);
                
                if(occupationalGroupQueryFilter.isOnlyLeafsOccupationalGroups()) {
                    wheres.add("  occupationalGroup.id NOT IN (SELECT DISTINCT occupationalGroup2.parentOccupationalGroup.id FROM OccupationalGroupImpl occupationalGroup2 WHERE occupationalGroup2.parentOccupationalGroup IS NOT NULL) ");
                }
               
                   
                
                String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                        org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();
                
                int startIndex = occupationalGroupQueryFilter != null ? occupationalGroupQueryFilter.getStartIndex() : null;
                //int maxResults = creditsEntryQueryFilter != null ? creditsEntryQueryFilter.getMaxResults() : null;
                Integer maxResults = null;
                
                return new PageList<OccupationalGroup>(
                        findByNamedParam(" Select occupationalGroup  " + queryWithoutOrdering, paramNames, values, 
                                           startIndex, 
                                           maxResults),
                                          countByNamedParam( " SELECT count(distinct occupationalGroup) " + queryWithoutOrdering, paramNames, values));

                
				//sb.append(where);
				
				//Query q = sess.createQuery(select+sb.toString());
								
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				//List<OccupationalGroup> list = q.list();
				//return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	/*private static  String buildWhereClause(OccupationalGroupQueryFilter occupationalGroupQueryFilter) {
		StringBuffer sb = new StringBuffer();
		


		if(occupationalGroupQueryFilter.getId()!=null){
			sb.append(" AND occupationalGroup.id = ").append(occupationalGroupQueryFilter.getId()).append(" ");
		}
		
		if(occupationalGroupQueryFilter.getCode()!=null){
			sb.append(" AND occupationalGroup.code = ").append(occupationalGroupQueryFilter.getCode()).append(" ");
		}
		
		
		return sb.toString();
	}*/
	
	   private void buildWhereClause2(OccupationalGroupQueryFilter occupationalGroupQueryFilter,
	            List<String> wheres, List<String> paramNames, List<Object> values) {
	        if (occupationalGroupQueryFilter != null) {
	            
	            Long id = occupationalGroupQueryFilter.getId();
	            if(id!=null && id > 0) {
	                wheres.add("  occupationalGroup.id = :id ");
	                paramNames.add("id");
	                values.add(id);
	            }
	            
	            
	            
	            String code = occupationalGroupQueryFilter.getCode();
	            if(StringUtils.hasText(code)) {
	        
	                wheres.add("  occupationalGroup.code = :code ");
	                paramNames.add("code");
	                values.add(code);
	            }
	            

	            
	        }
	    }


	
}

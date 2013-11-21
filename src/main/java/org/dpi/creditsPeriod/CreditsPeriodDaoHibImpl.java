package org.dpi.creditsPeriod;

import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CreditsPeriodDaoHibImpl extends DataAccessHibImplAbstract implements CreditsPeriodDao
{
	@SuppressWarnings("unchecked")
	public List<CreditsPeriod> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all CreditsPeriod");

		List<CreditsPeriod> list = getHibernateTemplate().find("from CreditsPeriod order by name");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " CreditsPeriod in " + timer.printElapsedTime());

		return list;
	}	
	
	
	@SuppressWarnings( "unchecked" )
	public List<CreditsPeriod> find(final CreditsPeriodQueryFilter creditsPeriodQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find CreditsPeriod with filter '" + creditsPeriodQueryFilter.toString() + "'" );

		List<CreditsPeriod> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(creditsPeriodQueryFilter);
				
				String select = "Select creditsPeriod ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM CreditsPeriodImpl creditsPeriod ");
				sb.append(" LEFT OUTER JOIN FETCH creditsPeriod.previousCreditsPeriod previousCreditsPeriod ");


				sb.append(where);
				
				sb.append(" ORDER BY creditsPeriod.name desc ");
				
				Query q = sess.createQuery(select+sb.toString());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<CreditsPeriod> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved  with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private String buildWhereClause(CreditsPeriodQueryFilter creditsPeriodQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(creditsPeriodQueryFilter!=null) {
			String name = creditsPeriodQueryFilter.getName();
			if(name!=null) {
				sb.append(" AND creditsPeriod.name = '").append(name).append("'");
			}
			
			Date startDate = creditsPeriodQueryFilter.getStartDate();
			if(startDate!=null) {
				sb.append(" AND creditsPeriod.startDate >= :startDate '");
			}
			
			Date endDate = creditsPeriodQueryFilter.getEndDate();
			if(endDate!=null) {
				sb.append(" AND creditsPeriod.endDate <= :startDate '");
			}
			

			if(!CollectionUtils.isEmpty(creditsPeriodQueryFilter.getStatuses())){
				sb.append(" AND (");
				for (Iterator<Status> iterator = creditsPeriodQueryFilter.getStatuses().iterator(); iterator.hasNext();) {
					Status status = (Status) iterator.next();
					sb.append(" creditsPeriod.status = '"+status.name()+"' ");
					if(iterator.hasNext()){
						sb.append(" OR ");
					}
				}
				sb.append(" ) ");				
					
			}
			

		}
		return sb.toString();
	}
	

	
}

package org.dpi.creditsEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentStatus;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind.OrderDirection;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CreditsEntryDaoHibImpl extends BaseDAOHibernate implements CreditsEntryDao
{
	@SuppressWarnings("unchecked")
	public List<CreditsEntry> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all creditsEntries");

		List<CreditsEntry> list = getHibernateTemplate().find("from CreditsEntryImpl");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " entry in " + timer.printElapsedTime());

		return list;
	}	
	
	
	@SuppressWarnings( "unchecked" )
	public List<CreditsEntry> find(final CreditsEntryQueryFilter creditsEntryQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find entry with filter '" + creditsEntryQueryFilter.toString() + "'" );

		List<CreditsEntry> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
							
				String select = "Select entry ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM CreditsEntryImpl entry ");
				sb.append(" LEFT OUTER JOIN FETCH entry.employment employment");
				sb.append(" LEFT OUTER JOIN FETCH employment.person person ");
				sb.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment");
				sb.append(" LEFT OUTER JOIN FETCH subDepartment.department ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH entry.creditsPeriod creditsPeriod ");
				sb.append(" LEFT OUTER JOIN FETCH employment.previousEmployment previousEmployment");
				sb.append(" LEFT OUTER JOIN FETCH previousEmployment.category previousCategory ");
				sb.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");

				String where = " WHERE 1=1 "+buildWhereClause(creditsEntryQueryFilter);
				
				sb.append(where);
				
				sb.append(" ORDER BY employment.person.apellidoNombre asc, ");
				sb.append(" employment.startDate desc ");
				
				Query q = sess.createQuery(select+sb.toString());
				
				
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<CreditsEntry> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	public static  String buildWhereClause(CreditsEntryQueryFilter entryQueryFilter) {
		StringBuffer sb = new StringBuffer();
		
		if(!CollectionUtils.isEmpty(entryQueryFilter.getCreditsEntryTypes())){
			sb.append(" AND (");
			for (Iterator iterator = entryQueryFilter.getCreditsEntryTypes().iterator(); iterator.hasNext();) {
				CreditsEntryType creditsEntryType = (CreditsEntryType) iterator.next();
				sb.append(" entry.creditsEntryType = '"+creditsEntryType.name()+"' ");
				if(iterator.hasNext()){
					sb.append(" OR ");
				}
			}
			sb.append(" ) ");				
				
		}
		
		if(!CollectionUtils.isEmpty(entryQueryFilter.getGrantedStatuses())){
			sb.append(" AND (");
			for (Iterator iterator = entryQueryFilter.getGrantedStatuses().iterator(); iterator.hasNext();) {
				GrantedStatus grantedStatus = (GrantedStatus) iterator.next();
				sb.append(" entry.grantedStatus = '"+grantedStatus.name()+"' ");
				if(iterator.hasNext()){
					sb.append(" OR ");
				}
			}
			sb.append(" ) ");				
				
		}


		if(entryQueryFilter.getId()!=null){
			sb.append(" AND entry.id = ").append(entryQueryFilter.getId()).append(" ");
		}
		
		if(entryQueryFilter.getIdCreditsPeriod()!=null){
			sb.append(" AND creditsPeriod.id = ").append(entryQueryFilter.getIdCreditsPeriod()).append(" ");
		}
		
		if(entryQueryFilter.hasCredits!=null && entryQueryFilter.hasCredits.booleanValue()==true){
			sb.append(" AND entry.numberOfCredits > 0 ").append(" ");
		}
		
		if(entryQueryFilter.getEmploymentQueryFilter()!=null) {
			EmploymentQueryFilter employmentQueryFilter = entryQueryFilter.getEmploymentQueryFilter();
			
			String cuil = employmentQueryFilter.getCuil();
			if(cuil!=null) {
				sb.append(" AND employment.person.cuil = '").append(cuil).append("'");
			}
			
			String codigoCentro = employmentQueryFilter.getCodigoCentro();
			if(codigoCentro!=null) {
				sb.append(" AND employment.subDepartment.codigoCentro = '").append(codigoCentro).append("'");
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(codigoSector!=null) {
				sb.append(" AND employment.subDepartment.codigoSector = '").append(codigoSector).append("'");
			}
			
			String categoryCode = employmentQueryFilter.getCategoryCode();
			if(categoryCode!=null) {
				sb.append(" AND employment.category.code = '").append(categoryCode).append("'");
			}
			
			Long departmentId = employmentQueryFilter.getDepartmentId();
			if(departmentId!=null) {
				sb.append(" AND subDepartment.department.id = ").append(departmentId).append(" ");
			}

			Long idEmpleo = employmentQueryFilter.getEmploymentId();
			if(idEmpleo!=null) {
				sb.append(" AND employment.id = '").append(idEmpleo).append("'");
			}
			
			List<Long> personsIds = employmentQueryFilter.getPersonsIds();
			if(!CollectionUtils.isEmpty(personsIds)){
				if(personsIds.size()==1) {
					sb.append(" AND person.id = '").append(personsIds.get(0)).append("'");
				}else{
						sb.append(" AND (1<>1 ");
						//take into account the limit of 1000 elements in an IN condition in Oracle
						List<List<Long>> listsOfAgentsIds = org.dpi.util.CollectionUtils.chopped(personsIds, 999);
						for(List<Long> listOfAgentsIds :listsOfAgentsIds){
							sb.append(" OR person.id IN ( ")
							.append(StringUtils.collectionToDelimitedString(listOfAgentsIds, ","))
							.append(")");
							
						}
						sb.append(")");

				}
				
			}

		
			if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
				sb.append(" AND (");
				for (Iterator iterator = employmentQueryFilter.getEmploymentStatuses().iterator(); iterator.hasNext();) {
					EmploymentStatus employmentStatus = (EmploymentStatus) iterator.next();
					sb.append(" employment.status = '"+employmentStatus.name()+"' ");
					if(iterator.hasNext()){
						sb.append(" OR ");
					}
				}
				sb.append(" ) ");				
					
			}

		}
		return sb.toString();
	}
	
	
	
	   private void buildWhereClause2(CreditsEntryQueryFilter creditsEntryQueryFilter,
	            List<String> wheres, List<String> paramNames, List<Object> values) {
	       
           if (creditsEntryQueryFilter != null) {
               
               if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsPeriodNames())){
                   wheres.add("  creditsPeriod.name IN ( :creditsPeriodsNames )  ");
                   paramNames.add("creditsPeriodsNames");
                   values.add(creditsEntryQueryFilter.getCreditsPeriodNames());
                   
               }
               
               if(!CollectionUtils.isEmpty(creditsEntryQueryFilter.getCreditsEntryTypes())){
                   StringBuilder sb = new StringBuilder();

                   sb.append(" ( ");
                   
                   int statusIndex = 1;
                   for (Iterator iterator = creditsEntryQueryFilter.getCreditsEntryTypes().iterator(); iterator.hasNext();) {
                       CreditsEntryType creditsEntryType = (CreditsEntryType) iterator.next();
                       sb.append(" entry.creditsEntryType = :creditsEntryType"+statusIndex);
                       
                       paramNames.add("creditsEntryType"+statusIndex);
                       values.add(creditsEntryType);
                       
                       if(iterator.hasNext()){
                           sb.append(" OR ");
                       }
                       statusIndex++;
                   }
                   sb.append(" ) ");   
                   
                   wheres.add(sb.toString());
                       
               }
               
               
           }
           

	       
	    }
	
	
	@Override
    public PageList<CreditsEntry> findCreditsEntries(final CreditsEntryQueryFilter creditsEntryQueryFilter){

        List<String> wheres = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        
        StringBuffer queryBuilder = new StringBuffer();
       

        queryBuilder.append(" FROM CreditsEntryImpl entry ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH entry.employment employment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.person person ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH subDepartment.department department ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.category ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH entry.creditsPeriod creditsPeriod ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.previousEmployment previousEmployment ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH previousEmployment.category previousCategory ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup ");
        queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup ");
        
        
        
        buildWhereClause2(creditsEntryQueryFilter,wheres,paramNames,values);
        

        String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

        String queryWithOrdering = queryWithoutOrdering;
        if (creditsEntryQueryFilter != null && creditsEntryQueryFilter.getOrderBy() != null) {
            queryWithOrdering += " ORDER BY LOWER(" + creditsEntryQueryFilter.getOrderBy() + ")"
                    + (creditsEntryQueryFilter.getOrderDirection().equals(OrderDirection.DESCENDING) ? " DESC " : " ASC ");
        } else {
            queryWithOrdering += " ORDER BY creditsPeriod.name ASC ,"+
                                 "          department.name ASC, "+
                                 "          LOWER(person.apellidoNombre) ASC";
                    
        }
        
        

        return new PageList<CreditsEntry>(
                findByNamedParam(" Select distinct entry  " + queryWithOrdering, paramNames, values, creditsEntryQueryFilter != null ? creditsEntryQueryFilter
                        .getStartIndex() : null, creditsEntryQueryFilter != null ? creditsEntryQueryFilter.getMaxResults() : null), countByNamedParam(
                " SELECT count(distinct entry) " + queryWithoutOrdering, paramNames, values));


    }
}

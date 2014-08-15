package org.dpi.employment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind.OrderDirection;
import org.janux.bus.persistence.BaseDAOHibernate;
import org.janux.util.Chronometer;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class EmploymentDaoHibImpl extends BaseDAOHibernate implements EmploymentDao
{
	@SuppressWarnings("unchecked")
	public List<Employment> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all employments");

		List<Employment> list = getHibernateTemplate().find("from EmploymentImpl order by codigo");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " employment in " + timer.printElapsedTime());

		return list;
	}	
	
	/*
	@SuppressWarnings( "unchecked" )
	public List<Employment> find(final EmploymentQueryFilter employmentQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Employment with filter '" + employmentQueryFilter.toString() + "'" );

		List<Employment> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(employmentQueryFilter);
				
				String select = "Select distinct employment ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmploymentImpl employment ");
				sb.append(" LEFT OUTER JOIN FETCH employment.person person");
				sb.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment");
				sb.append(" LEFT OUTER JOIN FETCH subDepartment.department ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH employment.creditsEntries creditsEntries");
				sb.append(" LEFT OUTER JOIN FETCH creditsEntries.creditsPeriod ");
				sb.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.minimumCategory parentOccupationalGroupMinimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.maximumCategory parentOccupationalGroupMaximumCategory ");
				

				sb.append(where);
				
				sb.append(" ORDER BY LOWER(person.apellidoNombre) asc ");
				
				Query query = sess.createQuery(select+sb.toString());
				
				setNamedParameters(query,employmentQueryFilter);
				
		
				List<Employment> list = query.list();
				return list;
			}


		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}*/
	
    public PageList<Employment> findEmployments(final EmploymentQueryFilter employmentQueryFilter) {

        List<String> wheres = new ArrayList<String>();
        List<String> paramNames = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        
		StringBuffer queryBuilder = new StringBuffer();
		queryBuilder.append(" FROM EmploymentImpl employment ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH employment.person person");
		queryBuilder.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment");
		queryBuilder.append(" LEFT OUTER JOIN FETCH subDepartment.department ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH employment.category ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH employment.creditsEntries creditsEntries");
		queryBuilder.append(" LEFT OUTER JOIN FETCH creditsEntries.creditsPeriod ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup");
		queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");
		queryBuilder.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.minimumCategory parentOccupationalGroupMinimumCategory ");
		queryBuilder.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.maximumCategory parentOccupationalGroupMaximumCategory ");
		
		//String wheres = " WHERE 1=1 "+buildWhereClause(employmentQueryFilter);

		buildWhereClause2(employmentQueryFilter,wheres,paramNames,values);
		

        String queryWithoutOrdering = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
                org.dpi.util.StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

        String queryWithOrdering = queryWithoutOrdering;
        if (employmentQueryFilter != null && employmentQueryFilter.getOrderBy() != null) {
        	queryWithOrdering += " ORDER BY BY LOWER(" + employmentQueryFilter.getOrderBy() + ")"
                    + (employmentQueryFilter.getOrderDirection().equals(OrderDirection.DESCENDING) ? " DESC " : " ASC ");
        } else {
        	queryWithOrdering += " ORDER BY LOWER(person.apellidoNombre) ASC ";
        }
        
        

        return new PageList<Employment>(
                findByNamedParam(" Select distinct employment  " + queryWithOrdering, paramNames, values, employmentQueryFilter != null ? employmentQueryFilter
                        .getStartIndex() : null, employmentQueryFilter != null ? employmentQueryFilter.getMaxResults() : null), countByNamedParam(
                " SELECT count(distinct employment) " + queryWithoutOrdering, paramNames, values));


    }
	
	private void buildWhereClause2(EmploymentQueryFilter employmentQueryFilter,
			List<String> wheres, List<String> paramNames, List<Object> values) {
		if (employmentQueryFilter != null) {
			
			String cuil = employmentQueryFilter.getCuil();
			if(StringUtils.hasText(cuil)) {
				wheres.add("  person.cuil like :cuil ");
				paramNames.add("cuil");
				values.add("%"+employmentQueryFilter.getCuil()+"%");
			}
			
			
			
			String apellidoNombre = employmentQueryFilter.getApellidoNombre();
			if(StringUtils.hasText(apellidoNombre)) {
		
				wheres.add("  upper(person.apellidoNombre) like :apellidoNombre ");
				paramNames.add("apellidoNombre");
				values.add("%"+employmentQueryFilter.getApellidoNombre().toUpperCase()+"%");
			}
			
			
			Long idDepartment = employmentQueryFilter.getDepartmentId();
			if(idDepartment!=null) {

				
				wheres.add("  subDepartment.department.id = :idDepartment ");
				paramNames.add("idDepartment");
				values.add(employmentQueryFilter.getDepartmentId());
			}
			
			String codigoCentro = employmentQueryFilter.getCodigoCentro();
			if(StringUtils.hasText(codigoCentro)) {
				
				wheres.add(" employment.subDepartment.codigoCentro = :codigoCentro ");
				paramNames.add("codigoCentro");
				values.add(employmentQueryFilter.getCodigoCentro());
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(StringUtils.hasText(codigoSector)) {

				wheres.add("  employment.subDepartment.codigoSector = :codigoSector ");
				paramNames.add("codigoSector");
				values.add(employmentQueryFilter.getCodigoSector());

			}
			
			

			
			String categoryCode = employmentQueryFilter.getCategoryCode();
			if(StringUtils.hasText(categoryCode)) {
				wheres.add("  employment.category.code = :categoryCode ");
				paramNames.add("categoryCode");
				values.add(categoryCode);
			}
			
			
			List<Long> personsIds = employmentQueryFilter.getPersonsIds();
			if(!CollectionUtils.isEmpty(personsIds)){
				/*if(personsIds.size()==1) {
					sb.append(" AND person.id = :personId ");
				}else{
					sb.append(" AND person.id IN ( :personsIds ) ");
				}*/
				
				wheres.add("  person.id IN ( :personsIds )  ");
				paramNames.add("personsIds");
				values.add(personsIds);
				
			}

			Long idEmployment = employmentQueryFilter.getEmploymentId();
			if(idEmployment!=null) {
				
				wheres.add("  employment.id = :idEmployment ");
				paramNames.add("idEmployment");
				values.add(idEmployment);
			}
			
			
			if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
				StringBuilder sb = new StringBuilder();

				sb.append(" (");
				
				int statusIndex = 1;
				for (Iterator iterator = employmentQueryFilter.getEmploymentStatuses().iterator(); iterator.hasNext();) {
					EmploymentStatus employmentStatus = (EmploymentStatus) iterator.next();
					sb.append(" employment.status = :employmentStatus"+statusIndex);
					
					paramNames.add("employmentStatus"+statusIndex);
					values.add(employmentStatus);
					
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

/*
	private String buildWhereClause(EmploymentQueryFilter employmentQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(employmentQueryFilter!=null) {
			String cuil = employmentQueryFilter.getCuil();
			if(StringUtils.hasText(cuil)) {
				sb.append(" AND person.cuil like :cuil ");
			}
			
			
			String apellidoNombre = employmentQueryFilter.getApellidoNombre();
			if(StringUtils.hasText(apellidoNombre)) {
				sb.append(" AND upper(person.apellidoNombre) like :apellidoNombre ");
			}
			
			
			String codigoCentro = employmentQueryFilter.getCodigoCentro();
			if(StringUtils.hasText(codigoCentro)) {
				sb.append(" AND employment.subDepartment.codigoCentro = :codigoCentro ");
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(StringUtils.hasText(codigoSector)) {
				sb.append(" AND employment.subDepartment.codigoSector = :codigoSector ");
			}
			
			String categoryCode = employmentQueryFilter.getCategoryCode();
			if(StringUtils.hasText(categoryCode)) {
				sb.append(" AND employment.category.code = :codigoSector ");
			}
			
			List<Long> personsIds = employmentQueryFilter.getPersonsIds();
			if(!CollectionUtils.isEmpty(personsIds)){
				if(personsIds.size()==1) {
					sb.append(" AND person.id = :personId ");
				}else{
					sb.append(" AND person.id IN ( :personsIds ) ");
				}
				
			}
			
			Long idDepartment = employmentQueryFilter.getDepartmentId();
			if(idDepartment!=null) {
				sb.append(" AND subDepartment.department.id = :idDepartment ");
			}

			Long idEmployment = employmentQueryFilter.getEmploymentId();
			if(idEmployment!=null) {
				sb.append(" AND employment.id = :idEmployment ");
			}
			
			if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
				sb.append(" AND (");
				int statusIndex = 1;
				for (Iterator iterator = employmentQueryFilter.getEmploymentStatuses().iterator(); iterator.hasNext();) {
					EmploymentStatus employmentStatus = (EmploymentStatus) iterator.next();
					sb.append(" employment.status = :employmentStatus"+statusIndex);
					if(iterator.hasNext()){
						sb.append(" OR ");
					}
					statusIndex++;
				}
				sb.append(" ) ");				
					
			}

		}
		return sb.toString();
	}*/
	
	
	/*public void setNamedParameters(Query query,
			EmploymentQueryFilter employmentQueryFilter) {
		if(employmentQueryFilter!=null) {
			String cuil = employmentQueryFilter.getCuil();
			if(StringUtils.hasText(cuil)) {
				query.setString("cuil", "%"+cuil+"%");
			}
			
			String apellidoNombre = employmentQueryFilter.getApellidoNombre();
			if(StringUtils.hasText(apellidoNombre)) {
				query.setString("apellidoNombre", "%"+apellidoNombre.toUpperCase()+"%");
			}
			
			String codigoCentro = employmentQueryFilter.getCodigoCentro();
			if(StringUtils.hasText(codigoCentro)) {
				query.setString("codigoCentro", codigoCentro);
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(StringUtils.hasText(codigoSector)) {
				query.setString("codigoSector", codigoSector);
			}
			
			String categoryCode = employmentQueryFilter.getCategoryCode();
			if(StringUtils.hasText(categoryCode)) {
				query.setString("categoryCode", categoryCode);
			}
			
			List<Long> personsIds = employmentQueryFilter.getPersonsIds();
			if(!CollectionUtils.isEmpty(personsIds)){
				if(personsIds.size()==1) {
					query.setLong("personsId", personsIds.get(0));
				}else{
					query.setParameterList("personsIds", personsIds);
				}
				
			}
			
			Long idDepartment = employmentQueryFilter.getDepartmentId();
			if(idDepartment!=null) {
				query.setLong("idDepartment", idDepartment);
			}

			String idEmployment = employmentQueryFilter.getEmploymentId();
			if(StringUtils.hasText(idEmployment)) {
				query.setString("idEmployment", idEmployment);
			}
			
			if(!CollectionUtils.isEmpty(employmentQueryFilter.getEmploymentStatuses())){
				
				int statusIndex = 1;
				for (Iterator iterator = employmentQueryFilter.getEmploymentStatuses().iterator(); iterator.hasNext();) {
					EmploymentStatus employmentStatus = (EmploymentStatus) iterator.next();
					query.setParameter("employmentStatus"+statusIndex, employmentStatus);
					statusIndex++;
				}
			
					
			}

		}

		
	}*/
	

	

	
	@Override
	public Employment findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Employment with id: '" + id + "'");

		List list = getHibernateTemplate().find("from EmploymentImpl where id=?", id);

		Employment employment = (list.size() > 0) ? (Employment)list.get(0) : null;

		if (employment == null) {
			log.warn("Unable to find Employment with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved employment with id: '" + id + "' in " + timer.printElapsedTime());

		return employment;
	}

	
}

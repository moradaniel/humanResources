package org.dpi.employment;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class EmploymentDaoHibImpl extends DataAccessHibImplAbstract implements EmploymentDao
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
				sb.append(" LEFT OUTER JOIN FETCH employment.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
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
				
				
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<Employment> list = query.list();
				return list;
			}


		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
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
				sb.append(" AND employment.centroSector.codigoCentro = :codigoCentro ");
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(StringUtils.hasText(codigoSector)) {
				sb.append(" AND employment.centroSector.codigoSector = :codigoSector ");
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
			
			String idReparticion = employmentQueryFilter.getReparticionId();
			if(StringUtils.hasText(idReparticion)) {
				sb.append(" AND centroSector.reparticion.id = :idReparticion ");
			}

			String idEmployment = employmentQueryFilter.getEmploymentId();
			if(StringUtils.hasText(idEmployment)) {
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
	}
	
	
	public void setNamedParameters(Query query,
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
			
			String idReparticion = employmentQueryFilter.getReparticionId();
			if(StringUtils.hasText(idReparticion)) {
				query.setString("idReparticion", idReparticion);
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

		
	}
	
	@SuppressWarnings( "unchecked" )
	public List<Employment> findInactivEmployments(final EmploymentQueryFilter employmentQueryFilter){
		if (log.isDebugEnabled()) log.debug("attempting to find Employment anterior a ascenso: movimiento Id: '" +/* movimentoAscenso.getId() + */"'" );

		List<Employment> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String select = "Select employment ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmploymentImpl employment ");
				sb.append(" LEFT OUTER JOIN FETCH employment.agente ");
				sb.append(" LEFT OUTER JOIN FETCH employment.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH employment.creditsEntries ");


				String where = " WHERE 1=1 "+buildWhereClause(employmentQueryFilter);
				sb.append(where);
				sb.append(" ORDER BY employment.endDate desc");
				
				Query q = sess.createQuery(select+sb.toString());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/			
				List<Employment> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}


	
	@SuppressWarnings( "unchecked" )
	public Employment findPreviousEmployment(final EmploymentQueryFilter employmentQueryFilter) {
		if (log.isDebugEnabled()) log.debug("attempting to find Employment anterior a ascenso: movimiento Id: '" +/* movimentoAscenso.getId() + */"'" );

		List<Employment> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String select = "Select employment ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmploymentImpl employment ");
				sb.append(" LEFT OUTER JOIN FETCH employment.agente ");
				sb.append(" LEFT OUTER JOIN FETCH employment.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH employment.creditsEntries ");


				String where = " WHERE 1=1 "+ buildWhereClause(employmentQueryFilter);
				sb.append(where);
				sb.append(" AND employment.endDate <= :endDate ");
				sb.append(" ORDER BY employment.endDate desc");
				
				Query q = sess.createQuery(select+sb.toString());
				
				q.setTimestamp("endDate", employmentQueryFilter.getEndDate());
				
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/			
				List<Employment> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return (list.size() > 0) ? (Employment)list.get(0) : null;

	}
	
	
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

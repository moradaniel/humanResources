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
				
				String select = "Select employment ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmploymentImpl employment ");
				sb.append(" LEFT OUTER JOIN FETCH employment.person person");
				sb.append(" LEFT OUTER JOIN FETCH employment.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH employment.creditsEntries ");
				sb.append(" LEFT OUTER JOIN FETCH employment.occupationalGroup occupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.minimumCategory parentOccupationalGroupMinimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.maximumCategory parentOccupationalGroupMaximumCategory ");
				

				sb.append(where);
				
				sb.append(" ORDER BY employment.person.apellidoNombre asc ");
				
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
	
	private String buildWhereClause(EmploymentQueryFilter employmentQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(employmentQueryFilter!=null) {
			String cuil = employmentQueryFilter.getCuil();
			if(cuil!=null) {
				sb.append(" AND employment.person.cuil = '").append(cuil).append("'");
			}
			
			String codigoCentro = employmentQueryFilter.getCodigoCentro();
			if(codigoCentro!=null) {
				sb.append(" AND employment.centroSector.codigoCentro = '").append(codigoCentro).append("'");
			}

			String codigoSector = employmentQueryFilter.getCodigoSector();
			if(codigoSector!=null) {
				sb.append(" AND employment.centroSector.codigoSector = '").append(codigoSector).append("'");
			}
			
			String categoryCode = employmentQueryFilter.getCategoryCode();
			if(categoryCode!=null) {
				sb.append(" AND employment.category.code = '").append(categoryCode).append("'");
			}
			
			List<Long> personsIds = employmentQueryFilter.getPersonsIds();
			if(!CollectionUtils.isEmpty(personsIds)){
				if(personsIds.size()==1) {
					sb.append(" AND person.id = '").append(personsIds.get(0)).append("'");
				}else{
					sb.append(" AND person.id IN ( ").append(personsIds)
					.append(StringUtils.collectionToDelimitedString(personsIds, ","))
					.append(")");
				}
				
			}
			
			String idReparticion = employmentQueryFilter.getReparticionId();
			if(idReparticion!=null) {
				sb.append(" AND centroSector.reparticion.id = '").append(idReparticion).append("'");
			}

			String idEmployment = employmentQueryFilter.getEmploymentId();
			if(idEmployment!=null) {
				sb.append(" AND employment.id = '").append(idEmployment).append("'");
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
				sb.append(" ORDER BY employment.fechaFin desc");
				
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
				sb.append(" AND employment.fechaFin <= :fechaFin ");
				sb.append(" ORDER BY employment.fechaFin desc");
				
				Query q = sess.createQuery(select+sb.toString());
				
				q.setTimestamp("fechaFin", employmentQueryFilter.getFechaFin());
				
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

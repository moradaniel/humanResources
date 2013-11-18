package org.dpi.empleo;

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
	public List<Empleo> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all empleos");

		List<Empleo> list = getHibernateTemplate().find("from EmpleoImpl order by codigo");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " empleo in " + timer.printElapsedTime());

		return list;
	}	
	
	
	@SuppressWarnings( "unchecked" )
	public List<Empleo> find(final EmploymentQueryFilter empleoQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Empleo with filter '" + empleoQueryFilter.toString() + "'" );

		List<Empleo> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(empleoQueryFilter);
				
				String select = "Select empleo ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmpleoImpl empleo ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.agente agente");
				sb.append(" LEFT OUTER JOIN FETCH empleo.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.category ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.movimientosCreditos ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.occupationalGroup occupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.minimumCategory minimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.maximumCategory maximumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.minimumCategory parentOccupationalGroupMinimumCategory ");
				sb.append(" LEFT OUTER JOIN FETCH parentOccupationalGroup.maximumCategory parentOccupationalGroupMaximumCategory ");
				

				sb.append(where);
				
				sb.append(" ORDER BY empleo.agente.apellidoNombre asc ");
				
				Query q = sess.createQuery(select+sb.toString());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<Empleo> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private String buildWhereClause(EmploymentQueryFilter empleoQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(empleoQueryFilter!=null) {
			String cuil = empleoQueryFilter.getCuil();
			if(cuil!=null) {
				sb.append(" AND empleo.agente.cuil = '").append(cuil).append("'");
			}
			
			String codigoCentro = empleoQueryFilter.getCodigoCentro();
			if(codigoCentro!=null) {
				sb.append(" AND empleo.centroSector.codigoCentro = '").append(codigoCentro).append("'");
			}

			String codigoSector = empleoQueryFilter.getCodigoSector();
			if(codigoSector!=null) {
				sb.append(" AND empleo.centroSector.codigoSector = '").append(codigoSector).append("'");
			}
			
			String categoryCode = empleoQueryFilter.getCategoryCode();
			if(categoryCode!=null) {
				sb.append(" AND empleo.category.code = '").append(categoryCode).append("'");
			}
			
			List<Long> agentesIds = empleoQueryFilter.getAgentesIds();
			if(!CollectionUtils.isEmpty(agentesIds)){
				if(agentesIds.size()==1) {
					sb.append(" AND agente.id = '").append(agentesIds.get(0)).append("'");
				}else{
					sb.append(" AND agente.id IN ( ").append(agentesIds)
					.append(StringUtils.collectionToDelimitedString(agentesIds, ","))
					.append(")");
				}
				
			}
			
			String idReparticion = empleoQueryFilter.getReparticionId();
			if(idReparticion!=null) {
				sb.append(" AND centroSector.reparticion.id = '").append(idReparticion).append("'");
			}

			String idEmpleo = empleoQueryFilter.getEmpleoId();
			if(idEmpleo!=null) {
				sb.append(" AND empleo.id = '").append(idEmpleo).append("'");
			}
			
			if(!CollectionUtils.isEmpty(empleoQueryFilter.getEstadosEmpleo())){
				sb.append(" AND (");
				for (Iterator iterator = empleoQueryFilter.getEstadosEmpleo().iterator(); iterator.hasNext();) {
					EmploymentStatus estadoEmpleo = (EmploymentStatus) iterator.next();
					sb.append(" empleo.estado = '"+estadoEmpleo.name()+"' ");
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
	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter){
		if (log.isDebugEnabled()) log.debug("attempting to find Empleo anterior a ascenso: movimiento Id: '" +/* movimentoAscenso.getId() + */"'" );

		List<Empleo> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String select = "Select empleo ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmpleoImpl empleo ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.agente ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.category ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.movimientosCreditos ");


				String where = " WHERE 1=1 "+buildWhereClause(empleoQueryFilter);
				sb.append(where);
				sb.append(" ORDER BY empleo.fechaFin desc");
				
				Query q = sess.createQuery(select+sb.toString());
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/			
				List<Empleo> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}


	
	@SuppressWarnings( "unchecked" )
	public Empleo findPreviousEmpleo(final EmploymentQueryFilter empleoQueryFilter) {
		if (log.isDebugEnabled()) log.debug("attempting to find Empleo anterior a ascenso: movimiento Id: '" +/* movimentoAscenso.getId() + */"'" );

		List<Empleo> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String select = "Select empleo ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM EmpleoImpl empleo ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.agente ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.category ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.movimientosCreditos ");


				String where = " WHERE 1=1 "+ buildWhereClause(empleoQueryFilter);
				sb.append(where);
				sb.append(" AND empleo.fechaFin <= :fechaFin ");
				sb.append(" ORDER BY empleo.fechaFin desc");
				
				Query q = sess.createQuery(select+sb.toString());
				
				q.setTimestamp("fechaFin", empleoQueryFilter.getFechaFin());
				
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/			
				List<Empleo> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return (list.size() > 0) ? (Empleo)list.get(0) : null;

	}
	
	
	@Override
	public Empleo findById(Long id) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Empleo with id: '" + id + "'");

		List list = getHibernateTemplate().find("from EmpleoImpl where id=?", id);

		Empleo empleo = (list.size() > 0) ? (Empleo)list.get(0) : null;

		if (empleo == null) {
			log.warn("Unable to find Empleo with id: '" + id + "'");
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with id: '" + id + "' in " + timer.printElapsedTime());

		return empleo;
	}

	
}

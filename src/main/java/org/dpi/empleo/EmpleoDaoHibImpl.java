package org.dpi.empleo;

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
public class EmpleoDaoHibImpl extends DataAccessHibImplAbstract implements EmpleoDao
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
	public List<Empleo> find(final EmpleoQueryFilter empleoQueryFilter){
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
				sb.append(" LEFT OUTER JOIN FETCH empleo.categoria ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.movimientosCreditos ");

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
	
	private String buildWhereClause(EmpleoQueryFilter empleoQueryFilter) {
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
			
			String codigoCategoria = empleoQueryFilter.getCodigoCategoria();
			if(codigoCategoria!=null) {
				sb.append(" AND empleo.categoria.codigo = '").append(codigoCategoria).append("'");
			}
			
			Long idAgente = empleoQueryFilter.getAgenteId();
			if(idAgente!=null) {
				sb.append(" AND agente.id = '").append(idAgente).append("'");
			}
			
			String idReparticion = empleoQueryFilter.getReparticionId();
			if(idReparticion!=null) {
				sb.append(" AND centroSector.reparticion.id = '").append(idReparticion).append("'");
			}

			String idEmpleo = empleoQueryFilter.getEmpleoId();
			if(idEmpleo!=null) {
				sb.append(" AND empleo.id = '").append(idEmpleo).append("'");
			}

			if(empleoQueryFilter.getEstadoEmpleo()!=null){
				switch(empleoQueryFilter.getEstadoEmpleo()){
					case ACTIVO: sb.append(" AND empleo.estado = '"+EstadoEmpleo.ACTIVO.name()+"' ");//AND empleo.fechaFin is null");
					break;
					case INACTIVO: sb.append(" AND empleo.fechaFin is not null");
					break;
					case TODOS: sb.append("");
					break;
	
				}
			}

		}
		return sb.toString();
	}
	
	@SuppressWarnings( "unchecked" )
	public List<Empleo> findEmpleosInactivos(final EmpleoQueryFilter empleoQueryFilter){
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
				sb.append(" LEFT OUTER JOIN FETCH empleo.categoria ");
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
	public Empleo findPreviousEmpleo(final EmpleoQueryFilter empleoQueryFilter) {
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
				sb.append(" LEFT OUTER JOIN FETCH empleo.categoria ");
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

	
}

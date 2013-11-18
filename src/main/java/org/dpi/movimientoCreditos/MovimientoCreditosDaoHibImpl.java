package org.dpi.movimientoCreditos;

import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import org.dpi.empleo.EmploymentQueryFilter;
import org.dpi.empleo.EmploymentStatus;
import org.dpi.movimientoCreditos.MovimientoCreditos.GrantedStatus;
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
public class MovimientoCreditosDaoHibImpl extends DataAccessHibImplAbstract implements MovimientoCreditosDao
{
	@SuppressWarnings("unchecked")
	public List<MovimientoCreditos> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all movimientos");

		List<MovimientoCreditos> list = getHibernateTemplate().find("from MovimientoCreditosImpl");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " movimiento in " + timer.printElapsedTime());

		return list;
	}	
	
	
	@SuppressWarnings( "unchecked" )
	public List<MovimientoCreditos> find(final MovimientoCreditosQueryFilter movimientoCreditosQueryFilter){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find movimiento with filter '" + movimientoCreditosQueryFilter.toString() + "'" );

		List<MovimientoCreditos> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(movimientoCreditosQueryFilter);
				
				String select = "Select movimiento ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM MovimientoCreditosImpl movimiento ");
				sb.append(" LEFT OUTER JOIN FETCH movimiento.empleo empleo");
				sb.append(" LEFT OUTER JOIN FETCH empleo.agente agente ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.categoria ");
				sb.append(" LEFT OUTER JOIN FETCH movimiento.creditsPeriod creditsPeriod ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.empleoAnterior empleoAnterior");
				sb.append(" LEFT OUTER JOIN FETCH empleoAnterior.categoria categoriaAnterior ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.occupationalGroup occupationalGroup");
				sb.append(" LEFT OUTER JOIN FETCH occupationalGroup.parentOccupationalGroup parentOccupationalGroup");

				sb.append(where);
				
				sb.append(" ORDER BY empleo.agente.apellidoNombre asc, ");
				sb.append(" empleo.fechaInicio desc ");
				
				Query q = sess.createQuery(select+sb.toString());
				
				
				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<MovimientoCreditos> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	public static  String buildWhereClause(MovimientoCreditosQueryFilter movimientoQueryFilter) {
		StringBuffer sb = new StringBuffer();
		
		if(!CollectionUtils.isEmpty(movimientoQueryFilter.getTiposMovimientoCreditos())){
			sb.append(" AND (");
			for (Iterator iterator = movimientoQueryFilter.getTiposMovimientoCreditos().iterator(); iterator.hasNext();) {
				TipoMovimientoCreditos tipoMovimientoCreditos = (TipoMovimientoCreditos) iterator.next();
				sb.append(" movimiento.tipoMovimientoCreditos = '"+tipoMovimientoCreditos.name()+"' ");
				if(iterator.hasNext()){
					sb.append(" OR ");
				}
			}
			sb.append(" ) ");				
				
		}
		
		if(!CollectionUtils.isEmpty(movimientoQueryFilter.getGrantedStatuses())){
			sb.append(" AND (");
			for (Iterator iterator = movimientoQueryFilter.getGrantedStatuses().iterator(); iterator.hasNext();) {
				GrantedStatus grantedStatus = (GrantedStatus) iterator.next();
				sb.append(" movimiento.grantedStatus = '"+grantedStatus.name()+"' ");
				if(iterator.hasNext()){
					sb.append(" OR ");
				}
			}
			sb.append(" ) ");				
				
		}


		if(movimientoQueryFilter.getId()!=null){
			sb.append(" AND movimiento.id = ").append(movimientoQueryFilter.getId()).append(" ");
		}
		
		if(movimientoQueryFilter.getIdCreditsPeriod()!=null){
			sb.append(" AND creditsPeriod.id = ").append(movimientoQueryFilter.getIdCreditsPeriod()).append(" ");
		}
		
		if(movimientoQueryFilter.hasCredits!=null && movimientoQueryFilter.hasCredits.booleanValue()==true){
			sb.append(" AND cantidadCreditos > 0 ").append(" ");
		}
		
		if(movimientoQueryFilter.getEmploymentQueryFilter()!=null) {
			EmploymentQueryFilter empleoQueryFilter = movimientoQueryFilter.getEmploymentQueryFilter();
			
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
			
			String idReparticion = empleoQueryFilter.getReparticionId();
			if(idReparticion!=null) {
				sb.append(" AND centroSector.reparticion.id = '").append(idReparticion).append("'");
			}

			String idEmpleo = empleoQueryFilter.getEmpleoId();
			if(idEmpleo!=null) {
				sb.append(" AND empleo.id = '").append(idEmpleo).append("'");
			}
			
			List<Long> agentesIds = empleoQueryFilter.getAgentesIds();
			if(!CollectionUtils.isEmpty(agentesIds)){
				if(agentesIds.size()==1) {
					sb.append(" AND agente.id = '").append(agentesIds.get(0)).append("'");
				}else{
						sb.append(" AND (1<>1 ");
						//take into account the limit of 1000 elements in an IN condition in Oracle
						List<List<Long>> listsOfAgentsIds = org.dpi.util.CollectionUtils.chopped(agentesIds, 999);
						for(List<Long> listOfAgentsIds :listsOfAgentsIds){
							sb.append(" OR agente.id IN ( ")
							.append(StringUtils.collectionToDelimitedString(listOfAgentsIds, ","))
							.append(")");
							
						}
						sb.append(")");

				}
				
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
}

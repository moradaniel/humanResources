package org.dpi.agente;

import java.sql.SQLException;
import java.util.List;

import org.dpi.empleo.EstadoEmpleo;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.util.StringUtils;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class AgenteDaoHibImpl extends DataAccessHibImplAbstract implements AgenteDao
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String LIST_QUERY = "SELECT agentes "
			+ "FROM AgenteImpl agentes ";

	private static final String LIST_QUERY_COUNT =  "SELECT count(agentes) "
			+ "FROM AgenteImpl agentes ";
	
	@SuppressWarnings("unchecked")
	public List<Agente> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all agentes");

		List<Agente> list = getHibernateTemplate().find("from AgenteImpl order by cuil");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " agente in " + timer.printElapsedTime());

		return list;
	}	
	
	public Agente findByCuil(String cuil){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Agente with cuil '" + cuil );

		String queryString = "from AgenteImpl where cuil=:cuil ";
		
		String[] paramNames = new String[1];
		Object[] paramValues = new Object[1];
		paramNames[0] = "cuil";
		paramValues[0] = cuil;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		Agente agente = (list.size() > 0) ? (Agente)list.get(0) : null;

		if (agente == null) {
			log.warn("Unable to find Agente with cuil '" + cuil);
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved agente with cuil'" + cuil+ "' in " + timer.printElapsedTime());

		return agente;
	}
	
	
	public PageList<Agente> findAgentes(final QueryBind bind,
			   final AgenteQueryFilter filter,
			   boolean isForExcel) {

		return doInHibernate(LIST_QUERY, LIST_QUERY_COUNT,
		buildWhereClauseAgenteRequest(null, "", bind, filter), bind);
	}
	
	private String buildWhereClauseAgenteRequest(Integer id, String KeyId, QueryBind bind, AgenteQueryFilter filter) {

		StringBuilder query_where = new StringBuilder();
//		 if the id is  null that means we want to retrieve all loans for all Area Manager
		if(id!=null){
			query_where.append("WHERE ").append(KeyId).append(".id = '").append(id).append("'");
		}else{
			// this is only to Start with a Where statement and follow with "AND" statement
			query_where.append("WHERE 1 = 1");
		}
		
		if(filter !=null){
			if(StringUtils.hasText(filter.getApellidoNombre())){
				
				query_where.append(" AND upper(agentes.apellidoNombre) like '%").append(filter.getApellidoNombre().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getCuil())){
				
				query_where.append(" AND agentes.cuil like '%").append(filter.getCuil()).append("%' ");
			}
			
		}

		//query_where.append(" AND consultancy.consultancyType = '").append(ConsultancyType.SMALLLOANREQUEST.ordinal()).append("' ");

		/*
		if(filter.isAreaConsultantCheckbox() && filter.getAreaConsultant() != null) {
			query_where.append(" AND areaConsultant.id = '").append(filter.getAreaConsultant().getId()).append("' ");
		}

		if (filter!=null){

			if(filter.getSmallLoanRequestStatus()!=null)
			{
				query_where.append(" AND lr.smallLoanRequestStatus = '").append(filter.getSmallLoanRequestStatus().ordinal()).append("'");
			}
			else
				if(!(filter.getAllStatus()!=null && filter.getAllStatus()) && filter.getSignedLoan()!=null)
				{
						if(filter.getSignedLoan()){
							query_where.append(" AND lr.smallLoanRequestStatus = '").append(SmallLoanRequestStatus.SIGNED.ordinal()).append("'");
						} else
						{
							query_where.append(" AND lr.smallLoanRequestStatus != '").append(SmallLoanRequestStatus.SIGNED.ordinal()).append("'");
						}
				}

			// SIGNATURE PROCESSING
			String[] contraintSignatures = buildHQLSignatureTimeConstraint(filter);
			if(contraintSignatures!=null){
				if(contraintSignatures.length > 1){
					query_where.append(" AND lr.finalDate ").append(contraintSignatures[0]);
					query_where.append(" AND lr.finalDate ").append(contraintSignatures[1]);
				}
				else{
					query_where.append(" AND lr.finalDate ").append(contraintSignatures[0]);
				}
			}

			// COMMISSION PROCESSING
			if(filter.isOpeningCommission()) {
				String[] openingComissionContraints = buildHQLOpeningComissionConstraint(filter);
				if(openingComissionContraints!=null){
					if(openingComissionContraints.length > 1){
						query_where.append(" AND lr.loanDescription.openingCommission ").append(openingComissionContraints[0]);
						query_where.append(" AND lr.loanDescription.openingCommission ").append(openingComissionContraints[1]);
					}
					else{
						query_where.append(" AND lr.loanDescription.openingCommission ").append(openingComissionContraints[0]);
					}
				}
			}

			// LOAN AMOUNT PROCESSING
			if(filter.isLoanAmout()){
				BigDecimal min = filter.getMinAmount();
				if(min != null)
				{
					query_where.append(" AND lr.loanDescription.amount.value >= ").append(min);
				}
				BigDecimal max = filter.getMaxAmount();

				// make sure its not null or you are not trying to get a value minor to 0
				if(max != null && !(max.compareTo(new BigDecimal("0"))==0))
				{
					query_where.append(" AND lr.loanDescription.amount.value <= ").append(max);
				}
			}

			// FINANCIAL ENTITY
			if(filter.isEntity()){
				FinancialEntity financialEntity = filter.getFinancialEntity();
				if(financialEntity != null)
				{
					query_where.append(" AND lr.financialEntity.id = ").append(financialEntity.getId());
				}
			}


			// Related PoS
			if(filter.isRelatedPoS()){
				PoS poS = filter.getPoS();
				if(poS != null)
				{
					query_where.append(" AND pos.id = ").append(poS.getId());
				}
			}

			if(filter.isRate()){

				String[] rate = buildHQLRateConstraint(filter);
				if(rate != null){
					if(rate.length > 1){
						query_where.append(" AND ").append("lr.loanDescription.rate " + rate[0]);
						query_where.append(" AND ").append("lr.loanDescription.rate " + rate[1]);
					}
					else{
						query_where.append(" AND lr.loanDescription.rate ").append(rate[0]);
					}
				}

			}


		}*/

		if (bind!=null){
			query_where.append(" ").append(bind.buildHQLOrderBy());
		}

		return query_where.toString();
	}

	
	private PageList doInHibernate(final String query, final String countQuery, final String whereClause, final QueryBind bind) {
		return (PageList) getHibernateTemplate().executeFind(new HibernateCallback() {

			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {

				if (log.isDebugEnabled()) {
					log.debug("HQL: " + query + whereClause);
				}

				Query q = sess.createQuery(query + whereClause);

				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}

				PageList pageList = new PageList(q.list());

				if (bind != null) {
					Query qCount = sess.createQuery(countQuery + whereClause);
					Long totalItems = (Long)qCount.uniqueResult();
					
					//Integer size = ((Long) qCount.uniqueResult()).intValue();
					pageList.setTotalItems(totalItems);
					pageList.setPageItems(bind.getCountElement());
					
					int pageItems =bind.getCountElement();
					long totalPageCount = totalItems / pageItems+1;
					long mod = totalItems % pageItems;
					if(mod == 0) totalPageCount -= 1;					
					pageList.setTotalPageCount(totalPageCount);
				}

				return pageList;
			}
		});
	}

	@Override
	public List<Agente> find(final AgenteQueryFilter agenteQueryFilter) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Agente with filter '" + agenteQueryFilter.toString() + "'" );

		@SuppressWarnings("unchecked")
		List<Agente> list = getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(agenteQueryFilter);
				
				String select = "Select distinct agente ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM AgenteImpl agente ");
				sb.append(" LEFT OUTER JOIN FETCH agente.empleos empleo");
				sb.append(" LEFT OUTER JOIN FETCH empleo.centroSector centroSector");
				sb.append(" LEFT OUTER JOIN FETCH centroSector.reparticion ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.categoria ");
				sb.append(" LEFT OUTER JOIN FETCH empleo.movimientosCreditos ");

				sb.append(where);
				
				sb.append(" ORDER BY agente.apellidoNombre asc ");
				
				Query q = sess.createQuery(select+sb.toString());
				
				if(agenteQueryFilter!=null)
				{
					if(agenteQueryFilter.getCuil()!=null){
						q.setParameter("cuil",agenteQueryFilter.getCuil());
					}
					
					if(agenteQueryFilter.getAgenteId()!=null){
						q.setLong("idAgente",agenteQueryFilter.getAgenteId());
					}

					if(agenteQueryFilter.getReparticionId()!=null){
						q.setParameter("idReparticion",agenteQueryFilter.getReparticionId());
					}

					if(agenteQueryFilter.getCondicionAgente()!=null){
						q.setParameter("condicionAgente",agenteQueryFilter.getCondicionAgente());
					}

				}

				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<Agente> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved empleo with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private String buildWhereClause(AgenteQueryFilter agenteQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(agenteQueryFilter!=null) {
			String cuil = agenteQueryFilter.getCuil();
			if(cuil!=null) {
				sb.append(" AND agente.cuil = :cuil ");
			}
			
			Long idAgente = agenteQueryFilter.getAgenteId();
			if(idAgente!=null) {
				sb.append(" AND agente.id = :idAgente ");
			}
			
			Long idReparticion = agenteQueryFilter.getReparticionId();
			if(idReparticion!=null) {
				sb.append(" AND centroSector.reparticion.id = :idReparticion ");
			}

			CondicionAgente condicionAgente = agenteQueryFilter.getCondicionAgente();
			if(condicionAgente!=null) {
				sb.append(" AND agente.condicion =  :condicionAgente ");
			}

			if(agenteQueryFilter.getEstadoAgente()!=null){
				switch(agenteQueryFilter.getEstadoAgente()){
					//un agente esta activo si tiene un empleo en estado ACTIVO
					case ACTIVO: 	sb.append(" AND  agente.id in ");
									sb.append(" (Select agenteActivo.id " );
									sb.append("	from AgenteImpl agenteActivo ");
									sb.append("	LEFT OUTER JOIN agenteActivo.empleos empleo2 ");
									sb.append(" LEFT OUTER JOIN empleo2.centroSector centroSector2");
									sb.append(" LEFT OUTER JOIN centroSector2.reparticion ");
									sb.append("	WHERE empleo2.estado = '"+EstadoEmpleo.ACTIVO.name()+"'");
									if(idReparticion!=null) {
										sb.append(" AND centroSector2.reparticion.id = '").append(idReparticion).append("'");
									}

									sb.append("	)");
							
							/*"exists ( "+
									 " from " +
									 " LEFT OUTER JOIN EmpleoImpl as empleo2 "+
									 "  where empleo2.agente = agente "+
									 "  and empleo2.estado = '"+EstadoEmpleo.ACTIVO.name()+"'"+
									 "	)");*/
									 

					break;
/*					case INACTIVO: sb.append(" AND empleo.fechaFin is not null");
					break;*/
					case TODOS: sb.append("");
					break;
	
				}
			}

		}
		return sb.toString();
	}


	
}

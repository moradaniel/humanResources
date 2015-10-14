package org.dpi.person;

import java.sql.SQLException;
import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.janux.bus.persistence.BaseDAOHibernate;
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
public class PersonDaoHibImpl extends BaseDAOHibernate implements PersonDao
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private static final String LIST_QUERY = "SELECT persons "
			+ "FROM PersonImpl persons ";

	private static final String LIST_QUERY_COUNT =  "SELECT count(persons) "
			+ "FROM PersonImpl persons ";
	
	@SuppressWarnings("unchecked")
	public List<Person> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all persons");

		List<Person> list = (List<Person>)getHibernateTemplate().find("from PersonImpl order by cuil");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " person in " + timer.printElapsedTime());

		return list;
	}	
	
	public Person findByCuil(String cuil){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Person with cuil '" + cuil );

		String queryString = "from PersonImpl where cuil=:cuil ";
		
		String[] paramNames = new String[1];
		Object[] paramValues = new Object[1];
		paramNames[0] = "cuil";
		paramValues[0] = cuil;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		Person person = (list.size() > 0) ? (Person)list.get(0) : null;

		if (person == null) {
			log.warn("Unable to find Person with cuil '" + cuil);
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved person with cuil'" + cuil+ "' in " + timer.printElapsedTime());

		return person;
	}
	
	
	public PageList<Person> findPersons(final QueryBind bind,
			   final PersonQueryFilter filter,
			   boolean isForExcel) {

		return doInHibernate(LIST_QUERY, LIST_QUERY_COUNT,
		buildWhereClausePersonRequest(null, "", bind, filter), bind);
	}
	
	private String buildWhereClausePersonRequest(Integer id, String KeyId, QueryBind bind, PersonQueryFilter filter) {

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
				
				query_where.append(" AND upper(persons.apellidoNombre) like '%").append(filter.getApellidoNombre().toUpperCase()).append("%' ");
			}
			
			if(StringUtils.hasText(filter.getCuil())){
				
				query_where.append(" AND persons.cuil like '%").append(filter.getCuil()).append("%' ");
			}
			
		}

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
	public List<Person> find(final PersonQueryFilter personQueryFilter) {
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Person with filter '" + personQueryFilter.toString() + "'" );

		@SuppressWarnings("unchecked")
		List<Person> list = (List<Person>)getHibernateTemplate().executeFind(new HibernateCallback() {
			
			public Object doInHibernate(Session sess)
					throws HibernateException, SQLException  {	
				
				String where = " WHERE 1=1 "+buildWhereClause(personQueryFilter);
				
				String select = "Select distinct person ";
				
				StringBuffer sb = new StringBuffer();
				sb.append(" FROM PersonImpl person ");
				sb.append(" LEFT OUTER JOIN FETCH person.employments employment");
				sb.append(" LEFT OUTER JOIN FETCH employment.subDepartment subDepartment");
				sb.append(" LEFT OUTER JOIN FETCH subDepartment.department ");
				sb.append(" LEFT OUTER JOIN FETCH employment.category ");
				sb.append(" LEFT OUTER JOIN FETCH employment.creditsEntries ");

				sb.append(where);
				
				sb.append(" ORDER BY person.apellidoNombre asc ");
				
				Query q = sess.createQuery(select+sb.toString());
				
				if(personQueryFilter!=null)
				{
					if(personQueryFilter.getCuil()!=null){
						q.setParameter("cuil",personQueryFilter.getCuil());
					}
					
					if(personQueryFilter.getPersonId()!=null){
						q.setLong("idPerson",personQueryFilter.getPersonId());
					}

					if(personQueryFilter.getDepartmentId()!=null){
						q.setParameter("idDepartment",personQueryFilter.getDepartmentId());
					}


				}

				/*q.setParameter("franchising", franchising);
				if (bind != null) {
					q.setMaxResults(bind.getCountToLastElement());
					q.setFirstResult(bind.getFirstElement());
				}*/				
				List<Person> list = q.list();
				return list;
			}
		});
		
		//if (log.isDebugEnabled()) log.debug("successfully retrieved employment with codigo '" + codigo+ "' in " + timer.printElapsedTime());
		return list;
	}
	
	private String buildWhereClause(PersonQueryFilter personQueryFilter) {
		StringBuffer sb = new StringBuffer();
		if(personQueryFilter!=null) {
			String cuil = personQueryFilter.getCuil();
			if(cuil!=null) {
				sb.append(" AND person.cuil = :cuil ");
			}
			
			Long idPerson = personQueryFilter.getPersonId();
			if(idPerson!=null) {
				sb.append(" AND person.id = :idPerson ");
			}
			
			Long idDepartment = personQueryFilter.getDepartmentId();
			if(idDepartment!=null) {
				sb.append(" AND subDepartment.department.id = :idDepartment ");
			}


			/*if(personQueryFilter.getEstadoPerson()!=null){
				switch(personQueryFilter.getEstadoPerson()){
					//un person esta activo si tiene un employment en estado ACTIVO
					case ACTIVO: 	sb.append(" AND  person.id in ");
									sb.append(" (Select personActivo.id " );
									sb.append("	from PersonImpl personActivo ");
									sb.append("	LEFT OUTER JOIN personActivo.employments employment2 ");
									sb.append(" LEFT OUTER JOIN employment2.subDepartment subDepartment2");
									sb.append(" LEFT OUTER JOIN subDepartment2.department ");
									sb.append("	WHERE employment2.estado = '"+EstadoEmpleo.ACTIVO.name()+"'");
									if(idDepartment!=null) {
										sb.append(" AND subDepartment2.department.id = '").append(idDepartment).append("'");
									}

									sb.append("	)");
							
								 

					break;
					case TODOS: sb.append("");
					break;
	
				}
			}*/

		}
		return sb.toString();
	}


	
}

package org.janux.bus.persistence;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.dpi.util.PageList;
import org.dpi.util.StringUtils;
import org.dpi.util.query.AbstractQueryFilter;
import org.dpi.util.query.QueryBind.OrderDirection;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.NonUniqueResultException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;


public class BaseDAOHibernate extends HibernateDaoSupport implements HibernateDataAccessObject {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	

	public static Object firstResult(List results) {
		return (results != null && results.size()>0) ? results.get(0) : null;
	}

	public BaseDAOHibernate() {
		super();
	}

	public void update(Object persistentObject) throws DataAccessException {
		getHibernateTemplate().update(persistentObject);
	}

	public Serializable save(Object persistentObject) throws DataAccessException {
		return getHibernateTemplate().save(persistentObject);
	}

	public void saveOrUpdate(Object persistentObject) throws DataAccessException {
		getHibernateTemplate().saveOrUpdate(persistentObject);
	}
	
	public void delete(Object persistentObject) throws DataAccessException {
		getHibernateTemplate().delete(persistentObject);
	}

	/**
	 * Reattach the persistentObject to the current session by executing a SQL select
	 * to the database
	 */
	public void refresh(final Object persistentObject) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session hibSession) throws HibernateException {
				hibSession.refresh(persistentObject);
				return null;
			}
		});
	}


	/**
	 * Attach an object to the current session given a lockMode. See {@link LockMode}.
	 * @param persistentObject
	 * @param lockMode
	 * @throws DataAccessException
	 */
	private void attach(final Object persistentObject, final LockMode lockMode) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session hibSession) throws HibernateException {
				hibSession.lock(persistentObject,lockMode);
				return null;
			}
		});
	}

	/**
	 * Reattach an object with the current session. This will not execute a select to the database so the detached instance has to be unmodified.
	 * The logical use would be the Hibernate refresh(object, LockMode.NONE) method but this is always executing a select even though the object was not 
	 * modified. So we have used the hibernate lock method with LockMode.NONE as a parameter to reattach an unmodified object to the current session 
	 * without executing a Select or Update to the database.
	 * @param persistentObject
	 * @throws DataAccessException
	 */
	public void attachClean(final Object persistentObject) throws DataAccessException {
		this.attach(persistentObject, LockMode.NONE);
	}

	public void evict(final Object persistentObject) throws DataAccessException {
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session hibSession) throws HibernateException {
				hibSession.evict(persistentObject);
				return null;
			}
		});
	}

	public void flush() 
	{
		getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session hibSession) throws HibernateException {
				hibSession.flush();
				return null;
			}
		});
	}

	public void clear() 
	{
		getHibernateTemplate().execute(
		new HibernateCallback() 
		{
			public Object doInHibernate(Session hibSession) throws HibernateException 
			{
				hibSession.clear();
				return (null);
			}
		});
	}
	
	
	public Object merge(final Object aObject) 
	{
		return (getHibernateTemplate().merge(aObject));
	}


	public Session getHibernateSession()
	{
		return super.getSession();
	}

	
	/** 
	 * convenience method to log profile messages at the INFO level 
	 * - wrap this method in a 'isInfoEnabled' if statement to minimize logging overhead
	 */
	protected void recordTime(String msg, long startTime)
	{
		org.janux.util.Profiler.recordTime(log, msg, startTime);
	}
	
	
	public int count(final DetachedCriteria detachedCriteria) {
		// todo hacking useNative is a result of SPR-2499 and will be removed soon
		boolean useNative = getHibernateTemplate().isExposeNativeSession();
		getHibernateTemplate().setExposeNativeSession(true);
		detachedCriteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
		Integer result = (Integer) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session).setProjection(
				        Projections.rowCount());
				return executableCriteria.uniqueResult();
			}
		});
		getHibernateTemplate().setExposeNativeSession(useNative);
		return result;
	}

	public List getInstances(final DetachedCriteria detachedCriteria) {
		return getInstances(detachedCriteria, null, null);
	}

	public List getInstances(final DetachedCriteria detachedCriteria, final Integer startIndex, final Integer maxResults) {
		return (List) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Criteria executableCriteria = detachedCriteria.getExecutableCriteria(session);
				if (startIndex != null) {
					executableCriteria.setFirstResult(startIndex);
				}
				if (maxResults != null) {
					executableCriteria.setMaxResults(maxResults);
				}
				return executableCriteria.list();
			}
		});
	}
	
	public List findByNamedParam(final String queryString, final List<String> paramNames, final List<Object> values)
			throws DataAccessException {
		return (List) findByNamedParam(queryString, paramNames, values, null, null, null);
	}

	public List findByNamedParam(final String queryString, final List<String> paramNames, final List<Object> values,
			final Integer startIndex, final Integer maxResults) throws DataAccessException {
		return (List) findByNamedParam(queryString, paramNames, values, null, startIndex, maxResults);
	}
	
	public long countByNamedParam(final String queryString, final List<String> paramNames, final List<Object> values)
			throws DataAccessException {
		
		String queryStringWithNoFetchs = queryString.replaceAll("FETCH", " ").replaceAll("fetch", " ");
		
		return (Long) uniqueElement((List) findByNamedParam(queryStringWithNoFetchs, paramNames, values, null, null, null));
	}
	
	public Object findByNamedParam(final String queryString, final List<String> paramNames, final List<Object> values, final Type[] types, final Integer startIndex, final Integer maxResults) throws DataAccessException {

			if (paramNames.size() != values.size()) {
				throw new IllegalArgumentException("Length of paramNames list must match length of values list");
			}
			if (types != null && paramNames.size() != types.length) {
				throw new IllegalArgumentException("Length of paramNames list must match length of types array");
			}
			
			return getHibernateTemplate().execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException {
					Query queryObject = session.createQuery(queryString);
					
					if (maxResults != null) {
						queryObject.setMaxResults(maxResults);
					}
					
					if (startIndex != null) {
						queryObject.setFirstResult(startIndex);
					}
					
					if (values != null) {
						for (int i = 0; i < values.size(); i++) {
							applyNamedParameterToQuery(queryObject, paramNames.get(i), values.get(i), (types != null ? types[i] : null));
						}
					}
					return queryObject.list();
				}
			});
		}
	
	/**
	 * ASC. This is a bile copy of org.springframework.orm.hibernate3.HibernateTemplate.applyNamedParameterToQuery
	 * that because of "visibility" it can be accessed from here.
	 * 
	 * Apply the given name parameter to the given Query object.
	 * 
	 * @param queryObject
	 *            the Query object
	 * @param paramName
	 *            the name of the parameter
	 * @param value
	 *            the value of the parameter
	 * @param type
	 *            Hibernate type of the parameter (or <code>null</code> if
	 *            none specified)
	 * @throws HibernateException
	 *             if thrown by the Query object
	 */
	public static void applyNamedParameterToQuery(Query queryObject, String paramName, Object value, Type type)
			throws HibernateException {

		if (value instanceof Collection) {
			if (type != null) {
				queryObject.setParameterList(paramName, (Collection) value, type);
			}
			else {
				queryObject.setParameterList(paramName, (Collection) value);
			}
		}
		else if (value instanceof Object[]) {
			if (type != null) {
				queryObject.setParameterList(paramName, (Object[]) value, type);
			}
			else {
				queryObject.setParameterList(paramName, (Object[]) value);
			}
		}
		else {
			if (type != null) {
				queryObject.setParameter(paramName, value, type);
			}
			else {
				queryObject.setParameter(paramName, value);
			}
		}
	}
	
	/**
	 * ASC: Same as applyNamedParameterToQuery this is a copy of
	 * org.hibernate.impl.AbstractQueryImpl.java.uniqueElement that's no visible
	 * from here.
	 * 
	 * @param list
	 * @return
	 * @throws NonUniqueResultException
	 */
	public static Object uniqueElement(List list) throws NonUniqueResultException {
		int size = list.size();
		if (size == 0)
			return null;
		Object first = list.get(0);
		for (int i = 1; i < size; i++) {
			if (list.get(i) != first) {
				throw new NonUniqueResultException(list.size());
			}
		}
		return first;
	}

	
	protected PageList getPageListByNamedParam(String mainEntityAlias, StringBuilder queryBuilder, List<String> wheres,
			List<String> paramNames, List<Object> values) {
		return getPageListByNamedParam(mainEntityAlias, null, queryBuilder, wheres, paramNames, values);
	}
	
	/**
	 * 
	 * Convenience method for easily processing, building and executing an HQL query.
	 * 
	 * @param mainEntityAlias
	 *            alias of the main entity in the query
	 * @param filter
	 *            the filter restrictions to apply to the query.
	 * @param queryBuilder
	 *            the beginning of a query expressed in Hibernate's query
	 *            language
	 * @param wheres
	 *            the list of query conditions to be satisfied
	 * @param paramNames
	 *            the names of the parameters
	 * @param values
	 *            the values of the parameters
	 * @return a PageList containing 0 or more persistent instances of main entity alias type
	 */
	protected PageList getPageListByNamedParam(String mainEntityAlias, AbstractQueryFilter filter, StringBuilder queryBuilder,
			List<String> wheres, List<String> paramNames, List<Object> values) {

		String query = wheres.isEmpty() ? queryBuilder.toString() : queryBuilder.append(" WHERE ").append(
				StringUtils.getStringsSeparatedBy(" AND ", wheres)).toString();

		if (filter != null && filter.getOrderBy() != null) {
			query += " ORDER BY " + filter.getOrderBy()
					+ (filter.getOrderDirection().equals(OrderDirection.DESCENDING) ? " DESC " : " ASC ");
		}

		String selectQuery = " SELECT " + mainEntityAlias + " " + query;
		String countQuery = " SELECT count(" + mainEntityAlias + ") " + query;
		
		if (filter != null) {
			return new PageList(findByNamedParam(selectQuery, paramNames, values, filter.getStartIndex(), filter
					.getMaxResults()), countByNamedParam(countQuery, paramNames, values));
		} else {
			return new PageList(findByNamedParam(selectQuery, paramNames, values, null, null), countByNamedParam(
					countQuery, paramNames, values));
		}
	}


}

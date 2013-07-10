package org.janux.bus.persistence;

import java.io.Serializable;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Base class for all Data Access classes implemented using Hibernate and
 * String declarative transactions.
 * 
 * @author  <a href="mailto:philippe.paravicini@janux.org">Philippe Paravicini</a>
 * @version cvs: $Id: DataAccessHibImplAbstract.java,v 1.7 2007-12-18 19:30:39 philippe Exp $
 *
 */
public class DataAccessHibImplAbstract extends HibernateDaoSupport implements HibernateDataAccessObject {

	protected Logger log = LoggerFactory.getLogger(this.getClass());
	

	public static Object firstResult(List results) {
		return (results != null && results.size()>0) ? results.get(0) : null;
	}

	public DataAccessHibImplAbstract() {
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
}

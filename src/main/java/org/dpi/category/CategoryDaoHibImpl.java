package org.dpi.category;

import java.util.List;

import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CategoryDaoHibImpl extends DataAccessHibImplAbstract implements CategoryDao
{
	@SuppressWarnings("unchecked")
	public List<Category> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all categories");

		List<Category> list = getHibernateTemplate().find("from CategoryImpl order by code");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " category in " + timer.printElapsedTime());

		return list;
	}	
	
	public Category findByCode(String code){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Category with code '" + code + "'" );

		String queryString = "from CategoryImpl where code=:code ";
		
		String[] paramNames = new String[1];
		Object[] paramValues = new Object[1];
		paramNames[0] = "code";
		paramValues[0] = code;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		Category category = (list.size() > 0) ? (Category)list.get(0) : null;

		if (category == null) {
			log.warn("Unable to find Category with code '" + code);
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved category with code '" + code+ "' in " + timer.printElapsedTime());

		return category;
	}
}

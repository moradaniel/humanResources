package org.dpi.categoria;

import java.util.List;

import org.janux.bus.persistence.DataAccessHibImplAbstract;
import org.janux.util.Chronometer;

/**
 * Used to create, save, retrieve, update and delete objects from
 * persistent storage
 *
 */
public class CategoriaDaoHibImpl extends DataAccessHibImplAbstract implements CategoriaDao
{
	@SuppressWarnings("unchecked")
	public List<Categoria> findAll()
	{
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find all categorias");

		List<Categoria> list = getHibernateTemplate().find("from CategoriaImpl order by codigo");

		if (log.isInfoEnabled()) log.info("successfully retrieved " + list.size() + " categoria in " + timer.printElapsedTime());

		return list;
	}	
	
	public Categoria findByCodigo(String codigo){
		Chronometer timer = new Chronometer();

		if (log.isDebugEnabled()) log.debug("attempting to find Categoria with codigo '" + codigo + "'" );

		String queryString = "from CategoriaImpl where codigo=:codigo ";
		
		String[] paramNames = new String[1];
		Object[] paramValues = new Object[1];
		paramNames[0] = "codigo";
		paramValues[0] = codigo;
		
		List list = getHibernateTemplate().findByNamedParam(queryString, paramNames, paramValues);

		Categoria categoria = (list.size() > 0) ? (Categoria)list.get(0) : null;

		if (categoria == null) {
			log.warn("Unable to find Categoria with codigo '" + codigo);
			return null;
		}

		if (log.isDebugEnabled()) log.debug("successfully retrieved categoria with codigo '" + codigo+ "' in " + timer.printElapsedTime());

		return categoria;
	}
}

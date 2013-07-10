package org.dpi.movimientoCreditos;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface MovimientoCreditosDao extends HibernateDataAccessObject
{

	public List<MovimientoCreditos> findAll();
	
	public List<MovimientoCreditos> find(MovimientoCreditosQueryFilter movimientoCreditosFilter);
	
}

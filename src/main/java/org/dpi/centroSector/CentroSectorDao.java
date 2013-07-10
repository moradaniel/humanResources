package org.dpi.centroSector;

import java.util.List;

import org.dpi.reparticion.Reparticion;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface CentroSectorDao extends HibernateDataAccessObject
{

	public List<CentroSector> findAll();
	
	public CentroSector findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector);
	
	public PageList<CentroSector> findCentroSectores(final QueryBind bind,
			   final CentroSectorQueryFilter filter,
			   boolean isForExcel);
	
	public List<CentroSector> findCentroSectores(final Reparticion reparticion);
	
	public CentroSector findById(Long id);
	
	
}

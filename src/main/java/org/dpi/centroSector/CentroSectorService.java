package org.dpi.centroSector;

import java.util.List;

import org.dpi.reparticion.Reparticion;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.EntityNotFoundException;
import org.springframework.context.ApplicationContextAware;


/**
 * Used to create, save, retrieve, update and delete CentroSector objects from
 * persistent storage
 *
 */
public interface CentroSectorService extends ApplicationContextAware
{

	public CentroSector findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector);

	public PageList<CentroSector> findCentroSectores(final QueryBind bind,
			   final CentroSectorQueryFilter filter,
			   boolean isForExcel);
	
	public List<CentroSector> findCentroSectores(final Reparticion reparticion);
	
	public CentroSector findById(Long id) throws EntityNotFoundException;

	public void save(final CentroSector aReparticion); 
	
	public void saveOrUpdate(final CentroSector aReparticion); 
	
	public CentroSectorDao getCentroSectorDao();

}

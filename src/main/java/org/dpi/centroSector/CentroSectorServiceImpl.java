package org.dpi.centroSector;

import java.util.List;

import org.dpi.reparticion.Reparticion;
import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.janux.bus.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;



public class CentroSectorServiceImpl implements CentroSectorService
{
	Logger log = LoggerFactory.getLogger(this.getClass());;
	private final CentroSectorDao centroSectorDao;
	private ApplicationContext applicationContext;
	
	public CentroSectorServiceImpl(final CentroSectorDao reparticionDao) {
		this.centroSectorDao = reparticionDao;
	}

	public CentroSector findByCodigoCentroCodigoSector(String codigoCentro, String codigoSector){
		
		return centroSectorDao.findByCodigoCentroCodigoSector(codigoCentro,codigoSector);
	}

	
	public void save(final CentroSector aReparticion) 
	{
		centroSectorDao.save(aReparticion);
	}
	
	public void saveOrUpdate(final CentroSector aReparticion) 
	{
		centroSectorDao.saveOrUpdate(aReparticion);
	}

	
	public CentroSector findById(Long id) throws EntityNotFoundException {
		return centroSectorDao.findById(id);
	}


	public CentroSectorDao getCentroSectorDao()
	{
		return centroSectorDao;
	}

	public List<CentroSector> findAll(){
		return this.centroSectorDao.findAll();
	}
	

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}


	@Override
	public PageList<CentroSector> findCentroSectores(QueryBind bind,
			CentroSectorQueryFilter filter, boolean isForExcel) {
		return this.centroSectorDao.findCentroSectores(bind, filter, isForExcel);
	}
	
	@Override
	public List<CentroSector> findCentroSectores(final Reparticion reparticion){
		return this.centroSectorDao.findCentroSectores(reparticion);
	}
}

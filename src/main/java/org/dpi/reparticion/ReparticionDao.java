package org.dpi.reparticion;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;
import org.springframework.samples.travel.SearchCriteria;


public interface ReparticionDao extends HibernateDataAccessObject
{

	public List<Reparticion> findAll();

	public Reparticion findByNombre(String nombre);

	public Reparticion findById(Long id);

	public List<Reparticion> findReparticiones(SearchCriteria criteria);
	
	public List<ReparticionSearchInfo> findAllReparticiones();
	
}

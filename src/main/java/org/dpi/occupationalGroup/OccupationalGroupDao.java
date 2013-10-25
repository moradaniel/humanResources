package org.dpi.occupationalGroup;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

public interface OccupationalGroupDao extends HibernateDataAccessObject{
	
	public OccupationalGroup findById(Long id);

	public List<OccupationalGroup> findAll();

	public List<OccupationalGroup> find(
			OccupationalGroupQueryFilter occupationalGroupQueryFilter);
	
	//public List<OccupationalGroup> find(OccupationalGroupQueryFilter occupationalGroupQueryFilter);
	

}

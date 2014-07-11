package org.dpi.occupationalGroup;


import java.util.List;

import org.springframework.context.ApplicationContextAware;

/**
 * Used to create, save, retrieve, update and delete {@link OccupationalGroup} objects from
 * persistent storage
 *
 */
public interface OccupationalGroupService extends ApplicationContextAware {

		public OccupationalGroup findById(Long id);
		
		public void delete(OccupationalGroup occupationalGroup);
			
		public void save(final OccupationalGroup occupationalGroup); 
		
		public void saveOrUpdate(final OccupationalGroup occupationalGroup); 
		
		public OccupationalGroupDao getOccupationalGroupDao();

		public List<OccupationalGroup> find(OccupationalGroupQueryFilter occupationalGroupQueryFilter);

	}

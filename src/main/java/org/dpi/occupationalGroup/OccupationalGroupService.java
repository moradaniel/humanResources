package org.dpi.occupationalGroup;


import java.util.List;

import org.springframework.context.ApplicationContextAware;

/**
 * Used to create, save, retrieve, update and delete {@link OccupationalGroup} objects from
 * persistent storage
 *
 */
public interface OccupationalGroupService extends ApplicationContextAware {


		/**
		 * Returns a possibly lightweight representation of the corresponding {@link OccupationalGroup}, which may not
		 * contain all associated objects, or <code>null</code> if the {@link OccupationalGroup} is not found.
		 *
		 * @param code a business code that uniquely identifies this {@link OccupationalGroup}
		 */
		//public List<OccupationalGroup> find(EmploymentQueryFilter occupationalGroupQueryFilter);

		public OccupationalGroup findById(Long id);
		
		//public List<OccupationalGroup> findOccupationalGroupsInactivos(final EmploymentQueryFilter occupationalGroupQueryFilter);
		
		public void delete(OccupationalGroup occupationalGroup);
			
		public void save(final OccupationalGroup occupationalGroup); 
		
		public void saveOrUpdate(final OccupationalGroup occupationalGroup); 
		
		public OccupationalGroupDao getOccupationalGroupDao();

		public List<OccupationalGroup> find(
				OccupationalGroupQueryFilter occupationalGroupQueryFilter);


	}

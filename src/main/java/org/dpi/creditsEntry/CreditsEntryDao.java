package org.dpi.creditsEntry;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface CreditsEntryDao extends HibernateDataAccessObject
{

	public List<CreditsEntry> findAll();
	
	public List<CreditsEntry> find(CreditsEntryQueryFilter creditsEntryFilter);
	
}

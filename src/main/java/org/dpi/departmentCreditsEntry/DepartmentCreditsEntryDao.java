package org.dpi.departmentCreditsEntry;

import java.util.List;

import org.dpi.util.PageList;
import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface DepartmentCreditsEntryDao extends HibernateDataAccessObject
{

	public List<DepartmentCreditsEntry> findAll();
	
	public List<DepartmentCreditsEntry> find(DepartmentCreditsEntryQueryFilter creditsEntryFilter);
	
	public PageList<DepartmentCreditsEntry> findCreditsEntries(final DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter);
	
}

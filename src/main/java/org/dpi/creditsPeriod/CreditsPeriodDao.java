package org.dpi.creditsPeriod;

import java.util.List;

import org.janux.bus.persistence.HibernateDataAccessObject;

/**
 * Used to create, save, retrieve, update and delete  objects from
 * persistent storage
 */
public interface CreditsPeriodDao extends HibernateDataAccessObject
{

	public List<CreditsPeriod> findAll();
	
	public List<CreditsPeriod> find(CreditsPeriodQueryFilter employmentQueryFilter);
	
	
}

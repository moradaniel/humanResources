package org.dpi.creditsPeriod;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContextAware;

import biz.janux.calendar.DateRange;


/**
 * Used to create, save, retrieve, update and delete CreditsPeriod objects from
 * persistent storage
 *
 */
public interface CreditsPeriodService extends ApplicationContextAware
{
	
	/**
	 * DateRange that represents the period of time representing a CreditsPeriod
	 * @param year
	 * @return
	 */
	public DateRange getDateRangeForYear(int year);
	
	
	public boolean isDateWithinEjercicioAnual(Date date, int year);
	
	public List<CreditsPeriod> find(CreditsPeriodQueryFilter creditsPeriodQueryFilter);

	public void delete(CreditsPeriod employment);
		
	public void save(final CreditsPeriod employment); 
	
	public void saveOrUpdate(final CreditsPeriod employment); 
	
	public CreditsPeriodDao getCreditsPeriodDao();
	
	public CreditsPeriod getCurrentCreditsPeriod();


}

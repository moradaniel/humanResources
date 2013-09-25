package org.dpi.creditsPeriod;

import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContextAware;

import biz.janux.calendar.DateRange;


/**
 * Used to create, save, retrieve, update and delete Hotel objects from
 * persistent storage
 *
 */
public interface CreditsPeriodService extends ApplicationContextAware
{
	
	/**
	 * DateRange that represents the period of time representing a Ejercicio Anual
	 * @param year
	 * @return
	 */
	public DateRange getDateRangeForEjercicioAnual(int year);
	
	
	public boolean isDateWithinEjercicioAnual(Date date, int year);
	
	public int getCurrentCreditsPeriodYear();
	
	public List<CreditsPeriod> find(CreditsPeriodQueryFilter creditsPeriodQueryFilter);

	public void delete(CreditsPeriod empleo);
	
	
	public void save(final CreditsPeriod empleo); 
	
	public void saveOrUpdate(final CreditsPeriod empleo); 
	
	public CreditsPeriodDao getCreditsPeriodDao();
	
	public CreditsPeriod getCurrentCreditsPeriod();


}

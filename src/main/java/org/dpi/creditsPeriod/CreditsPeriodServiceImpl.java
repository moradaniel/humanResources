package org.dpi.creditsPeriod;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod.Status;
import org.dpi.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import biz.janux.calendar.DateRange;



public class CreditsPeriodServiceImpl implements CreditsPeriodService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	final DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");
	
	private final CreditsPeriodDao creditsPeriodDao;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	
	@Resource(name = "categoryService")
	private CategoryService categoryService;
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	

	private ApplicationContext applicationContext;
	
	public CreditsPeriodServiceImpl(final CreditsPeriodDao creditsPeriodDao) {
		this.creditsPeriodDao = creditsPeriodDao;
	}

	
	public List<CreditsPeriod> find(CreditsPeriodQueryFilter creditsPeriodQueryFilter){
		
		return creditsPeriodDao.find(creditsPeriodQueryFilter);
	}

	
	public void save(final CreditsPeriod creditsPeriod) 
	{
		creditsPeriodDao.save(creditsPeriod);

	}
	
	public void saveOrUpdate(final CreditsPeriod creditsPeriod) 
	{
		
		if (creditsPeriod.getId() != null) { // it is an update
			creditsPeriodDao.merge(creditsPeriod);
			} else { // you are saving a new one
				creditsPeriodDao.saveOrUpdate(creditsPeriod);
			}
		
	}
	
	public void delete(CreditsPeriod creditsPeriod){
		creditsPeriodDao.delete(creditsPeriod);
	}


	public CreditsPeriodDao getCreditsPeriodDao()
	{
		return creditsPeriodDao;
	}
	
	
	public List<CreditsPeriod> findAll(){
		return this.creditsPeriodDao.findAll();
	}
	
	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}
	
	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}


	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	public PersonService getPersonService() {
		return personService;
	}


	public void setPersonService(PersonService personService) {
		this.personService = personService;
	}

	public CentroSectorService getCentroSectorService() {
		return centroSectorService;
	}


	public void setCentroSectorService(CentroSectorService centroSectorService) {
		this.centroSectorService = centroSectorService;
	}


	@Override
	public DateRange getDateRangeForYear(int year) {
		Date startDate	 = null;
		Date endDate	 = null;

		try{
			startDate	 = format.parse("01/07/"+ year + " 00:00:00.000");
			endDate	 = format.parse("30/06/"+ year + " 23:59:59.999");
		}catch(ParseException ex){
			log.error(ex.getMessage());
			throw new RuntimeException(ex);
		}
		DateRange dateRange = new DateRange(startDate, endDate);
		return dateRange;
	}


	@Override
	public boolean isDateWithinEjercicioAnual(Date date, int year) {
		DateRange dateRange =  getDateRangeForYear(year);
		return dateRange.contains(date);
	}


	@Override
	public CreditsPeriod getCurrentCreditsPeriod(){
		CreditsPeriodQueryFilter creditsPeriodQueryFilter = new CreditsPeriodQueryFilter();
		creditsPeriodQueryFilter.addStatus(Status.Active);
						
		//get current period
		List<CreditsPeriod> currentCreditsPeriods = find(creditsPeriodQueryFilter);
		CreditsPeriod currentCreditsPeriod = currentCreditsPeriods.get(0);
		return currentCreditsPeriod;
	}
	
}

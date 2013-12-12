package org.dpi.employment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.category.Category;
import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.person.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;



public class EmploymentServiceImpl implements EmploymentService
{
	
	Logger log = LoggerFactory.getLogger(this.getClass());
	
	private final EmploymentDao employmentDao;
	
	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;
	
	@Resource(name = "categoryService")
	private CategoryService categoryService;

	@Resource(name = "occupationalGroupService")
	private OccupationalGroupService occupationalGroupService;
	
	
	@Resource(name = "personService")
	private PersonService personService;
	
	@Resource(name = "centroSectorService")
	private CentroSectorService centroSectorService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	private ApplicationContext applicationContext;
	
	public EmploymentServiceImpl(final EmploymentDao employmentDao) {
		this.employmentDao = employmentDao;
	}

	
	public List<Employment> find(EmploymentQueryFilter employmentQueryFilter){
		
		return employmentDao.find(employmentQueryFilter);
	}

	public Employment findById(Long id){
		return employmentDao.findById(id);
	}
	
	public void save(final Employment employment) 
	{
		employmentDao.save(employment);
	}
	
	public void saveOrUpdate(final Employment employment) 
	{
		if (employment.getId() != null) { // it is an update
			 employmentDao.merge(employment);
			} else { // you are saving a new one
			 employmentDao.saveOrUpdate(employment);
			}
	}
	
	public void delete(Employment employment){
		employmentDao.delete(employment);
	}

	public EmploymentDao getEmploymentDao()
	{
		return employmentDao;
	}

	public List<Employment> findAll(){
		return this.employmentDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	public List<Employment> findInactiveEmployments(final EmploymentQueryFilter employmentQueryFilter){
		employmentQueryFilter.addEmploymentStatus(EmploymentStatus.INACTIVO);
		
		return this.employmentDao.findInactivEmployments(employmentQueryFilter);
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


	@Override
	public Employment findPreviousEmployment(Employment employment){
		EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();
		employmentQueryFilter.setCuil(employment.getPerson().getCuil());
		employmentQueryFilter.setReparticionId(employment.getCentroSector().getReparticion().getId().toString());
		
		employmentQueryFilter.setEmploymentStatuses( CollectionUtils.arrayToList(EmploymentStatus.values()));
		employmentQueryFilter.setFechaFin(employment.getFechaInicio());

		return this.employmentDao.findPreviousEmployment(employmentQueryFilter);
	}
	
	
	public List<Category> getAvailableCategoriesForPromotion(Employment employment){
		List <Category> availableCategories = new ArrayList<Category>();
		List <Category> allCategories = this.categoryService.findAll();

		//get first level occupational group
		OccupationalGroup employmentOccupationalGroup = this.occupationalGroupService.findById(employment.getOccupationalGroup().getId());
		
		//get second level occupational group
		OccupationalGroup parentEmploymentOccupationalGroup = employmentOccupationalGroup.getParentOccupationalGroup(); 
		
		int minimumCategoryCodeInt = Integer.parseInt(parentEmploymentOccupationalGroup.getMinimumCategory().getCode());
		int maximumCategoryCodeInt = Integer.parseInt(parentEmploymentOccupationalGroup.getMaximumCategory().getCode());
		
		int currentEmploymentCategoryCodeInt = Integer.parseInt(employment.getCategory().getCode());
		
		for(Category category: allCategories){
			Integer categoryCode = Integer.parseInt(category.getCode());
			if(categoryCode >= minimumCategoryCodeInt && categoryCode <= maximumCategoryCodeInt){
				if(categoryCode > currentEmploymentCategoryCodeInt ){
					availableCategories.add(category);
				}
			}
		}

		return availableCategories;

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

	
}

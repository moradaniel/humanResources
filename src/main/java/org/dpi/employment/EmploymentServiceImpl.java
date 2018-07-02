package org.dpi.employment;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.category.Category;
import org.dpi.category.CategoryService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.occupationalGroup.OccupationalGroup;
import org.dpi.occupationalGroup.OccupationalGroupService;
import org.dpi.person.PersonService;
import org.dpi.subDepartment.SubDepartmentService;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;



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
	
	@Resource(name = "subDepartmentService")
	private SubDepartmentService subDepartmentService;
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;

	private ApplicationContext applicationContext;
	
	public EmploymentServiceImpl(final EmploymentDao employmentDao) {
		this.employmentDao = employmentDao;
	}

	
	/*public List<Employment> find(EmploymentQueryFilter employmentQueryFilter){
		
		return employmentDao.find(employmentQueryFilter);
	}*/
	
	
	public PageList<Employment> findEmployments(EmploymentQueryFilter employmentQueryFilter){
		
		return employmentDao.findEmployments(employmentQueryFilter);
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


	
	
	public List<Category> getAvailableCategoriesForPromotion(Employment employment){
	    
	    Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	    
	    
	        
		List <Category> availableCategories = new ArrayList<Category>();
		List <Category> allCategories = this.categoryService.findAll();

		//get first level occupational group
		OccupationalGroup employmentOccupationalGroup = this.occupationalGroupService.findById(employment.getOccupationalGroup().getId());
		
		//get second level occupational group
		OccupationalGroup parentEmploymentOccupationalGroup = employmentOccupationalGroup.getParentOccupationalGroup(); 
		
		int minimumCategoryCodeInt = Integer.parseInt(parentEmploymentOccupationalGroup.getMinimumCategory().getCode());
		int maximumCategoryCodeInt = Integer.parseInt(parentEmploymentOccupationalGroup.getMaximumCategory().getCode());
		
		int currentEmploymentCategoryCodeInt = Integer.parseInt(employment.getCategory().getCode());
		
		for(Category possibleCategory: allCategories){
		    Integer possibleCategoryCode = Integer.parseInt(possibleCategory.getCode());
		    if(possibleCategoryCode >= minimumCategoryCodeInt && possibleCategoryCode <= maximumCategoryCodeInt){
		        if(possibleCategoryCode > currentEmploymentCategoryCodeInt) {
		            if(/*CreditsEntryServiceImpl.canPromoteToUnlimitedCategory(currentUser)*/ true ||
		                    //not possible to promote more than 2 categories
		                    possibleCategoryCode <=  currentEmploymentCategoryCodeInt + 2  )
		            {
		                availableCategories.add(possibleCategory);
		            }
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
	

	
	public SubDepartmentService getSubDepartmentService() {
		return subDepartmentService;
	}


	public void setSubDepartmentService(SubDepartmentService subDepartmentService) {
		this.subDepartmentService = subDepartmentService;
	}
	
}

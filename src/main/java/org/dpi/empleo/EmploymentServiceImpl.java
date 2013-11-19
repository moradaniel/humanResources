package org.dpi.empleo;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.dpi.category.Category;
import org.dpi.category.CategoryService;
import org.dpi.centroSector.CentroSectorService;
import org.dpi.configuracionAsignacionCreditos.AdministradorCreditosService;
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
	
	private final EmploymentDao empleoDao;
	
	@Resource(name = "administradorCreditosService")
	private AdministradorCreditosService administradorCreditosService;
	
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
	
	public EmploymentServiceImpl(final EmploymentDao empleoDao) {
		this.empleoDao = empleoDao;
	}

	
	public List<Empleo> find(EmploymentQueryFilter empleoQueryFilter){
		
		return empleoDao.find(empleoQueryFilter);
	}

	public Empleo findById(Long id){
		return empleoDao.findById(id);
	}
	
	public void save(final Empleo empleo) 
	{
		empleoDao.save(empleo);
	}
	
	public void saveOrUpdate(final Empleo empleo) 
	{
		if (empleo.getId() != null) { // it is an update
			 empleoDao.merge(empleo);
			} else { // you are saving a new one
			 empleoDao.saveOrUpdate(empleo);
			}
	}
	
	public void delete(Empleo empleo){
		empleoDao.delete(empleo);
	}

	public EmploymentDao getEmploymentDao()
	{
		return empleoDao;
	}

	public List<Empleo> findAll(){
		return this.empleoDao.findAll();
	}

	public void setApplicationContext(final ApplicationContext aApplicationContext) throws BeansException
	{
		this.applicationContext = aApplicationContext;
	}

	public List<Empleo> findEmpleosInactivos(final EmploymentQueryFilter empleoQueryFilter){
		empleoQueryFilter.addEstadoEmpleo(EmploymentStatus.INACTIVO);
		
		return this.empleoDao.findEmpleosInactivos(empleoQueryFilter);
	}
	
	public AdministradorCreditosService getAdministradorCreditosService() {
		return administradorCreditosService;
	}


	public void setAdministradorCreditosService(
			AdministradorCreditosService administradorCreditosService) {
		this.administradorCreditosService = administradorCreditosService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}


	@Override
	public Empleo findPreviousEmpleo(Empleo empleo){
		EmploymentQueryFilter empleoQueryFilter = new EmploymentQueryFilter();
		empleoQueryFilter.setCuil(empleo.getPerson().getCuil());
		empleoQueryFilter.setReparticionId(empleo.getCentroSector().getReparticion().getId().toString());
		
		empleoQueryFilter.setEstadosEmpleo( CollectionUtils.arrayToList(EmploymentStatus.values()));
		empleoQueryFilter.setFechaFin(empleo.getFechaInicio());

		return this.empleoDao.findPreviousEmpleo(empleoQueryFilter);
	}
	
	
	public List<Category> getAvailableCategoriesForPromotion(Empleo employment){
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

package org.dpi.departmentCreditsEntry;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.EmploymentCreditsEntriesServiceImpl;
import org.dpi.employment.EmploymentService;
import org.dpi.subDepartment.SubDepartmentService;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DepartmentCreditsEntryController {
    
    static Logger log = LoggerFactory.getLogger(DepartmentCreditsEntryController.class);
	
	private DepartmentCreditsEntryService creditsEntryService;
	
	private EmploymentService employmentService;

	@Resource(name = "creditsManagerService")
	private CreditsManagerService creditsManagerService;

	@Resource(name = "categoryService")
	private CategoryService categoryService;
	
	@Resource(name = "subDepartmentService")
	private SubDepartmentService subDepartmentService;
	
	
	@Resource(name = "creditsPeriodService")
	private CreditsPeriodService creditsPeriodService;
	
	
	@Autowired
	@Qualifier("customObjectMapper")
	private ObjectMapper objectMapper; 
	    
	       
	
	@InitBinder
	public void initBinder(WebDataBinder binder){
	    binder.setAutoGrowNestedPaths(false);
	}


	@Inject
	public DepartmentCreditsEntryController(DepartmentCreditsEntryService creditsEntryService) {
		this.creditsEntryService = creditsEntryService;
	}


	public CreditsManagerService getCreditsManagerService() {
		return creditsManagerService;
	}

	public void setCreditsManagerService(
			CreditsManagerService creditsManagerService) {
		this.creditsManagerService = creditsManagerService;
	}

	public EmploymentService getEmploymentService() {
		return employmentService;
	}


	public void setEmploymentService(EmploymentService employmentService) {
		this.employmentService = employmentService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}


	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}


	public SubDepartmentService getSubDepartmentService() {
		return subDepartmentService;
	}


	public void setSubDepartmentService(SubDepartmentService subDepartmentService) {
		this.subDepartmentService = subDepartmentService;
	}
	

	
	/*
    @RequestMapping(value = "/departments/creditsentries/processCambiarMultipleEstadoMovimiento", method = RequestMethod.POST)
    public String processCambiarMultipleEstadoMovimiento(@ModelAttribute("cambiosMultiplesEstadoMovimientosForm") CambiosMultiplesEstadoMovimientosForm cambiosMultiplesEstadoMovimientosForm) {

        List<DepartmentCreditsEntry> movimientos = cambiosMultiplesEstadoMovimientosForm.getMovimientos();
         
        for(DepartmentCreditsEntry submittedMovimiento :movimientos){
        	if(submittedMovimiento!=null){
        	    DepartmentCreditsEntryQueryFilter creditsEntryQueryFilter = new DepartmentCreditsEntryQueryFilter();
        		creditsEntryQueryFilter.setId(submittedMovimiento.getId());
        		List<DepartmentCreditsEntry> listCreditsEntry = creditsEntryService.find(creditsEntryQueryFilter);
        		DepartmentCreditsEntry creditsEntry = listCreditsEntry.get(0);
        		if(creditsEntry.getGrantedStatus()!=submittedMovimiento.getGrantedStatus()){
        			creditsEntryService.changeGrantedStatus(creditsEntry,submittedMovimiento.getGrantedStatus());
        		}
        		
        	}

        }
         
        return "redirect:/departments/department/showCreditEntries/"+creditsPeriodService.getCurrentCreditsPeriod().getName();

    }*/
    
    
    @RequestMapping(value = "/departmentcreditsentries/showCreditsEntries", method = RequestMethod.GET)
    public String showCreditsEntries(  HttpServletRequest request, 
            HttpServletResponse response,
            Model model) {

        
        Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("canAccountProposeNewEmployment", EmploymentCreditsEntriesServiceImpl.canAccountProposeNewEmployment(currentUser));
        
        return "departmentcreditsentries/listCreditsEntries";
    }
    
    

	
    

}
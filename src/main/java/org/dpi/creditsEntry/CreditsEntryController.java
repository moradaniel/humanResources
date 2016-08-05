package org.dpi.creditsEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dpi.category.CategoryService;
import org.dpi.common.ResponseMap;
import org.dpi.creditsEntry.CreditsEntry.GrantedStatus;
import org.dpi.creditsManagement.CreditsManagerService;
import org.dpi.creditsPeriod.CreditsPeriod;
import org.dpi.creditsPeriod.CreditsPeriodService;
import org.dpi.employment.EmploymentCreditsEntriesServiceImpl;
import org.dpi.employment.EmploymentQueryFilter;
import org.dpi.employment.EmploymentService;
import org.dpi.subDepartment.SubDepartmentService;
import org.dpi.util.PageList;
import org.janux.bus.security.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class CreditsEntryController {
    
    static Logger log = LoggerFactory.getLogger(CreditsEntryController.class);
	
	private CreditsEntryService creditsEntryService;
	
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
	public CreditsEntryController(CreditsEntryService creditsEntryService) {
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
	
	
	@RequestMapping(value = "/departments/creditsentries/{id}/setupFormCambiarEstadoCreditsEntry", method = RequestMethod.GET)
	public String setupFormCambiarEstadoCreditsEntry(@PathVariable Long id, Model model){  
			      
          
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		
		creditsEntryQueryFilter.addCreditsEntryIds(id);
		
		
		List<CreditsEntry> movimientos = creditsEntryService.find(creditsEntryQueryFilter);

		CreditsEntry creditsEntry = movimientos.get(0);
          
		CreditsEntryForm creditsEntryForm = new CreditsEntryForm(creditsEntry);
		
		
		model.addAttribute("creditsEntryForm", creditsEntryForm);
		model.addAttribute("grantedStatuses", GrantedStatus.values());

        
        return "creditsentries/cambioEstadoMovimientoForm";
    }
	
	
	@RequestMapping(value = "/creditsentries/cambiarEstadoMovimiento", method = RequestMethod.POST)
	public String processFormCambiarEstadoMovimiento(HttpServletRequest request,
													@ModelAttribute("creditsEntryForm") CreditsEntryForm creditsEntryForm,
													Model model,
													BindingResult result) throws Exception {
		

		//retrieve the creditsEntry from database
		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
		creditsEntryQueryFilter.addCreditsEntryIds(creditsEntryForm.getCreditsEntryId());
				
		List<CreditsEntry> movimientos = creditsEntryService.find(creditsEntryQueryFilter);
		CreditsEntry creditsEntry = movimientos.get(0);

          
		creditsEntryService.changeGrantedStatus(creditsEntry, creditsEntryForm.getGrantedStatus());  


        String creditsPeriodName =  creditsEntry.getCreditsPeriod().getName();
		return "redirect:/departments/department/showCreditEntries/"+creditsPeriodName;
    }  
	
	
    @RequestMapping(value = "/departments/creditsentries/processCambiarMultipleEstadoMovimiento", method = RequestMethod.POST)
    public String processCambiarMultipleEstadoMovimiento(@ModelAttribute("cambiosMultiplesEstadoMovimientosForm") CambiosMultiplesEstadoMovimientosForm cambiosMultiplesEstadoMovimientosForm) {

        List<CreditsEntry> movimientos = cambiosMultiplesEstadoMovimientosForm.getMovimientos();
         
        for(CreditsEntry submittedMovimiento :movimientos){
        	if(submittedMovimiento!=null){
        		CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        		creditsEntryQueryFilter.addCreditsEntryIds(submittedMovimiento.getId());
        		List<CreditsEntry> listCreditsEntry = creditsEntryService.find(creditsEntryQueryFilter);
        		CreditsEntry creditsEntry = listCreditsEntry.get(0);
        		if(creditsEntry.getGrantedStatus()!=submittedMovimiento.getGrantedStatus()){
        			creditsEntryService.changeGrantedStatus(creditsEntry,submittedMovimiento.getGrantedStatus());
        		}
        		
        	}

        }
         
        return "redirect:/departments/department/showCreditEntries/"+creditsPeriodService.getCurrentCreditsPeriod().getName();

    }
    
    
    @RequestMapping(value = "/creditsentries/showCreditsEntries", method = RequestMethod.GET)
    public String showCreditsEntries(  HttpServletRequest request, 
            HttpServletResponse response,
            Model model) {

        
        Account currentUser = (Account)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("canAccountProposeNewEmployment", EmploymentCreditsEntriesServiceImpl.canAccountProposeNewEmployment(currentUser));
        
        return "creditsentries/listCreditsEntries";
    }
    
    
    
    /**
     * Search creditsEntries
     * @param departmentId
     * @return
     * @throws JsonProcessingException 
     */

    @RequestMapping(value = "/rest/creditsentries/findCreditsEntries", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findCreditsEntries(/*@PathVariable String departmentId,*/

            //@RequestParam(value="apellidoNombre", required=false) String apellidoNombre,
            //@RequestParam(value="cuil", required=false) String cuil,
           // @RequestParam(value="employmentstatus", required=false) String employmentstatus,
            //@RequestParam(value="page", required=false) Integer page,//pageNumber requested
            @RequestParam(value="pageNumber", required=false, defaultValue = "1") Integer page,//pageNumber requested
            //@RequestParam(value="count", required=false) Integer pageSize,//number of rows requested (pagesize)
            @RequestParam(value="pageSize", required=false, defaultValue = "10") Integer pageSize,//number of rows requested (pagesize)
            //@RequestParam(value="filter[apellidoNombre]", required=false) String apellidoNombre,
            //@RequestParam(value="apellidoNombre", required=false) String apellidoNombre,
            @RequestParam(value="creditsPeriodName", required=false) String creditsPeriodName,
            @RequestParam(value="creditsEntryType", required=false) String creditsEntryType,
            
            //@RequestParam(value="filter[cuil]", required=false) String cuil,
            @RequestParam(value="cuil", required=false) String cuil,
            @RequestParam(value="sidx", required=false) String sidx,
            @RequestParam(value="sord", required=false) String sord

            //@RequestParam(value="page", required=false) Integer page,//pageNumber requested
            //@RequestParam(value="rows", required=false) Integer rows,//number of rows requested (pagesize)
            //@RequestParam(value="sidx", required=false) String sidx,
            //@RequestParam(value="sord", required=false) String sord) 

            ) throws JsonProcessingException {
        log.info("Started creditsEntries paginated search");       

        
        EmploymentQueryFilter employmentQueryFilter = new EmploymentQueryFilter();

        Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Account currentUser = (Account)accountObj;

        CreditsEntryQueryFilter creditsEntryQueryFilter = new CreditsEntryQueryFilter();
        
        creditsEntryQueryFilter.setPageSize(pageSize);
        creditsEntryQueryFilter.setPage(page-1);
        
        creditsEntryQueryFilter.setEmploymentQueryFilter(employmentQueryFilter);
        
        if(StringUtils.hasText(creditsEntryType)) {
            creditsEntryQueryFilter.addCreditsEntryType(CreditsEntryType.valueOf(creditsEntryType));
        }
        
        
        if(StringUtils.hasText(creditsPeriodName)) {
            creditsEntryQueryFilter.addCreditsPeriodName(creditsPeriodName);
        }

        PageList<CreditsEntry> creditsEntries = creditsEntryService.findCreditsEntries(creditsEntryQueryFilter);


                
        List<CreditsEntryVO> creditsEntryVOReparticion = creditsEntryService.buildCreditsEntryVO(creditsEntries,currentUser);
        

        long total = creditsEntries.getTotalItems();


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json;charset=utf-8");

        Map<String, Object> responseMap = new ResponseMap<CreditsEntryVO>().mapOK(creditsEntryVOReparticion,total);

        String serializedResponse = objectMapper.writeValueAsString(responseMap);


        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

    }
	

    /**
     * Search creditsEntries
     * @param departmentId
     * @return
     * @throws JsonProcessingException 
     */

    @RequestMapping(value = "/rest/creditsPeriods/findCreditsPeriods", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findCreditsPeriods(

            @RequestParam(value="pageNumber", required=false, defaultValue = "1") Integer page,//pageNumber requested
            @RequestParam(value="pageSize", required=false, defaultValue = "10") Integer pageSize,//number of rows requested (pagesize)
            @RequestParam(value="sidx", required=false) String sidx,
            @RequestParam(value="sord", required=false) String sord



            ) throws JsonProcessingException {
        log.info("Started creditsPeriods paginated search");       

        
        List<CreditsPeriod> creditsPeriods = creditsPeriodService.findAll();
        
        Map<String, Object> responseMap = new ResponseMap<CreditsPeriod>().mapOK(creditsPeriods);

        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        
        String serializedResponse = objectMapper.writeValueAsString(responseMap);

        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);
        
        
    }
    
    @RequestMapping(value = "/rest/creditsEntriesTypes/findCreditsEntriesTypes", method = RequestMethod.GET)
    public ResponseEntity<String> getAllCreditsEntriesTypes() throws JsonProcessingException {
        log.info("Start getAllCreditsEntriesTypes.");     


        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        List<CreditsEntryType> searcheableCreditsEntriesTypes = new ArrayList<CreditsEntryType>();

        searcheableCreditsEntriesTypes.add(CreditsEntryType.AscensoAgente);
        searcheableCreditsEntriesTypes.add(CreditsEntryType.BajaAgente);
        searcheableCreditsEntriesTypes.add(CreditsEntryType.IngresoAgente);
        searcheableCreditsEntriesTypes.add(CreditsEntryType.CargaInicialAgenteExistente);

        Map<String, Object> responseMap = new ResponseMap<CreditsEntryType>().mapOK(searcheableCreditsEntriesTypes);

        String serializedResponse = objectMapper.writeValueAsString(responseMap);

        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

    }
    
    

}
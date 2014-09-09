package org.dpi.department;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.dpi.common.ResponseMap;
import org.dpi.creditsEntry.CreditsEntryController;
import org.dpi.security.UserAccessService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class DepartmentSearchController {
    
    static Logger log = LoggerFactory.getLogger(CreditsEntryController.class);

	private DepartmentService departmentService;
	
	
	@Resource(name = "userAccessService")
	private UserAccessService userAccessService;

    @Autowired
    @Qualifier("customObjectMapper")
    private ObjectMapper objectMapper;

	

	@Inject
	public DepartmentSearchController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	
	public DepartmentService getReparticionService() {
		return departmentService;
	}
	
    /**
     * Search availableDepartmentsForAccount
     * @return
     * @throws JsonProcessingException 
     */

    @RequestMapping(value = "/rest/departments/findAvailableDepartmentsForAccount", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findAvailableDepartmentsForAccount(/*@PathVariable String departmentId,*/
            @RequestParam(value="pageNumber", required=false, defaultValue = "1") Integer page,//pageNumber requested

            @RequestParam(value="pageSize", required=false, defaultValue = "500") Integer pageSize//number of rows requested (pagesize)
            
            ) throws JsonProcessingException {
        log.info("Started creditsEntries paginated search");       
        
        
        
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String accountName = ((Account) principal).getName();
        Set<DepartmentAdminInfo> departments = userAccessService.getDepartmentListForAccount(accountName, null);
        

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Content-Type", "application/json;charset=utf-8");

        Map<String, Object> responseMap = new ResponseMap<DepartmentAdminInfo>().mapOK(new ArrayList<DepartmentAdminInfo>(departments),departments.size());

        String serializedResponse = objectMapper.writeValueAsString(responseMap);


        return new ResponseEntity<String>(serializedResponse, responseHeaders, HttpStatus.OK);

    }


}
package org.dpi.person;

import java.util.List;

import javax.inject.Inject;

import org.dpi.util.PageList;
import org.dpi.util.query.QueryBind;
import org.dpi.util.query.QueryBind.OrderDirection;
import org.dpi.web.AgenteMapper;
import org.dpi.web.JqgridFilter;
import org.dpi.web.JqgridObjectMapper;
import org.dpi.web.response.PersonDto;
import org.dpi.web.response.JqgridResponse;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PersonController {

	private PersonService personService;
	
	@Inject
	public PersonController(PersonService personService) {
		this.personService = personService;
	}


	@RequestMapping(value="/persons/search", produces="application/json")
	public @ResponseBody JqgridResponse<PersonDto> records(
    		@RequestParam("_search") Boolean search,
    		@RequestParam(value="filters", required=false) String filters,
    		@RequestParam(value="page", required=false) Integer page,//pageNumber requested
    		@RequestParam(value="rows", required=false) Integer rows,//number of rows requested (pagesize)
    		@RequestParam(value="sidx", required=false) String sidx,
    		@RequestParam(value="sord", required=false) String sord) {

		
		
		Integer startIndex = (page * rows)-rows;
		Integer endIndex = startIndex + rows - 1;
		
		QueryBind bind = new QueryBind();
		bind.setRange(startIndex, endIndex);
		
		OrderDirection orderDirection = OrderDirection.ASCENDING;
		if(StringUtils.hasText(sord) && sord.equalsIgnoreCase("desc")){
			orderDirection = OrderDirection.DESCENDING;
		}

		bind.addOrdering(sidx, orderDirection);
		

		
		return getFilteredRecords(filters, bind, page);


	}
	
	/**
	 * Helper method for returning filtered records
	 */
	public JqgridResponse<PersonDto> getFilteredRecords(String filters, QueryBind bind, Integer page) {
		String apellidoNombre = null;
		String cuil = null;

		if(filters!=null){
			JqgridFilter jqgridFilter = JqgridObjectMapper.map(filters);
			if(jqgridFilter!=null){
				for (JqgridFilter.Rule rule: jqgridFilter.getRules()) {
					if (rule.getField().equals("apellidoNombre"))
						apellidoNombre = rule.getData();
					else if (rule.getField().equals("cuil"))
						cuil = rule.getData();
				}
			}
		}	
		
		PersonQueryFilter agenteFilter = new PersonQueryFilter();
		
		agenteFilter.setApellidoNombre(apellidoNombre);
		agenteFilter.setCuil(cuil);
		
		PageList<Person> agentes = personService.findAgentes(bind, agenteFilter, false);
		
        
        if (page > agentes.getTotalPageCount()){ 
        	page=agentes.getTotalPageCount().intValue();
        }
        
		List<PersonDto> agenteDtos = AgenteMapper.map(agentes);
		JqgridResponse<PersonDto> response = new JqgridResponse<PersonDto>();
		response.setRows(agenteDtos);
		response.setRecords(agentes.getTotalItems().toString());
		response.setTotal(agentes.getTotalPageCount().toString());
		response.setPage(page.toString());
		return response;
	}
	
	
	/*

	@RequestMapping(value="/get", produces="application/json")
	public @ResponseBody UserDto get(@RequestBody UserDto user) {
		return UserMapper.map(repository.findByUsername(user.getUsername()));
	}

	@RequestMapping(value="/create", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse create(
			@RequestParam String username,
			@RequestParam String password,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam Integer role) {

		User newUser = new User(username, password, firstName, lastName, new Role(role));
		Boolean result = service.create(newUser);
		return new StatusResponse(result);
	}

	@RequestMapping(value="/update", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse update(
			@RequestParam String username,
			@RequestParam String firstName,
			@RequestParam String lastName,
			@RequestParam Integer role) {

		User existingUser = new User(username, firstName, lastName, new Role(role));
		Boolean result = service.update(existingUser);
		return new StatusResponse(result);
	}

	@RequestMapping(value="/delete", produces="application/json", method=RequestMethod.POST)
	public @ResponseBody StatusResponse delete(
			@RequestParam String username) {

		User existingUser = new User(username);
		Boolean result = service.delete(existingUser);
		return new StatusResponse(result);
	}
	
	*/
}


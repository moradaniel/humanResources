package org.dpi.department;

import javax.inject.Inject;

import org.springframework.samples.travel.SearchCriteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DepartmentSearchController {

	private DepartmentService departmentService;

	@Inject
	public DepartmentSearchController(DepartmentService departmentService) {
		this.departmentService = departmentService;
	}
	
	
	public DepartmentService getReparticionService() {
		return departmentService;
	}

	
	@RequestMapping(value = "/departments/main", method = RequestMethod.GET)
	public void main(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/departments/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria) {
	}

	/*@RequestMapping(value = "/departments", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account account = (Account) accountObj; // better be one of our Account objects!
		if (account.hasPermissions("Manage_Reparticiones", "READ")){
			List<Reparticion> departments = departmentService.findReparticiones(criteria);
			model.addAttribute("departmentList",departments);
			return "departments/list";
		}
		return "redirect:/home";

	}*/

}
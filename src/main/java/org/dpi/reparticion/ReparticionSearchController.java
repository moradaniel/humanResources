package org.dpi.reparticion;

import java.util.List;

import javax.inject.Inject;

import org.janux.bus.security.Account;
import org.springframework.samples.travel.SearchCriteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ReparticionSearchController {

	private ReparticionService reparticionService;

	@Inject
	public ReparticionSearchController(ReparticionService reparticionService) {
		this.reparticionService = reparticionService;
	}
	
	
	public ReparticionService getReparticionService() {
		return reparticionService;
	}

	
	@RequestMapping(value = "/reparticiones/main", method = RequestMethod.GET)
	public void main(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/reparticiones/search", method = RequestMethod.GET)
	public void search(SearchCriteria searchCriteria) {
	}

	@RequestMapping(value = "/reparticiones", method = RequestMethod.GET)
	public String list(SearchCriteria criteria, Model model) {

		Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Account account = (Account) accountObj; // better be one of our Account objects!
		if (account.hasPermissions("Manage_Reparticiones", "READ")){
			List<Reparticion> reparticiones = reparticionService.findReparticiones(criteria);
			model.addAttribute("reparticionList",reparticiones);
			return "reparticiones/list";
		}
		return "redirect:/home";

	}

}
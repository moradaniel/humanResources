package org.dpi.web;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.janux.bus.security.Account;
import org.janux.bus.security.AccountService;
import org.janux.bus.security.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {


	static Logger log = LoggerFactory.getLogger(HomeController.class);



	@Resource(name = "accountServiceGeneric")
	private AccountService accountService;


	public HomeController() {
	}




	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String homeByRol(HttpServletRequest request, HttpServletResponse response,/*@PathVariable Long reparticionId,*/ Model model) {

		Account januxUserDetailsAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		//AbstractUserSettings januxUserDetailsAccountSettings = settingsFactory.getSettingsForAccount(januxUserDetailsAccount);

		Account account = accountService.loadAccountByName(januxUserDetailsAccount.getName());

		for(Role role : account.getRoles()){
			if(role.getName().equals("RESPONSABLE_REPARTICION")){
				return "redirect:reparticiones/reparticion/showCreditos";
			}	

			if(role.getName().equals("SUPERVISOR_REPARTICIONES")){
				return "redirect:reparticiones/reparticion/showCreditos";
			}	

			if(role.getName().equals("SYS_ADMIN")){
				return "redirect:reparticiones/reparticion/showCreditos";

			}
		}

		return "reparticiones";
	}


}
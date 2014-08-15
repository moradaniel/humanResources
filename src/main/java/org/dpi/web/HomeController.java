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
	public String homeByRol(HttpServletRequest request, HttpServletResponse response, Model model) {

		Account januxUserDetailsAccount = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Account account = accountService.loadAccountByName(januxUserDetailsAccount.getName());

		for(Role role : account.getRoles()){
			if(role.getName().equals("DEPARTMENT_RESPONSIBLE")){
				return "redirect:departments/department/showCredits";
			}	

			if(role.getName().equals("DEPARTMENTS_SUPERVISOR")){
				return "redirect:departments/department/showCredits";
			}	
			
			if(role.getName().equals("SUBTREE_SUPERVISOR")){
				return "redirect:departments/department/showCredits";
			}
			
			if(role.getName().equals("HR_MANAGER")){
				return "redirect:departments/department/showEmployments";
			}

			if(role.getName().equals("SYS_ADMIN")){
				return "redirect:departments/department/showCredits";

			}
		}

		return "redirect:departments/department/showCredits";
	}


}
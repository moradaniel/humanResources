package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.security.AbstractUserSettings;
import org.dpi.security.UserSettingsFactory;
import org.janux.bus.security.Account;
import org.janux.bus.security.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 * This interceptor wraps all actions involving Reparticions; it performs the following actions:
 *
 * <ul>
 * 	<li>ensures that a Session exists</li>
 * 	<li>
 * 		ensures that a list of the Reparticions that the current Principal can edit has
 * 		been loaded into the session; this list should contain at least the Code
 * 		and Name of the Reparticion
 * 	</li>
 * </ul>
 * 
 ***************************************************************************************************
 */
public class AccountActionInterceptor extends HandlerInterceptorAdapter
{

	Log log = LogFactory.getLog(this.getClass());

	/** the default key for the account object in the model: "account" */
	public final static String DEFAULT_ACCOUNT_KEY = "account";
	public final static String DEFAULT_SETTINGS_KEY = "prefs";
	public final static String DEFAULT_ROLES_KEY = "roles";
	private UserSettingsFactory settingsFactory;	

	private String accountKey;
	private String settingsKey;
	private String rolesKey;

	/** 
	 * Returns the name of the key used to refer to the logged-in Account object
	 * in the view
	 */
	public String getAccountKey() {
		return (accountKey != null) ? accountKey : DEFAULT_ACCOUNT_KEY;
	}

	public void setAccountKey(String accountKey) {
		this.accountKey = accountKey;
	}

	public String getSettingsKey() {
		return (settingsKey != null) ? settingsKey : DEFAULT_SETTINGS_KEY;
	}

	public void setSettingsKey(String settingsKey) {
		this.settingsKey = settingsKey;
	}
	
	public String getRolesKey() {
		return (rolesKey != null) ? rolesKey : DEFAULT_ROLES_KEY;
	}

	public void setSettingsFactory(UserSettingsFactory settingsFactory)
	{
		this.settingsFactory = settingsFactory;
	}


	public AccountActionInterceptor()
	{

	}


	/** 
	 * update the reparticion and reparticion list
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		if(SecurityContextHolder.getContext().getAuthentication()!=null){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (!(principal instanceof Account))
			{
				return true; // still anonymous; nothing to do yet;
			}
			return true;
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if(SecurityContextHolder.getContext().getAuthentication()!=null){
			if(modelAndView!=null){//if null it was an Ajax request
				Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Account account = (Account) accountObj; // better be one of our Account objects!
				AbstractUserSettings settings = settingsFactory.getSettingsForAccount(account);
	
				modelAndView.addObject(getAccountKey(), account);
				modelAndView.addObject(getSettingsKey(), settings);
				
				List<String> rolesNames = new ArrayList<String>();
				for(Role role : account.getRoles()){
					rolesNames.add(role.getName());
				}
				
				modelAndView.addObject(getRolesKey(), StringUtils.collectionToCommaDelimitedString(rolesNames));
				
				if (log.isDebugEnabled())
				{
					log.debug("classname: " + account.getClass().getName());
					log.debug("account: " + account);
				}

			}
			//return model;

		}
	}


}



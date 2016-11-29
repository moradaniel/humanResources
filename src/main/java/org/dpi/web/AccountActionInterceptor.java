package org.dpi.web;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.department.Department;
import org.dpi.department.DepartmentController;
import org.dpi.security.AbstractUserSettings;
import org.dpi.security.UserSettingsFactory;
import org.janux.bus.security.Account;
import org.janux.bus.security.Role;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 * This interceptor wraps all actions involving departments; it performs the following actions:
 *
 * <ul>
 * 	<li>ensures that a Session exists</li>
 * 	<li>
 * 		ensures that a list of the departments that the current Principal can edit has
 * 		been loaded into the session; this list should contain at least the Code
 * 		and Name of the department
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
	 * update the department and department list
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		if(SecurityContextHolder.getContext().getAuthentication()!=null){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

			if (!(principal instanceof Account))
			{
				return true; // still anonymous; nothing to do yet;
			}else{
			    /*WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
			    
			    Department department = (Department)request.getAttribute(DepartmentController.KEY_DEPARTMENT);
			    StringBuffer stringBuffer = new StringBuffer();
			    stringBuffer.append("=====User:"+((Account)principal).getName()+" accessing from IP:"+ webAuthenticationDetails.getRemoteAddress());
			    if(department!=null) {
			        stringBuffer.append("__"+department.getCode());    
			    }
			    stringBuffer.append("__"+getURL(request));
			    //stringBuffer.append("User=====");
				log.info(stringBuffer.toString());*/
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
				
				//modelAndView.addObject(getRolesKey(), StringUtils.collectionToCommaDelimitedString(rolesNames));
				modelAndView.addObject(getRolesKey(), rolesNames);
				
				if (log.isDebugEnabled())
				{
					log.debug("classname: " + account.getClass().getName());
					log.debug("account: " + account);
				}

			}
			//return model;

			
			String urlRequested = getURL(request);
			if(urlRequested.indexOf("findAvailableDepartmentsForAccount") < 0 ){
    			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                WebAuthenticationDetails webAuthenticationDetails = (WebAuthenticationDetails)SecurityContextHolder.getContext().getAuthentication().getDetails();
                
                Department department = (Department)DepartmentController.getCurrentDepartment(request);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("=====User:"+((Account)principal).getName()+" accessing from IP:"+ webAuthenticationDetails.getRemoteAddress());
                if(department!=null) {
                    stringBuffer.append("__"+department.getCode());    
                }
                stringBuffer.append("__"+urlRequested);
                //stringBuffer.append("User=====");
                log.info(stringBuffer.toString());
			
			}
		}
	}
	
	private static String getURL(HttpServletRequest req) {

	    //String scheme = req.getScheme();             // http
	    //String serverName = req.getServerName();     // hostname.com
	    //int serverPort = req.getServerPort();        // 80
	    //String contextPath = req.getContextPath();   // /mywebapp
	    String servletPath = req.getServletPath();   // /servlet/MyServlet
	    String pathInfo = req.getPathInfo();         // /a/b;c=123
	    String queryString = req.getQueryString();          // d=789

	    // Reconstruct original requesting URL
	    StringBuffer url =  new StringBuffer();
	    /*url.append(scheme).append("://").append(serverName);

	    if ((serverPort != 80) && (serverPort != 443)) {
	        url.append(":").append(serverPort);
	    }

	    url.append(contextPath);*/
	    url.append(servletPath);

	    if (pathInfo != null) {
	        url.append(pathInfo);
	    }
	    if (queryString != null) {
	        url.append("?").append(queryString);
	    }
	    return url.toString();
	}


}



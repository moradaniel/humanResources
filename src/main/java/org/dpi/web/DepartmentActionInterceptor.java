package org.dpi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.department.DepartmentController;
import org.dpi.department.DepartmentService;
import org.dpi.security.UserAccessService;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 * This interceptor wraps all actions involving Departments; it performs the following actions:
 *
 * <ul>
 * 	<li>ensures that a Session exists</li>
 * 	<li>
 * 		ensures that a list of the Departments that the current Principal can edit has
 * 		been loaded into the session; this list should contain at least the Code
 * 		and Name of the Department
 * 	</li>
 * </ul>
 * 
 * @author  Daniel Mora
 ***************************************************************************************************
 */
public class DepartmentActionInterceptor extends HandlerInterceptorAdapter
{
	Log log = LogFactory.getLog(this.getClass());
	
	private DepartmentService departmentService;
	private UserAccessService accessService;


	/** instantiates an interceptor with a department accessor that it can use to perform its work */
	public DepartmentActionInterceptor(DepartmentService departmentService,UserAccessService accessService)
	{
		this.departmentService = departmentService;
		this.accessService = accessService;
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
			}

			/* TBD: How expensive would it be to just checkAccess all the time? If we don't, and an admin revokes a 
			 * user's access while they're logged in, they won't lose access immediately.
			 */
			boolean checkDepartmentAccess = true;

			DepartmentController.refreshRequestDepartment(request, departmentService, accessService, checkDepartmentAccess);
			//DepartmentController.refreshRequestDepartmentList(request, accessService);

			return true;
		}
		return true;
	}



}



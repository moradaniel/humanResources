package org.dpi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.reparticion.ReparticionController;
import org.dpi.reparticion.ReparticionService;
import org.dpi.security.UserAccessService;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 * This interceptor wraps all actions involving Reparticiones; it performs the following actions:
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
 * @author  Daniel Mora
 ***************************************************************************************************
 */
public class ReparticionActionInterceptor extends HandlerInterceptorAdapter
{
	Log log = LogFactory.getLog(this.getClass());
	
	private ReparticionService reparticionService;
	private UserAccessService accessService;


	/** instantiates an interceptor with a Reparticion accessor that it can use to perform its work */
	public ReparticionActionInterceptor(ReparticionService reparticionService,UserAccessService accessService)
	{
		this.reparticionService = reparticionService;
		this.accessService = accessService;
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

			/* TBD: How expensive would it be to just checkAccess all the time? If we don't, and an admin revokes a 
			 * user's access while they're logged in, they won't lose access immediately.
			 */
			boolean checkReparticionAccess = true;

			ReparticionController.refreshRequestReparticion(request, reparticionService, accessService, checkReparticionAccess);
			ReparticionController.refreshRequestReparticionList(request, accessService);

			return true;
		}
		return true;
	}



}



package org.dpi.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.locale.impl.DefaultLocaleResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 ***************************************************************************************************
 */
public class TilesLocaleInterceptor extends HandlerInterceptorAdapter
{
	Logger log = LoggerFactory.getLogger(this.getClass());
	



	/** instantiates an interceptor with a Hotel accessor that it can use to perform its work */
	public TilesLocaleInterceptor()
	{
		
	/*	this.hotelService = hotelService;
		this.accessService = accessService;*/
	}


	/** 
	 * update the hotel and hotel list
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
	{
		if(SecurityContextHolder.getContext()!=null){
		 if(SecurityContextHolder.getContext().getAuthentication()!=null){
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			/*if (!(principal instanceof Account))
			{
				return true; // still anonymous; nothing to do yet;
			}*/
	
			/* TBD: How expensive would it be to just checkAccess all the time? If we don't, and an admin revokes a 
			 * user's access while they're logged in, they won't lose access immediately.
			 */
			
			/*boolean checkHotelAccess = true;
			
			HotelInfoController.refreshRequestHotel(request, hotelService, accessService, checkHotelAccess);
			HotelInfoController.refreshRequestHotelList(request, accessService);
			*/
			
			if(request.getSession()!=null && request.getSession().getAttribute(DefaultLocaleResolver.LOCALE_KEY)==null){
				request.getSession().setAttribute(DefaultLocaleResolver.LOCALE_KEY, new Locale("es","AR"));
			}
			
		 }
		
		}
		
		return true;
	}

}



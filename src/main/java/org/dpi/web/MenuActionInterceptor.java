package org.dpi.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dpi.department.Department;
import org.dpi.department.DepartmentController;
import org.dpi.department.DepartmentService;
import org.janux.bus.security.Account;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


/**
 ***************************************************************************************************
 * 
 * @author  Daniel Mora
 ***************************************************************************************************
 */
public class MenuActionInterceptor extends HandlerInterceptorAdapter
{
    Log log = LogFactory.getLog(this.getClass());

    private DepartmentService departmentService;

    public final static String strShowHierarchicalAccumulatedCredits = "showHierarchicalAccumulatedCredits";

    public MenuActionInterceptor(DepartmentService departmentService/*,UserAccessService accessService*/)
    {
        this.departmentService = departmentService;
    }


    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {


        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            if(modelAndView!=null){//if null it was an Ajax request
                Object accountObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Account account = (Account) accountObj; // better be one of our Account objects!

                boolean showHierarchicalAccumulatedCredits = false;

                final Department department = DepartmentController.getCurrentDepartment(request);

                if(department != null) {

                    if(account.hasPermissions("Department_Info", "UPDATE") && 
                            (departmentService.isMinisterio(department)||departmentService.isPoderEjecutivo(department))) {
                        showHierarchicalAccumulatedCredits = true;
                    }

                }
                
                modelAndView.addObject(strShowHierarchicalAccumulatedCredits, showHierarchicalAccumulatedCredits);

            }

        }
    }



}



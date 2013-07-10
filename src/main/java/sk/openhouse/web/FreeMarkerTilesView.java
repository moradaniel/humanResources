 package sk.openhouse.web;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.samples.travel.PartialRenderingTilesView;
import org.springframework.web.servlet.support.RequestContext;

/**
 * This is the same as TilesView but have an option (by default true) to expose
 * spring macro helpers
 *
 * @author pete <p.reisinger@gmail.com>
 */
public class FreeMarkerTilesView extends PartialRenderingTilesView {

    public static final String SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE =
            "springMacroRequestContext";

    private boolean exposeSpringMacroHelpers = true;

    /**
     * @param exposeSpringMacroHelpers  indicates if spring macro helpers
     *                                  should be in velocity template, default
     *                                  is true
     */
    public void setExposeSpringMacroHelpers(boolean exposeSpringMacroHelpers) {
        this.exposeSpringMacroHelpers = exposeSpringMacroHelpers;
    }

    /**
     * @return  indicates if spring macro helpers should be in velocity
     *          template, default is true
     */
    public boolean getExposeSpringMacroHelpers() {
        return exposeSpringMacroHelpers;
    }

    @Override
    protected void renderMergedOutputModel(
            Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

    	//this.setRequestContextAttribute("requestContext");
        /* add spring macro helpers */
        if (this.exposeSpringMacroHelpers) {
            if (model.containsKey(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE)) {
                throw new ServletException(
                        "Cannot expose bind macro helper '" + SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE +
                        "' because of an existing model object of the same name");
            }
            // Expose RequestContext instance for Spring macros.
            model.put(SPRING_MACRO_REQUEST_CONTEXT_ATTRIBUTE,
                    new RequestContext(request, response, getServletContext(), model));
        }

        super.renderMergedOutputModel(model, request, response);
    }
}

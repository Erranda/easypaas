package com.withinet.opaas.wicket.html;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.wicket.services.UserSession;

import static com.withinet.opaas.controller.common.ServiceProperties.*;
/**
 * @author Folarin Omotoriogun
 */
public class ProjectIndex extends Authenticated
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1340763028068919773L;

	@SpringBean
	ProjectController projectController;
	
	@SpringBean
	InstanceController instanceController;
	
	@SpringBean
	Authorizer authorizer;
	
    /**
     * Constructor.
     */
    public ProjectIndex ()
    {
    	Long uid = UserSession.get().getUser().getID();
    	
    	try {
    		authorizer.authorize(READ_PROJECT, uid);
    		ProjectTableWidget projectTableWidget = new ProjectTableWidget ("project-table-widget", true);
    		add (projectTableWidget);
    	} catch (ControllerSecurityException e) {
    		ProjectTableWidget projectTableWidget = new ProjectTableWidget ("project-table-widget", false);
    		add (projectTableWidget);
    	}
       
	    try {
	    	authorizer.authorize(CREATE_PROJECT, uid);
    		ProjectSetupWidget projectSetupWidget = new ProjectSetupWidget ("project-setup-widget", true);
    		add (projectSetupWidget);
		} catch (ControllerSecurityException e) {
			ProjectSetupWidget projectSetupWidget = new ProjectSetupWidget ("project-setup-widget", false);
    		add (projectSetupWidget);
		}
    }
    
    public ProjectIndex (PageParameters pageParameters) {
    	if (!pageParameters.get("action").isNull()) {
    		if (pageParameters.get("action").equals("start") &&
    				pageParameters.get("pid").isNull()) {
    		}
    	}
    }
}
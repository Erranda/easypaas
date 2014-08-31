/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.wicket.services.UserSession;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

/**
 * @author Folarin Omotoriogun
 *
 */
public class ProjectTableQuickAction extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7296329391239609079L;
	@SpringBean
	Authorizer authorizer;
	
	public ProjectTableQuickAction(String id, WebMarkupContainer startInstanceLink, 
			WebMarkupContainer deleteProject,
			WebMarkupContainer viewBundles, WebMarkupContainer viewInstances) {
		super(id);
		Long uid = UserSession.get().getUser().getID();
		try {
    		authorizer.authorize(CREATE_INSTANCE, uid);
    	} catch (ControllerSecurityException e) {
    		startInstanceLink.setVisible(false);
    	}
		add (startInstanceLink);
		
		try {
    		authorizer.authorize(DELETE_PROJECT, uid);
    	} catch (ControllerSecurityException e) {
    		deleteProject.setVisible(false);
    	}
		add (deleteProject);
		
		try {
    		authorizer.authorize(READ_PROJECT, uid);
    	} catch (ControllerSecurityException e) {
    		viewBundles.setVisible(false);
    	}
		add (viewBundles);
		
		try {
    		authorizer.authorize(READ_PROJECT, uid);
    	} catch (ControllerSecurityException e) {
    		viewBundles.setVisible(false);
    	}
		add (viewInstances);
	}

}

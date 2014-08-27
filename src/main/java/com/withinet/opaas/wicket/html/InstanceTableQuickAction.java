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
 * @author Folarin
 *
 */
public class InstanceTableQuickAction extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2469990252457612242L;
	
	@SpringBean
	Authorizer authorizer;
	
	public InstanceTableQuickAction(String id, WebMarkupContainer stopInstanceLink, WebMarkupContainer flushInstanceLink, 
			WebMarkupContainer startInstanceLink, WebMarkupContainer cpanelLink, WebMarkupContainer viewLogLink) {
		super(id);
		Long uid = UserSession.get().getUser().getID();
		try {
    		authorizer.authorize(STOP_INSTANCE, uid);
    	} catch (ControllerSecurityException e) {
    		stopInstanceLink.setVisible(false);
    	}
		add (stopInstanceLink);
		try {
    		authorizer.authorize(DELETE_INSTANCE, uid);
    	} catch (ControllerSecurityException e) {
    		flushInstanceLink.setVisible(false);
    		System.out.print(false);
    		try {
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    		
    	}
		add (flushInstanceLink);
		try {
    		authorizer.authorize(START_INSTANCE, uid);
    	} catch (ControllerSecurityException e) {
    		startInstanceLink.setVisible(false);
    	}
		add (startInstanceLink);
		
		try {
    		authorizer.authorize(ACCESS_INSTANCE_CONSOLE, uid);
    	} catch (ControllerSecurityException e) {
    		cpanelLink.setVisible(false);
    	}
		add (cpanelLink);
		
		try {
    		authorizer.authorize(ACCESS_INSTANCE_LOG, uid);
    	} catch (ControllerSecurityException e) {
    		viewLogLink.setVisible(false);
    	}
		add (viewLogLink);
		add (new WebMarkupContainer ("divider").setVisibilityAllowed(flushInstanceLink.isVisible()));
	}

}

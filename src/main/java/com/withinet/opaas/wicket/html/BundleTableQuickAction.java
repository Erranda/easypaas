/**
 * 
 */
package com.withinet.opaas.wicket.html;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public class BundleTableQuickAction extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8155156904127650558L;
	
	@SpringBean
	private Authorizer authorizer;
	
	public BundleTableQuickAction(String id, WebMarkupContainer deleteBundleLink, WebMarkupContainer updateBundleLink) {
		super(id);
		Long uid = UserSession.get().getUser().getID();
		try {
    		authorizer.authorize(UPDATE_BUNDLE, uid);
		} catch (ControllerSecurityException e) {
			updateBundleLink.setVisible(false);
		} 
		add (updateBundleLink);
		try {
    		authorizer.authorize(DELETE_BUNDLE, uid);
		} catch (ControllerSecurityException e) {
			deleteBundleLink.setVisible(false);
		} 
		add (deleteBundleLink);
	}	
}

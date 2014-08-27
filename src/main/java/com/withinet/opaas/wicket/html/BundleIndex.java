package com.withinet.opaas.wicket.html;


import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import static com.withinet.opaas.controller.common.ServiceProperties.*;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Martijn Dashorst
 */
public class BundleIndex extends Authenticated
{
	@SpringBean
	BundleController bundleController;
	
	@SpringBean
	Authorizer authorizer;

    /**
     * Constructor.
     */
    public BundleIndex ()
    {
    	Long uid = UserSession.get().getUser().getID();
    	try {
    		authorizer.authorize(READ_BUNDLE, uid);
    		add (new BundleTableWidget ("bundle-table-widget", true));
		} catch (ControllerSecurityException e) {
			add (new BundleTableWidget ("bundle-table-widget", false));
		} 
    	
    	try {
    		authorizer.authorize(CREATE_BUNDLE, uid);
			add (new BundleSetupWidget ("bundle-setup-widget", true));
		} catch (ControllerSecurityException e) {
			add (new BundleSetupWidget ("bundle-setup-widget", false));
		} 
    }
    
    public BundleIndex (PageParameters pageParameters) {
    	if (!pageParameters.get("pid").isNull()) {
    		Long pid = pageParameters.get("pid").toLong();
    		Long uid = UserSession.get().getUser().getID();
    		try {
        		authorizer.authorize(READ_BUNDLE, uid);
        		add (new BundleTableWidget ("bundle-table-widget", pid, true));
    		} catch (ControllerSecurityException e) {
    			add (new BundleTableWidget ("bundle-table-widget", pid, false));
    		} 
    		try {
        		authorizer.authorize(CREATE_BUNDLE, uid);
    			add (new BundleSetupWidget ("bundle-setup-widget", true));
    		} catch (ControllerSecurityException e) {
    			add (new BundleSetupWidget ("bundle-setup-widget", false));
    		} 
    	}    	
    }
    
    @Override
    public void onInitialize () {
    	super.onInitialize();
    }
}
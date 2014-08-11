package com.withinet.opaas.wicket.html;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Martijn Dashorst
 */
public class BundleIndex extends Authenticated
{
	@SpringBean
	BundleController bundleController;
	
	Long userId = UserSession.get().getUser().getID();
    /**
     * Constructor.
     */
    public BundleIndex ()
    {
    	add (new BundleTableWidget ("bundle-table-widget"));
    }
    
    public BundleIndex (PageParameters pageParameters) {
    	if (!pageParameters.get("pid").isNull()) {
    		Long pid = pageParameters.get("pid").toLong();
    		add (new BundleTableWidget ("bundle-table-widget", pid));
    	}
    	if (!pageParameters.get("action").isNull() && !pageParameters.get("bid").isNull()) {
    		String val = pageParameters.get("action").toString();
    		try {
    			Long bid = pageParameters.get("bid").toLong();
    			val = val.toLowerCase().trim();
    			if (val.equals("delete")) {
    				bundleController.deleteBundle(bid, userId);
    				info ("Bundle deleted from your cache");
    			}
    			add (new BundleTableWidget ("bundle-table-widget"));
    		} catch (RuntimeException e) {
    			e.printStackTrace();
    			error (e.getMessage());
    		} catch (BundleControllerException e) {
				error (e.getMessage());
			}
    	}
    	
    }
    
    @Override
    public void onInitialize () {
    	super.onInitialize();
    }
}
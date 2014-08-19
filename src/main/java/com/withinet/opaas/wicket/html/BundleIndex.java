package com.withinet.opaas.wicket.html;

import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
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
    	try {
			add (new BundleSetupWidget ("bundle-setup-widget"));
		} catch (UserControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BundleControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProjectControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public BundleIndex (PageParameters pageParameters) {
    	if (!pageParameters.get("pid").isNull()) {
    		Long pid = pageParameters.get("pid").toLong();
    		add (new BundleTableWidget ("bundle-table-widget", pid));
    		try {
				add (new BundleSetupWidget ("bundle-setup-widget"));
			} catch (UserControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BundleControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ProjectControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}    	
    }
    
    @Override
    public void onInitialize () {
    	super.onInitialize();
    }
}
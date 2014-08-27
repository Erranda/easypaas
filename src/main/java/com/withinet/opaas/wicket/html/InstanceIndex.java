package com.withinet.opaas.wicket.html;





import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.common.ControllerSecurityException;

import com.withinet.opaas.wicket.services.UserSession;
import static com.withinet.opaas.controller.common.ServiceProperties.*;


/**
 * @author Folarin Omotoriogun
 */
public class InstanceIndex extends Authenticated
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7252358786436224331L;

	@SpringBean
	InstanceController instanceController;
	
	@SpringBean
	Authorizer authorizer;
	
    /**
     * Constructor.
     */
    public InstanceIndex ()
    {
    	Long uid = UserSession.get().getUser().getID();
    	try {
    		authorizer.authorize(SYSTEM_ADMIN, uid);
    		add (new InstanceSetupWidget ("instance-setup-widget", true));
		} catch (ControllerSecurityException e) {
			add (new InstanceSetupWidget ("instance-setup-widget", false));
		} 
    	
    	try {
    		authorizer.authorize(READ_INSTANCE, uid);
    		add (new InstanceTableWidget ("instance-table-widget", true));
		} catch (ControllerSecurityException e) {
			add (new InstanceTableWidget ("instance-table-widget", false));
		} 
    }
    
    public InstanceIndex (PageParameters pageParameters) {
    	Long uid = UserSession.get().getUser().getID();
    	if (!pageParameters.get("pid").isNull()) {
    		Long pid = pageParameters.get("pid").toLong();
    		try {
        		authorizer.authorize(READ_INSTANCE, uid);
        		add (new InstanceTableWidget ("instance-table-widget", pid, true));
    		} catch (ControllerSecurityException e) {
    			add (new InstanceTableWidget ("instance-table-widget", pid, false));
    		} 
    	}
    	
    	try {
    		authorizer.authorize(SYSTEM_ADMIN, uid);
    		add (new InstanceSetupWidget ("instance-setup-widget", true));
		} catch (ControllerSecurityException e) {
			add (new InstanceSetupWidget ("instance-setup-widget", false));
		} 
    }
}
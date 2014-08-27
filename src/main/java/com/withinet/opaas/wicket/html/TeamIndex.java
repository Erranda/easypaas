package com.withinet.opaas.wicket.html;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin Omotoriogun
 */
public class TeamIndex extends Authenticated
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3967639349012716065L;
	
	@SpringBean
	Authorizer authorizer;
	
	/**
     * Constructor.
     */
    public TeamIndex ()
    {
    	Long uid = UserSession.get().getUser().getID();
  
    	try {
    		authorizer.authorize(SYSTEM_ADMIN, uid);
    		TeamTableWidget table = new TeamTableWidget ("team-table-widget", true);
        	table.setOutputMarkupId(true);
            add (table);
            add (new TeamAddMemberSectionWidget ("team-add-member-section", table, true));
            add (new RoleTableWidget ("role-section", true));
    	} catch (ControllerSecurityException e) {
    		TeamTableWidget table = new TeamTableWidget ("team-table-widget", false);
        	table.setOutputMarkupId(true);
            add (table);
            add (new TeamAddMemberSectionWidget ("team-add-member-section", table, false));
            add (new RoleTableWidget ("role-section", false));
    	}	
    	
    }
}
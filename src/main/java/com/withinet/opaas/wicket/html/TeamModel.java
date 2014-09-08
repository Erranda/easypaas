/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.Collections;
import java.util.List;

import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public class TeamModel extends LoadableDetachableModel<List<User>> {
	
	private Long uid;
	
	@SpringBean
	Authorizer authorizer;
	
	@SpringBean
	private UserController userController;
	
	public TeamModel (Long id) {
		if (id == null)
			throw new IllegalArgumentException ();
		uid = id;
	}
	
	@Override
    public int hashCode()
    {
		return Long.valueOf(uid).hashCode();
    }
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2591092779258557836L;

	@Override
	protected List<User> load() {
			try {
				authorizer.authorize(ServiceProperties.SUPER_ADMIN, uid);
				return userController.listAllUsers(uid);
			} catch (ControllerSecurityException e) {
				try {
					return userController.listTeamMembers(uid, uid);
				} catch (UserControllerException e1) {
					e1.printStackTrace();
				}
			}
			return Collections.emptyList();
	}

}

/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.domain.UserRole;

/**
 * @author Folarin
 *
 */
@RestController
public class RoleControllerImpl implements RoleController {

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#createUserRole(com.withinet.opaas.domain.UserRole)
	 */
	public UserRole createUserRole(UserRole userRole)
			throws RoleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#deleteUserRole(java.lang.Long)
	 */
	public void deleteUserRole(Long id) throws RoleControllerException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#updateUserRole(com.withinet.opaas.domain.UserRole)
	 */
	public UserRole updateUserRole(UserRole userRole)
			throws RoleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#readUserRole(java.lang.Long)
	 */
	public UserRole readUserRole(Long id) throws RoleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#listRolesByUser(java.lang.Long)
	 */
	public List<UserRole> listRolesByUser(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

}

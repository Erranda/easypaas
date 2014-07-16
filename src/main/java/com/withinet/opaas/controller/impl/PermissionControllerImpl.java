/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.PermissionController;
import com.withinet.opaas.controller.common.PermissionControllerException;
import com.withinet.opaas.domain.UserPermission;

/**
 * @author Folarin
 *
 */
@RestController
public class PermissionControllerImpl implements PermissionController {

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.PermissionController#createUserPermission(com.withinet.opaas.domain.UserPermission)
	 */
	public UserPermission createUserPermission(UserPermission userPermission)
			throws PermissionControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.PermissionController#deleteUserPermission(java.lang.Long)
	 */
	public void deleteUserPermission(Long id)
			throws PermissionControllerException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.PermissionController#updateUserPermission(com.withinet.opaas.domain.UserPermission)
	 */
	public UserPermission updateUserPermission(UserPermission userPermission)
			throws PermissionControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.PermissionController#readUserPermission(java.lang.Long)
	 */
	public UserPermission readUserPermission(Long id)
			throws PermissionControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.PermissionController#listUserPermissionsByRole(java.lang.Long)
	 */
	public List<UserPermission> listUserPermissionsByRole(Long roleId) {
		// TODO Auto-generated method stub
		return null;
	}

}

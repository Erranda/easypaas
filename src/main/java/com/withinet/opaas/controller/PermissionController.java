/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.UserPermissionControllerException;
import com.withinet.opaas.domain.UserPermission;

/**
 * @author Folarin
 *
 */
@RestController
public interface PermissionController {
	
	public UserPermission createUserPermission (UserPermission userPermission) throws UserPermissionControllerException;
	
	public void deleteUserPermission (Long id) throws UserPermissionControllerException;
	
	public UserPermission updateUserPermission (UserPermission userPermission) throws UserPermissionControllerException;
	
	public UserPermission readUserPermission (Long id) throws UserPermissionControllerException;
	
	public List<UserPermission> listUserPermissionsByRole (Long roleId);
	
}

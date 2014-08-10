/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.PermissionControllerException;
import com.withinet.opaas.model.domain.UserPermission;

/**
 * @author Folarin
 *
 */
@RestController
public interface PermissionController {
	
	public UserPermission createUserPermission (UserPermission userPermission, Long requesterId) throws PermissionControllerException;
	
	public void deleteUserPermission (Long id, Long requesterId) throws PermissionControllerException;
	
	public UserPermission updateUserPermission (UserPermission userPermission, Long id, Long requesterId) throws PermissionControllerException;
	
	public UserPermission readUserPermission (Long id, Long requesterId) throws PermissionControllerException;
	
	public List<UserPermission> listUserPermissionsByRole (Long roleId, Long requesterId);
	
}

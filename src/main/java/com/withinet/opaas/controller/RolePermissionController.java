package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.RolePermissionControllerException;
import com.withinet.opaas.domain.RolePermission;
import com.withinet.opaas.domain.UserRole;

/**
 * @author Folarin
 *
 */
@RestController
public interface RolePermissionController {
	
	public RolePermission createRolePermission (RolePermission rolePermMap) throws RolePermissionControllerException;
	
	public void deleteRolePermission (RolePermission rolePermMap) throws RolePermissionControllerException;
	
	public List<RolePermission> listRolePermissionsByProject (UserRole role);
	
}

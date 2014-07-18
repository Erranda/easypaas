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
	
	public RolePermission createRolePermission (RolePermission rolePermMap, Long requesterId) throws RolePermissionControllerException;
	
	public void deleteRolePermission (RolePermission rolePermMap, Long requesterId) throws RolePermissionControllerException;
	
	public List<RolePermission> listRolePermissionsByRole (Long roleId, Long requesterId);
	
}

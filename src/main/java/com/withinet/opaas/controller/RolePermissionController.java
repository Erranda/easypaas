package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.RolePermissionControllerException;
import com.withinet.opaas.model.domain.RolePermission;
import com.withinet.opaas.model.domain.UserRole;

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

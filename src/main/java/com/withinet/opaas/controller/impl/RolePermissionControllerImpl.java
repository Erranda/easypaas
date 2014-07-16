package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.RolePermissionController;
import com.withinet.opaas.controller.common.RolePermissionControllerException;
import com.withinet.opaas.domain.RolePermission;
import com.withinet.opaas.domain.UserRole;

@RestController
public class RolePermissionControllerImpl implements RolePermissionController {

	public RolePermission createRolePermission(RolePermission rolePermMap)
			throws RolePermissionControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteRolePermission(RolePermission rolePermMap)
			throws RolePermissionControllerException {
		// TODO Auto-generated method stub

	}

	public List<RolePermission> listRolePermissionsByProject(UserRole role) {
		// TODO Auto-generated method stub
		return null;
	}

}

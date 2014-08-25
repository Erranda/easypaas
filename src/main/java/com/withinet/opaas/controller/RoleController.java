/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.model.domain.Permission;
import com.withinet.opaas.model.domain.Role;

/**
 * @author Folarin
 *
 */
@RestController
public interface RoleController {
	
	public Role createRole (Role userRole, Long requesterId) throws RoleControllerException;
	
	public void deleteRole (Long id, Long requesterId) throws RoleControllerException;
	
	public Role updateRole (Role userRole, Long id, Long requesterId) throws RoleControllerException;
	
	public Role readRole (Long id, Long requesterId) throws RoleControllerException;
	
	public List<Role> readRolesByOwner (Long requesterId) throws RoleControllerException;
	
	public Role addPermission (Long id, List<Permission> permissions, Long requesterId) throws RoleControllerException ;
	
	public Role addPermission (Long id, Permission permission, Long requesterId) throws RoleControllerException;
	
	public void removePermission (Long id, Long pid, Long requesterId) throws RoleControllerException;
	
	public List<Permission> readRolePermissions (Long id, Long requesterId) throws RoleControllerException;
	
}

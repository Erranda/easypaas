/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.PermissionRepository;
import com.withinet.opaas.model.RolePermissionRepository;
import com.withinet.opaas.model.RoleRepository;
import com.withinet.opaas.model.domain.Permission;
import com.withinet.opaas.model.domain.Role;
import com.withinet.opaas.model.domain.RolePermission;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class RoleControllerImpl implements RoleController {
	
	private Authorizer authorizer;
	
	private RoleRepository roleRepo;
	
	private UserController userCtrl;
	
	private RolePermissionRepository rpRepo;
	
	private PermissionRepository permRepo;
	
	@Autowired
	public void setPermissionRepository (PermissionRepository permRepo) {
		this.permRepo = permRepo;
	}
	
	@Autowired
	public void setRolePermissionRepository (RolePermissionRepository rp) {
		this.rpRepo = rp;
	}
	
	@Autowired
	public void setUserController (UserController userCtrl) {
		this.userCtrl = userCtrl;
	}
	
	@Autowired
	public void setRoleRepository (RoleRepository roleRepo) {
		this.roleRepo = roleRepo;
	}
	
	@Autowired
	public void setAuthorizer (Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#createRole(com.withinet.opaas.model.domain.Role, java.lang.Long)
	 */
	@Override
	public Role createRole(Role userRole, Long requesterId)
			throws RoleControllerException {
		Validation.assertNotNull(userRole);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		DomainConstraintValidator<Role> dcv = new  DomainConstraintValidator<Role> ();
		if (!dcv.isValid(userRole)) throw new IllegalArgumentException ("Bad request");
		User user;
		try {
			user = userCtrl.readAccount(requesterId, requesterId);
			Role role = roleRepo.findByOwnerAndName(user, userRole.getName());
			if (role != null)
				throw new RoleControllerException ("A role with this name already exists");
		} catch (UserControllerException e) {
			e.printStackTrace();
			throw new RoleControllerException (e.getMessage());
		}
		return roleRepo.saveAndFlush(userRole);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#deleteRole(java.lang.Long, java.lang.Long)
	 */
	@Override
	public void deleteRole(Long id, Long requesterId)
			throws RoleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		roleRepo.delete(role.getId());
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#updateRole(com.withinet.opaas.model.domain.Role, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Role updateRole(Role userRole, Long id, Long requesterId)
			throws RoleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#readRole(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Role readRole(Long id, Long requesterId)
			throws RoleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		return getRoleWithAuth (id, requesterId);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#addPermission(java.lang.Long, java.util.List, java.lang.Long)
	 */
	@Override
	public Role addPermission(Long id, List<Permission> permissions,
			Long requesterId) throws RoleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		if (role.getOwner().getID () != requesterId)
			throw new RoleControllerException ("You are not allowed to perform this action");
		for (Permission permission : permissions) {
			RolePermission rp = rpRepo.findByRoleAndPermission(role, permission);
			if (rp == null) {
				rpRepo.saveAndFlush(new RolePermission (user.getFullName(), role, permission));
			}
		}
		return role;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#addPermission(java.lang.Long, com.withinet.opaas.model.domain.Permission, java.lang.Long)
	 */
	@Override
	public Role addPermission(Long id, Permission permission, Long requesterId) throws RoleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		if (role.getOwner().getID () != requesterId)
			throw new RoleControllerException ("You are not allowed to perform this action");
		RolePermission rp = rpRepo.findByRoleAndPermission(role, permission);
			if (rp == null) {
				rpRepo.saveAndFlush(new RolePermission (user.getFullName(), role, permission));
			}
		return role;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.RoleController#removePermission(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public void removePermission(Long id, Long pid, Long requesterId) throws RoleControllerException  {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		Permission perm = permRepo.findOne(pid);
		RolePermission rolePerm = rpRepo.findByRoleAndPermission(role, perm);
		rpRepo.delete(rolePerm);
	}
	
	private Role getRoleWithAuth (Long id, Long uid) {
		Validation.assertNotNull(id);
		Validation.assertNotNull(uid);
		Role role = roleRepo.findOne(id);
		if (role.getOwner().getID() != uid)
			throw new ControllerSecurityException ("This action is not allowed");
		return role;
	}

	@Override
	public List<Role> readRolesByOwner(Long requesterId)
			throws RoleControllerException {
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("admin"), requesterId);
		return roleRepo.findByOwner(user);
	}
	
	@Override
	public List<Permission> readRolePermissions (Long id, Long requesterId) throws RoleControllerException  {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("admin"), requesterId);
		Role role = getRoleWithAuth (id, requesterId);
		List<Permission> perms = new ArrayList<Permission> ();
		for (RolePermission rp : rpRepo.findByRole(role)) {
			perms.add(rp.getPermission());
		}
		return perms;
	}
}
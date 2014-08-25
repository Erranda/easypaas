/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.RolePermission;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 * 
 */
@Service
public class Authorizer implements com.withinet.opaas.controller.Authorizer {

	@Autowired
	UserRepository userRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.withinet.opaas.controller.Authorizer#authorize(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public User authorize(String expPermission, Long uid) {
		boolean authd = false;
		User user = userRepo.findOne(uid);
		for (RolePermission rp : user.getAssignedRole().getRolePermissions()) {
			if (rp.getPermission().getValue().equals(expPermission)) {
				authd = true;
			}
		}
		if (authd == false)
			throw new ControllerSecurityException(
					"You are not allowed to perform this action");
		return user;
	}

	@Override
	public User authorize(List<String> expectedPermissions, Long uid) {
		boolean authd = false;
		User user = userRepo.findOne(uid);
		for (RolePermission rp : user.getAssignedRole().getRolePermissions()) {
			for (String permission : expectedPermissions) {
				if (rp.getPermission().getValue().equals(permission)) {
					authd = true;
					break;
				}
			}
			if (authd == true)
				break;
		}
		
		if (authd == false)
			throw new ControllerSecurityException(
					"You are not allowed to perform this action");
		return user;
	}

}

/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.domain.UserRole;

/**
 * @author Folarin
 *
 */
@RestController
public interface RoleController {
	
	public UserRole createUserRole (UserRole userRole, Long requesterId) throws RoleControllerException;
	
	public void deleteUserRole (Long id, Long requesterId) throws RoleControllerException;
	
	public UserRole updateUserRole (UserRole userRole, Long id, Long requesterId) throws RoleControllerException;
	
	public UserRole readUserRole (Long id, Long requesterId) throws RoleControllerException;
	
	public List<UserRole> listRolesByUser (Long id, Long requesterId);
	
}

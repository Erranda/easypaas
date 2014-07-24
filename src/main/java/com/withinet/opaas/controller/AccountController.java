/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.domain.User;


/**
 * @author Folarin
 *
 */

public interface AccountController {
	
	public User createAccount (User account) throws AccountControllerException;
	
	public void deleteAccount (Long id, Long requesterId) throws AccountControllerException;
	
	public User updateAccount (User account, Long id, Long requesterId) throws AccountControllerException;
	
	public User readAccount (Long id, Long requesterId) throws AccountControllerException;
	
	public List<User> listCollaborators (Long id, Long requesterId) throws AccountControllerException;
	
	public List<User> addTeamMembers (List<User> teamMembers, Long id, Long requesterId) throws AccountControllerException;
	
	public List<User> addTeamMember (User teamMember, Long id, Long requesterId) throws AccountControllerException;
	//Web access only
	public User login (String userId, String password) throws AccountLoginException;
	
}

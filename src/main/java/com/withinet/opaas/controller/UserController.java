/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.User;


/**
 * @author Folarin
 *
 */

public interface UserController {
	
	public User createAccount (User account) throws UserControllerException;
	
	public void deleteAccount (Long id, Long requesterId) throws UserControllerException;
	
	public User updateAccount (User account, Long id, Long requesterId) throws UserControllerException;
	
	public User readAccount (Long id, Long requesterId) throws UserControllerException;
	
	public List<User> listTeamMembers (Long userId, Long requesterId) throws UserControllerException;
	
	public void addTeamMember (User collaborator, Long id, Long requesterId) throws UserControllerException;
	
	public List<User> addTeamMemberFromExcel (String fileName, Long requesterId) throws UserControllerException;
	
	//Web access only
	public User login (String userId, String password) throws AccountLoginException;

	public void resetAccount(Long id, Long requesterId) throws UserControllerException;
	
	public void passwordReset (Long id, Long requesterId) throws UserControllerException;
}

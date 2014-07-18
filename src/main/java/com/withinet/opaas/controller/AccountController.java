/**
 * 
 */
package com.withinet.opaas.controller;

import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.domain.User;


/**
 * @author Folarin
 *
 */

public interface AccountController {
	
	public User createAccount (User account) throws AccountControllerException;
	
	public boolean deleteAccount (Long id, Long requesterId) throws AccountControllerException;
	
	public User updateAccount (User account, Long id, Long requesterId) throws AccountControllerException;
	
	public User readAccount (Long id, Long requesterId) throws AccountControllerException;
	
	//Web access only
	public User login (User user) throws AccountLoginException;
	
}

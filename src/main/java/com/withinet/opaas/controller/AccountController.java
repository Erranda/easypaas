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
	
	public boolean deleteAccount (User account) throws AccountControllerException;
	
	public User updateAccount (User account) throws AccountControllerException;
	
	public User readAccount (User account) throws AccountControllerException;
	
	public User login (User user) throws AccountLoginException;
	
}

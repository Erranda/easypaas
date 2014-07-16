package com.withinet.opaas.controller.impl;

import javax.security.auth.login.AccountException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.model.UserRepository;

/**
 * @author Folarin
 *
 */
@RestController
public class AccountControllerImpl implements AccountController {
	
	@Autowired
	ServiceProperties serviceProperties;
	
	@Autowired
	UserRepository userRepository;
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.AccountController#createAccount(com.withinet.opaas.domain.User)
	 */
	public User createAccount(@Valid User account) throws AccountControllerException {
		//Check if user already exists
		User empty = userRepository.findByEmail(account.getEmail());
		if (empty != null) throw new AccountControllerException ("Sorry, it looks like " + account.getEmail() + " belongs to an existing account.");
		if (account.getPassword().length() < 6) throw new AccountControllerException ("Password must be at least 6 characters long");
		return userRepository.saveAndFlush(account);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.AccountController#deleteAccount(com.withinet.opaas.domain.User)
	 */
	public boolean deleteAccount(User user) throws AccountControllerException {
		return false;
		
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.AccountController#updateAccount(com.withinet.opaas.domain.User)
	 */
	public User updateAccount(User account) throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.AccountController#readAccount(java.lang.Long)
	 */
	public User readAccount(User account) throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.AccountController#login(java.lang.String, java.lang.String)
	 */
	public User login(User user) throws AccountException {
		// TODO Auto-generated method stub
		return null;
	}

}

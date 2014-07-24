/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.controller.common.AccountConflictException;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.model.UserRepository;

/**
 * @author Folarin
 *
 */
@RestController
public class AccountControllerImpl implements AccountController {
	
	@Autowired
	UserRepository userRepo;
	
	private final static Logger logger = LoggerFactory
			.getLogger(Application.class);
	
	
	@Override
	public User createAccount(@Valid User account) throws AccountControllerException {
		DomainConstraintValidator<User> dcv = new  DomainConstraintValidator<User> ();
		if (!dcv.isValid(account)) throw new IllegalArgumentException ("Bad request");
		if (userRepo.findByEmail(account.getEmail()) != null) throw new AccountConflictException (account.getEmail() + " is already registered on our system");
		account.setCreated(new Date());
		account.setStatus("Active");
		account = userRepo.save(account);
		logger.info("create user " + account.getEmail () + ", with " + account.getPassword ());
		return account;
	}

	@Override
	public void deleteAccount(Long id, Long requesterId)
			throws AccountControllerException {
		User thisUser = userRepo.findOne(id);
		if (thisUser == null)
			throw new AccountNotFoundException ("Account not found");
		if (thisUser.getID() != requesterId) {
			User admin = thisUser.getAdministrator();
			if (admin == null)
				throw new ControllerSecurityException ("Unauthorized");
			else if (admin.getID() != requesterId)
				throw new ControllerSecurityException ("Unauthorized");
		} else {
			thisUser.setStatus("Deleted");
		}
	}

	@Override
	public User updateAccount(User account, Long id, Long requesterId)
			throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User readAccount(Long id, Long requesterId)
			throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User login(String email, String password)
			throws AccountLoginException {
		if (email == null) throw new AccountLoginException ("Email cannot be empty");
		else if (password == null) throw new AccountLoginException ("Password cannot be empty");
		User user = userRepo.findByEmailAndPassword(email, password);
		if (user == null || user.getID() == null) throw new AccountLoginException ("The email or password you entered is incorrect.");
		//No exception thrown return user
		return user;
		
	}

	@Override
	public List<User> listCollaborators(Long id, Long requesterId)
			throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> addTeamMembers(List<User> teamMembers, Long id,
			Long requesterId) throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<User> addTeamMember(User teamMember, Long id, Long requesterId)
			throws AccountControllerException {
		// TODO Auto-generated method stub
		return null;
	}

}

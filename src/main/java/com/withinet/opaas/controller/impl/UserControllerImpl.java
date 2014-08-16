/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountConflictException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.CollaboratorNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.UnauthorizedException;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class UserControllerImpl implements UserController {
	
	@Autowired
	UserRepository userRepo;
	
	private final static Logger logger = LoggerFactory
			.getLogger(Application.class);
	
	
	@Override
	public User createAccount(@Valid User account) throws UserControllerException {
		DomainConstraintValidator<User> dcv = new  DomainConstraintValidator<User> ();
		if (!dcv.isValid(account)) throw new IllegalArgumentException ("Bad request");
		if (userRepo.findByEmail(account.getEmail()) != null) throw new AccountConflictException (account.getEmail() + " is already registered on our system");
		account.setCreated(new Date());
		account.setStatus("Active");
		account = userRepo.saveAndFlush (account);
		logger.info("create user " + account.getEmail () + ", with " + account.getPassword ());
		return account;
	}

	@Override
	public void deleteAccount(Long id, Long requesterId)
			throws UserControllerException {
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
			throws UserControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User readAccount(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		User user = userRepo.findOne(id);
		if (user == null)
			throw new AccountNotFoundException ("Account does not exist");
		else
			return user;
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
			throws UserControllerException {
		User user = userRepo.findOne(id);
		User requester = userRepo.findOne(requesterId);
		authorizeAssetAccess (user, requester);
		List<User> collaborators = new ArrayList<User> ();
		//collaborators.addAll(user.getCollaborators());
		return collaborators;
	}

	@Override
	public List<User> addCollaborator(User collaborator, Long id, Long requesterId)
			throws UserControllerException {
		User user = userRepo.findOne(id);
		User requester = userRepo.findOne(requesterId);
		authorizeAssetAccess (user, requester);
		if (collaborator.getID() == 0)
			throw new CollaboratorNotFoundException ("Please save collaborator first");
		//user.getCollaborators().add(collaborator);
		collaborator.setAdministrator(user);
		userRepo.saveAndFlush (user);
		userRepo.saveAndFlush (collaborator);
		List<User> collaborators = new ArrayList<User> ();
		//collaborators.addAll(user.getCollaborators());
		return collaborators;
	}
	
	private void authorizeAssetAccess (User user, User requester) throws AccountNotFoundException {
		if (user == null || user.getID() == 0) 
			throw new AccountNotFoundException ("Cannot find this user");
		if (user.getID() != requester.getID()) {
			User admin = user.getAdministrator();
			if (admin == null)
				throw new UnauthorizedException ("Unauthorized");
			else if (admin.getID() != requester.getID()) 
				throw new UnauthorizedException ("Unauthorized");
		} 
	}

}

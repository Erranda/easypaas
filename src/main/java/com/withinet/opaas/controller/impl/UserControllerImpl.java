/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.FileController;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountConflictException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import static com.withinet.opaas.controller.common.ServiceProperties.*;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.UserParserException;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.util.ExcelUserParser;
import com.withinet.opaas.util.MailMan;
import com.withinet.opaas.util.PasswordGenerator;

/**
 * @author Folarin
 *
 */
@RestController
public class UserControllerImpl implements UserController {
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	InstanceController instanceController;
	
	@Autowired
	ProjectController projectController;
	
	@Autowired
	BundleController bundleController;
	
	@Autowired
	FileController fileController;
	
	private Authorizer authorizer;
	
	@Autowired
	public void setAuthorizer (Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	private final static Logger logger = LoggerFactory
			.getLogger(Application.class);
	
	
	@Override
	public User createAccount(@Valid User account) throws UserControllerException {
		account.setWorkingDirectory("");
		DomainConstraintValidator<User> dcv = new  DomainConstraintValidator<User> ();
		if (!dcv.isValid(account)) throw new IllegalArgumentException ("Bad request");
		if (userRepo.findByEmail(account.getEmail()) != null) throw new AccountConflictException (account.getEmail() + " is already registered on our system");
		account.setCreated(new Date());
		account.setQuota(0);
		account.setStatus("Disabled");
		List<User> superAdmins = userRepo.findByRole("SUPER ADMINISTRATOR");
		User superAdmin = superAdmins.get(0);
		account.setAdministrator(superAdmin);
		superAdmin.getTeamMembers().add(account);
		account = userRepo.saveAndFlush (account);
		
		StringBuffer buffer = new StringBuffer ();
		buffer.append("<html><body>");
		buffer.append("Hi " + account.getFullName() + ",<br/><br/><br/>");
		buffer.append("Your registeration is awaiting confirmation by an administrator <br/><br/>");
		buffer.append("You will receive an email once you are approved on to the platform <br/><br/>");
		buffer.append("**********************************************************<br/>");
		buffer.append("Email: " + account.getEmail() + "<br/>");
		buffer.append("Password: " + account.getPassword() + "<br/><br/>");
		buffer.append("Thanks,");
		buffer.append("<br/>System Administrator");
		buffer.append("</body></html>");
		
		StringBuffer bufferAdmin = new StringBuffer ();
		bufferAdmin.append("<html><body>");
		bufferAdmin.append("Hi " + superAdmin.getFullName() + ",<br/><br/><br/>");
		bufferAdmin.append("A user just registered on the platform <br/><br/>");
		bufferAdmin.append("**********************************************************<br/>");
		bufferAdmin.append("Message: " + account.getIntroduction());
		bufferAdmin.append("<br/>Email: " + account.getEmail() + "<br/><br/>");
		bufferAdmin.append("Thanks,");
		bufferAdmin.append("<br/>System Administrator");
		bufferAdmin.append("</body></html>");
		
		MailMan mailer = new MailMan ();	
		try {
			mailer.sendMessage("Registeration Withinet Cloud OSGi Platform", account.getEmail(), buffer.toString());
			mailer.sendMessage("New user Withinet Cloud OSGi Platform", superAdmin.getEmail(), bufferAdmin.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		logger.info("create user " + account.getEmail () + ", with " + account.getPassword ());
		return account;
	}

	@Override
	public void deleteAccount(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SYSTEM_ADMIN, requesterId); 
		User user = getWithBasicAuth (id, requesterId);
		resetAccount (id, requesterId);
		userRepo.delete(user);
	}
	
	@Override
	public void resetAccount(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SYSTEM_ADMIN, requesterId); 
		getWithBasicAuth (id, requesterId);
		try {
			List<Instance> instances = instanceController.listInstancesByUser(id, requesterId);
			for (Instance instance : instances) {
				instanceController.deleteInstance(instance.getId(), requesterId);
			}
			List<Project> projects = projectController.listCreatedProjectsByOwner(id, requesterId);
			for (Project project : projects) {
				projectController.deleteProject(project.getID(), requesterId);
			}	
			List<Bundle> bundles = bundleController.listBundlesByOwner(id, requesterId);
			for (Bundle bundle : bundles) {
				bundleController.deleteBundle(bundle.getID(), requesterId);
			}
		} catch (InstanceControllerException e) {
			e.printStackTrace();
			throw new UserControllerException (e.getMessage());
		} catch (ProjectControllerException e) {
			e.printStackTrace();
			throw new UserControllerException (e.getMessage());
		} catch (BundleControllerException e) {
			e.printStackTrace();
			throw new UserControllerException (e.getMessage());
		}
	}
	
	@Override
	public User updateAccount(User account, Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(account);
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SIGNED_IN, requesterId); 
		User user = getWithBasicAuth (id, requesterId);

		StringBuffer buffer = new StringBuffer ();
		buffer.append("<html><body>");
		buffer.append("Hi " + user.getFullName() + ",<br/><br/><br/>");
		buffer.append("The following updates have been performed on your account<br/><br/>");
		buffer.append("**********************************************************<br/>");
		boolean sendEmail = false;
		if (account.getFullName() != null && !account.getFullName().equals(user.getFullName())) {
			buffer.append("<br/>Name: " + account.getFullName());
			user.setFullName(account.getFullName());
			//Detect and update
			sendEmail = true;
		}
		if (account.getPassword() != null && !account.getPassword().equals(user.getPassword())) {
			buffer.append("<br/>Password: " + account.getPassword());
			user.setPassword(account.getPassword());
			sendEmail = true;
		}
			
		if (account.getStatus() != null && !account.getStatus().equals(user.getStatus())) {
			buffer.append("<br/>Status: " + account.getStatus());
			user.setStatus(account.getStatus());
			sendEmail = true;
		}
			
		if (account.getRole() != null && !account.getRole().equals(user.getRole())) {
			buffer.append("<br/>Role: " + account.getAssignedRole().getName());
			user.setRole(account.getRole());
			user.setAssignedRole(account.getAssignedRole());
			sendEmail = true;
		}
			
		if (account.getQuota() != null && !account.getQuota().equals(user.getQuota())) {
			buffer.append("<br/>Instance Quota Limit: " + account.getQuota());
			user.setQuota(account.getQuota());
		}
			
		
		buffer.append("<br/><br/>Thanks,");
		buffer.append("<br/>System Administrator");
		buffer.append("</body></html>");
		userRepo.saveAndFlush(user);
		MailMan mailer = new MailMan ();	
		try {
			if (sendEmail)
				mailer.sendMessage("Account update Withinet Cloud OSGi Platform", account.getEmail(), buffer.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return user;
	}

	@Override
	public User readAccount(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SIGNED_IN, requesterId); 
		return getWithBasicAuth (id, requesterId);
	}
	
	public User getWithBasicAuth(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		User user = userRepo.findOne(id);
		if (user == null)
			throw new AccountNotFoundException ("Account does not exist");
		if (id == requesterId || user.getAdministrator().getID() == requesterId)
			return user;
		else
			throw new ControllerSecurityException ("Not allowed");
	}
	
	@Override
	public User login(String email, String password)
			throws AccountLoginException {
		Validation.assertNotNull(email);
		Validation.assertNotNull(password);
		email = email.toLowerCase().trim();
		if (email == null) throw new AccountLoginException ("Email cannot be empty");
		else if (password == null) throw new AccountLoginException ("Password cannot be empty");
		User user = userRepo.findByEmailAndPassword(email, password);
		if (user == null || user.getID() == null) throw new AccountLoginException ("The email or password you entered is incorrect.");
		if (user.getStatus().equals("Disabled"))
			throw new AccountLoginException ("Your account is not active");
		return user;
		
	}

	@Override
	public List<User> listTeamMembers(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String>(READ_PROJECT);
		composite.addAll(CREATE_PROJECT);
		authorizer.authorize(composite, requesterId); 
		User user = getWithBasicAuth (id, requesterId);
		List<User> collaborators = userRepo.findByAdministrator(user);
		collaborators.remove(user);
		return collaborators;
	}

	@Override
	public void addTeamMember(User collaborator, Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(collaborator);
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SYSTEM_ADMIN, requesterId);
		User user = getWithBasicAuth (id, requesterId);
		collaborator.setPassword(PasswordGenerator.getRandomPassword());
		collaborator.setLocation(user.getLocation());
		collaborator.setPlatformName(user.getPlatformName());
		collaborator.setCreated(new Date());
		collaborator.setStatus("Active");
		collaborator.setIntroduction("I was added by an administrator");
		collaborator.setWorkingDirectory("");
		DomainConstraintValidator<User> dcv = new  DomainConstraintValidator<User> ();
		if (!dcv.isValid(collaborator)) throw new IllegalArgumentException ("Bad request");
		if (userRepo.findByEmail(collaborator.getEmail()) != null) throw new AccountConflictException (collaborator.getEmail() + " is already registered on our system");
		collaborator.setCreated(new Date());
		collaborator.setAdministrator(user);
		user.getTeamMembers().add(collaborator);
		userRepo.saveAndFlush (collaborator);
		
		StringBuffer buffer = new StringBuffer ();
		buffer.append("<html><body>");
		buffer.append("Hi " + collaborator.getFullName() + ",<br/><br/><br/>");
		buffer.append("Welcome to the " + collaborator.getPlatformName() + " platform.<br/><br/>");
		buffer.append("**********************************************************<br/>");
		buffer.append("Email: " + collaborator.getEmail() + "<br/>");
		buffer.append("Password: " + collaborator.getPassword() + "<br/><br/>");
		buffer.append("<a target=\"_blank\" href=\"" + DOMAIN + "/login\">Login</a><br/><br/>");
		buffer.append(user.getFullName());
		buffer.append("<br/>Platform Administrator");
		buffer.append("</body></html>");
		
		MailMan mailer = new MailMan ();	
		try {
			mailer.sendMessage("Welcome to " + collaborator.getPlatformName(), collaborator.getEmail(), buffer.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<User> addTeamMemberFromExcel(String fileName, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(fileName);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SYSTEM_ADMIN, requesterId);
		List<User> users = new ArrayList<User>();
		try {
			users = ExcelUserParser.parse(fileName);
			for (User user : users) {
				addTeamMember (user, requesterId, requesterId);
			}
		} catch (UserParserException e) {
			throw new UserControllerException (e.getMessage());
		}
		return users;
	}

	@Override
	public void passwordReset(Long id, Long requesterId)
			throws UserControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(SYSTEM_ADMIN, requesterId);
		User user = getWithBasicAuth (id, requesterId);
		user.setPassword(PasswordGenerator.getRandomPassword());
		
		StringBuffer buffer = new StringBuffer ();
		buffer.append("<html><body>");
		buffer.append("Hi " + user.getFullName() + ",<br/><br/><br/>");
		buffer.append("Your new password is " + user.getPassword());
		buffer.append("<br/>**********************************************************<br/><br/>");
		buffer.append(user.getAdministrator().getFullName() + ",<br/><br/>");
		buffer.append("<br/>Platform Administrator");
		buffer.append("</body></html>");
		
		MailMan mailer = new MailMan ();	
		try {
			mailer.sendMessage("Password reset " + user.getPlatformName(), user.getEmail(), buffer.toString());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}

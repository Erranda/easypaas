package test.com.withinet.opaas.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.UUID;

import javax.security.auth.login.AccountException;
import javax.validation.ConstraintViolationException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import test.com.withinet.opaas.controller.common.NullUserException;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.ProjectConflictException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ProjectNotFoundException;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ControllerIntegrationTests {
	
	@Autowired
	AccountController accountController;
	
	User user = new User ();
	
	User loggedInUser = new User ();
	

	private Integer count;
	
	Project project = new Project ();
	
	final String apiKey = UUID.randomUUID().toString();
	
	final String loggedInKey = UUID.randomUUID().toString();
	
	@Before
	public void setup () throws AccountControllerException{ 
		user = new User ();
		user.setApiKey(apiKey);
		
		project = new Project ();
		
		loggedInUser.setFullName ("Folarin O");
		loggedInUser.setEmail("folarinomotoriogun" + count + "@gmail.com");
		loggedInUser.setPassword("Folarin@123");
		loggedInUser.setStatus("registered");
		loggedInUser.setPlatformName("TEST PLATFORM");
		loggedInUser.setApiKey(loggedInKey);
		loggedInUser.setCreated(new Date ());
		accountController.createAccount(loggedInUser);
		
	}
	
	@After
	public void after () {
		count = count + 1;
	}
	
	@Test 
	public void createUserNoException () throws AccountControllerException {
		
		user.setFullName ("Folarin Omotoriogun");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Folarin@123");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
		assertTrue (user.getID() > 0);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createUserInvalidName () throws AccountControllerException {
		user.setFullName ("F");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Folarin@123");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createUserInvalidEmail () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Folarin@123");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
	}
	
	@Test (expected = AccountControllerException.class)
	public void createUserInvalidPassword () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Fol23");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
	}
	
	@Test
	public void deleteUserPerfect () throws AccountControllerException {
		assertTrue (accountController.deleteAccount(user.getID()));
	}
	
	@Test (expected = AccountControllerException.class)
	public void deleteUserFalse () throws AccountControllerException {
		accountController.deleteAccount(5L);
	}
	
	@Test (expected = AccountControllerException.class)
	public void updateEmailNotAllowed () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun12314@gmail.com");
		user.setPassword("Password");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		accountController.createAccount(user);
		user.setEmail ("fajfjafja@xyz.com");
		accountController.updateAccount(user);
	}
	
	@Test (expected = AccountControllerException.class)
	public void updateCreatedDateNotChangeable () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun12314@gmail.com");
		user.setPassword("Password");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		accountController.createAccount(user);
		user.setCreated(new Date ());
		accountController.updateAccount(user);
	}
	
	
	
	@Test
	public void updateUserPerfect () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun1234@gmail.com");
		String oldPassword = "adjasjfajfafa";
		String newPassword = "afahahfgangan";
		user.setPassword(oldPassword);
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		accountController.createAccount(user);
		user.setPassword(newPassword);
		accountController.updateAccount(user);
		assertTrue (!user.getPassword().equals(oldPassword));
	}
	
	@Test
	public void getUserByIdPerfect () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun12314@gmail.com");
		user.setPassword("Password");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		accountController.createAccount(user);
		User fetched = accountController.readAccount(user.getID());
		assertTrue (fetched != null);
	}
	
	@Test (expected = AccountControllerException.class)
	public void getUserByIdNotFound () throws AccountControllerException {
		accountController.readAccount(100L);
	}
	
	@Test (expected = AccountControllerException.class)
	public void loginFailed () throws AccountException {
		User user = new User ();
		user.setEmail("folarin@xyz.com");
		user.setPassword("Password");
		accountController.login(user);
	}
	
	@Test (expected = AccountControllerException.class)
	public void loginSucceeded () throws AccountException, AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun12314@gmail.com");
		user.setPassword("Password");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		accountController.createAccount(user);
		User loginUser = new User ();
		loginUser.setEmail("folarinomotoriogun12314@gmail.com");
		loginUser.setPassword("Password");
		loginUser = accountController.login(loginUser);
		assertTrue (loginUser.getID() > 0);
	}
	
	@Autowired
	ProjectController projectController;

	
	
	
	/**
	 * @throws ProjectControllerException 
	 * 
	 */
	@Test
	public void createProjectPerfect () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoDate () throws ProjectControllerException {		
		project.setName("Operation X service");
		//project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner( loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoName () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		//project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoOwner () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		//project.setOwner(user);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoUpdatedTime () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		// project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ProjectConflictException.class)
	public void createProjectAlreadyExists () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
		projectController.createProject(project);
	}
	
	@Test (expected = ProjectControllerException.class)
	public void deleteProjectPerfect () throws ProjectControllerException {
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
		assertTrue (projectController.deleteProject(project));
	}
	
	@Test (expected = AccountException.class)
	public void deleteProjectNotOwner () throws ProjectControllerException {
		
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
		
		Project project = new Project ();
		project.setID(project.getID());
		project.setOwner(user);
		assertTrue (projectController.deleteProject(project));
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void deleteProjectNullId () throws ProjectControllerException {
		Project project = new Project ();
		project.setOwner(user);
		assertTrue (projectController.deleteProject(project));
	}
	
	@Test (expected = NullUserException.class)
	public void deleteProjectNullUser () throws ProjectControllerException {
		Project project = new Project ();
		project.setOwner(null);
		assertTrue (projectController.deleteProject(project));
	}
	
	@Test (expected = AccountException.class)
	public void deleteProjectUserApiKeyNull () throws ProjectControllerException {
		Project project = new Project ();
		User user = new User ();
		user.setApiKey(null);
		project.setOwner(user);
		assertTrue (projectController.deleteProject(project));
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void deleteProjectNotFound () throws ProjectControllerException {
		Project project = new Project ();
		project.setID(1212L);
		project.setOwner(user);
		assertTrue (projectController.deleteProject(project));
	}
	
	
	
	
	
	
	


}
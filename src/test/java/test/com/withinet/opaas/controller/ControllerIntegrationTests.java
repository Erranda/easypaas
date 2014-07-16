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


import com.withinet.opaas.Application;
import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.NullUserException;
import com.withinet.opaas.controller.common.ProjectConflictException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ProjectNotFoundException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.domain.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ControllerIntegrationTests {
	
	@Autowired
	AccountController accountController;
	
	User user = new User ();
	
	User loggedInUser = new User ();
	
	User noAuthorityUser = new User ();

	private Integer count;
	
	Project project = new Project ();
	
	final String apiKey = UUID.randomUUID().toString();
	
	final String loggedInKey = UUID.randomUUID().toString();
	
	final String unAuthorizedKey = UUID.randomUUID().toString();
	
	public ControllerIntegrationTests () throws AccountControllerException {
		User unAuthenticated = new User ();
		User authenticatedNoAuthority = new User ();
		User authenticatedFullAuthority = new User ();
		
		authenticatedNoAuthority.setFullName ("Folarin O");
		authenticatedNoAuthority.setEmail("folarinomotoriogun" + count + "@gmail.com");
		authenticatedNoAuthority.setPassword("Folarin@123");
		authenticatedNoAuthority.setStatus("registered");
		authenticatedNoAuthority.setPlatformName("TEST PLATFORM");
		authenticatedNoAuthority.setApiKey(loggedInKey);
		authenticatedNoAuthority.setCreated(new Date ());
		accountController.createAccount(authenticatedNoAuthority);
		
		
		authenticatedFullAuthority.setFullName ("Folarin O");
		authenticatedFullAuthority.setEmail("folarinomotoriogun" + count + "@gmail.com");
		authenticatedFullAuthority.setPassword("Folarin@123");
		authenticatedFullAuthority.setStatus("registered");
		authenticatedFullAuthority.setPlatformName("TEST PLATFORM");
		authenticatedFullAuthority.setApiKey(loggedInKey);
		authenticatedFullAuthority.setCreated(new Date ());
		accountController.createAccount(authenticatedFullAuthority);
		
		UserRole role = new UserRole ();
		role.setName("Administrator");
		role.setOwner(authenticatedFullAuthority);
		
		
		
	}
	
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
		
		noAuthorityUser.setFullName ("Folarin O");
		noAuthorityUser.setEmail("folarinomotoriogun" + count + "@gmail.com");
		noAuthorityUser.setPassword("Folarin@123");
		noAuthorityUser.setStatus("registered");
		noAuthorityUser.setPlatformName("TEST PLATFORM");
		noAuthorityUser.setApiKey(unAuthorizedKey);
		noAuthorityUser.setCreated(new Date ());
		accountController.createAccount(noAuthorityUser);
		
		
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
	
	@Test (expected = ConstraintViolationException.class)
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
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Fol23");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
		assertTrue (accountController.deleteAccount(user));
	}
	
	@Test (expected = AccountException.class)
	public void deleteUserUnauthorized () throws AccountControllerException {
		user.setFullName ("Folarin O");
		user.setEmail("folarinomotoriogun" + count + "@gmail.com");
		user.setPassword("Fol23");
		user.setStatus("registered");
		user.setPlatformName("TEST PLATFORM");
		user.setCreated(new Date ());
		user = accountController.createAccount(user);
		user.clientApiKey = "adadadadada";
		assertTrue (accountController.deleteAccount(user));
	}
	
	@Test (expected = AccountNotFoundException.class)
	public void deleteUserFalse () throws AccountControllerException {
		user.setID(5L);
		accountController.deleteAccount(user);
	}
	
	@Test (expected = ConstraintViolationException.class)
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
	
	@Test (expected = ConstraintViolationException.class)
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
		user = accountController.createAccount(user);
		user.setPassword(newPassword);
		user = accountController.updateAccount(user);
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
		User fetched = accountController.readAccount(user);
		assertTrue (fetched != null);
	}
	
	@Test (expected = AccountNotFoundException.class)
	public void getUserByIdNotFound () throws AccountControllerException {
		User user = new User ();
		user.setID(23L);
		accountController.readAccount(user);
	}
	
	@Test (expected = AccountException.class)
	public void loginFailed () throws AccountException {
		User user = new User ();
		user.setEmail("folarin@xyz.com");
		user.setPassword("Password");
		accountController.login(user);
	}
	
	@Test
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
		project.clientApiKey = loggedInKey;
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoDate () throws ProjectControllerException {		
		project.clientApiKey = loggedInKey;
		project.setName("Operation X service");
		//project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner( loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoName () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setName("Operation X service");
		project.setCreated(new Date ());
		//project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void createProjectNoOwner () throws ProjectControllerException {
		project.clientApiKey = "dadada";
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
	
	@Test 
	public void deleteProjectPerfect () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
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
		project.clientApiKey = loggedInKey;
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		projectController.createProject(project);
		
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(project.getID());
		project.setOwner(user);
		projectController.deleteProject(project);
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void deleteProjectNullId () throws ProjectControllerException {
		Project project = new Project ();
		project.clientApiKey = loggedInKey;
		project.setOwner(user);
		projectController.deleteProject(project);
	}
	
	@Test (expected = NullUserException.class)
	public void deleteProjectNullUser () throws ProjectControllerException {
		Project project = new Project ();
		project.clientApiKey = loggedInKey;
		project.setOwner(null);
		projectController.deleteProject(project);
	}
	
	@Test (expected = AccountException.class)
	public void deleteProjectUserApiKeyNull () throws ProjectControllerException {
		Project project = new Project ();
		project.clientApiKey = null;
		project.setOwner(user);
		projectController.deleteProject(project);
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void deleteProjectNotFound () throws ProjectControllerException {
		Project project = new Project ();
		project.clientApiKey = loggedInKey;
		project.setID(1212L);
		project.setOwner(user);
		projectController.deleteProject(project);
	}
	
	@Test 
	public void updateProjectPerfect () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		project.setName("new name");
		project = projectController.updateProject(project);
		assertTrue (!project.getName().equals("Hello world"));
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void updateProjectChangeOwner () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		project.setOwner(user);
		project = projectController.updateProject(project);
	}
	
	@Test (expected = ConstraintViolationException.class)
	public void updateProjectChangeCreatedDate () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		project.setCreated(new Date ());
		project = projectController.updateProject(project);
	}
	
	@Test (expected = AccountException.class)
	public void updateProjectUnauthorized () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		project.clientApiKey = apiKey;
		project = projectController.updateProject(project);
	}

	@Test (expected = AccountException.class)
	public void updateProjectUnauthorizedKeyNull () throws ProjectControllerException {
		project.clientApiKey = null;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		project.clientApiKey = apiKey;
		project = projectController.updateProject(project);
	}
	
	@Test (expected = AccountException.class)
	public void readProjectUnauthorizedKeyNull () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		Project project = new Project ();
		project.clientApiKey = null;
		project.setID(this.project.getID());
		project = projectController.readProject(project);
	}
	
	@Test (expected = AccountException.class)
	public void readProjectUnauthorizedKey () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(this.project.getID());
		project = projectController.readProject(project);
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void readProjectNotFound () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(2121313L);
		project = projectController.readProject(project);
	}
	
	@Test (expected = AccountException.class)
	public void readProjectUnauthorized () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(this.project.getID());
		project = projectController.readProject(project);
		assertTrue (project.getID() > 0);
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void readProjectProjectNotFoundException () throws ProjectControllerException {
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(4242121121L);
		project = projectController.readProject(project);
		assertTrue (project.getID() > 0);
	}
	
	@Test (expected = AccountException.class)
	public void readProjectPerfect () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		Project project = new Project ();
		project.clientApiKey = apiKey;
		project.setID(this.project.getID());
		project = projectController.readProject(project);
		assertTrue (project.getID() > 0);
	}
	
	@Test 
	public void listProjectByUserUnauthorized () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(loggedInUser);
		project.setUpdated(new Date());
		project = projectController.createProject(project);
		projectController.listProjectsByUser(user);
	}
	
	@Test 
	public void listProjectByUserNoProjects () throws ProjectControllerException {
		project.clientApiKey = loggedInKey;
		assertTrue (projectController.listProjectsByUser(user).size() == 0);
	}
	
	//Bundle Controller
	
	@Autowired
	BundleController bundleController;
	
	//Create Bundle Unauthenticated, Unauthorized, ConstraintViolation, Perfect
	@Test (expected = AccountNotFoundException.class)
	public void createBundleUnauthenticated () throws BundleControllerException {
		User user = new User ();
		user.setID(44L);
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(user);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle);
	}
	
	@Test (expected = AccountException.class)
	public void createBundleUnauthrozied () throws BundleControllerException {
		User user = new User ();
		user.setID(44L);
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(noAuthorityUser);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle);
	}
	
	
	
	

}
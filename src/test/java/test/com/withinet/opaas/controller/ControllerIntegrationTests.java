package test.com.withinet.opaas.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.Set;

import javax.security.auth.login.AccountException;

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
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.PermissionController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.RolePermissionController;
import com.withinet.opaas.controller.common.AccountConflictException;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.BundleAlreadyExistsException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.BundleNotFoundException;
import com.withinet.opaas.controller.common.CollaboratorControllerException;
import com.withinet.opaas.controller.common.ControllerException;
import com.withinet.opaas.controller.common.InstanceAlreadyExistsException;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.InstanceNotFoundException;
import com.withinet.opaas.controller.common.ProjectConflictException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ProjectNotFoundException;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.common.RequestParameterConstraintViolation;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Instance;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.model.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ControllerIntegrationTests {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AccountController accountController;
	
	/*@Autowired
	RoleController roleController;
	
	@Autowired
	ServiceProperties serviceProperties;
	
	@Autowired
	PermissionController permissionController;
	
	@Autowired
	RolePermissionController rolePermissionController;
	
	@Autowired
	InstanceController instanceController;*/
	User web = new User ();
	
	@Before
	public void controllerIntegrationTests () throws ControllerException {
		
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("Webber@123");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("web@xyz.com");
		userRepository.save(web);
		/*UserRole webRole = new UserRole ();
		webRole.setDescription("A way of authenticating clients");
		webRole.setName("WEB");
		webRole.setOwner(web);
		
		UserPermission permission1 = new UserPermission ();
		permission1.setValue(serviceProperties.PERM_CREATE_ACCOUNT);
		
		UserPermission permission2 = new UserPermission ();
		permission2.setValue(serviceProperties.PERM_LOGIN_ACCOUNT);
		
		RolePermission rolePerm = new RolePermission ("test", webRole, permission1);
		
		RolePermission rolePerm1 = new RolePermission ("test", webRole, permission2);
	*/
		
	}
	
	@After
	public void cleanUserRepository () {
		userRepository.delete(web);
	}
	
	

	
	@Test
	public void loginSucceeded () throws AccountException, AccountControllerException {
		User object = new User ();
		object.setFullName ("Folarin O");
		object.setEmail("folarinomotoriogun1@gmail.com");
		object.setPassword("Password");
		object.setStatus("registered");
		object.setPlatformName("TEST PLATFORM");
		object.setCreated(new Date ());
		accountController.createAccount(object);
		
		assertTrue (accountController.login("folarinomotoriogun1@gmail.com", "Password").getID() > 0);
	}
	
	@Test (expected = AccountLoginException.class)
	public void loginFailed () throws AccountLoginException {
		accountController.login("folarin@xyz.com", "Password");
	}
	@Test
	public void createUserNoException () throws AccountControllerException {
		User object = new User ();
		object.setCreated(new Date ());
		object.setFullName("Web");
		object.setPassword("Webbere@123");
		object.setPlatformName("Web");
		object.setStatus("active");
		object.setEmail("oweb@xyz.com");
		accountController.createAccount(object);
		assertTrue (object.getID() > 0);
	}

	@Test (expected = AccountConflictException.class)
	public void createUserConflict () throws AccountControllerException {
		User conflict = new User ();
		conflict.setCreated(new Date ());
		conflict.setFullName("Web");
		conflict.setPassword("Webber@123");
		conflict.setPlatformName("Web");
		conflict.setStatus("active");
		conflict.setEmail("ino@xyz.com");
		accountController.createAccount(conflict);
		
		accountController.createAccount(conflict);
	}
	
	/*@Test 
	
	
	@Test (expected = AccountNotFoundException.class)
	public void deleteUserAccountNotFound () throws AccountControllerException {
		User test = new User ();
		test.setID(100000000L);
		test.setCreated(new Date ());
		test.setFullName("Web");
		test.setPassword("Webber@123");
		test.setPlatformName("Web");
		test.setStatus("active");
		test.setEmail("info@xyz.com");
		accountController.deleteAccount(test.getID(), web.getID());
	}
	
	@Test 
	public void deleteUserPerfect () throws AccountControllerException {		
		User test = new User ();
		test.setCreated(new Date ());
		test.setFullName("Web");
		test.setPassword("Webber@123");
		test.setPlatformName("Web");
		test.setStatus("active");
		test.setEmail("infowe1b@xyz.com");
		accountController.createAccount(test);
		assertTrue (accountController.deleteAccount(test.getID(), web.getID()));
	}
	
	@Test (expected = RequestParameterConstraintViolation.class)
	public void deleteUserNoId () throws AccountControllerException {		
		User test = new User ();
		test.setID(null);
		accountController.deleteAccount(test.getID(), web.getID());
	}
	
	@Test
	public void updateUserPerfect () throws AccountControllerException {		
		User conflict = new User ();
		conflict.setPassword("adadada");
		accountController.updateAccount(conflict, web.getID(), web.getID());
		assertTrue (!web.getPassword().equals("Webber@123"));
	}
	
	@Test
	public void getUserByIdPerfect () throws AccountControllerException {		
		User fetched = accountController.readAccount(web.getID(), web.getID());
		assertTrue (fetched.getID() == web.getID());
	}
	
	@Test (expected = AccountNotFoundException.class)
	public void getUserByIdNotFound () throws AccountControllerException {
		User no = new User ();
		no.setID(23232332L);
		accountController.readAccount(no.getID(), web.getID());
	}
	
	
	
	@Autowired
	ProjectController projectController;

	
	
	
	*//**
	 * @throws ProjectControllerException 
	 * 
	 *//*
	@Test
	public void createProjectPerfect () throws ControllerException {
		User object = new User ();
		object.setFullName ("Folarin O");
		object.setEmail("folarinomotoriogun14@gmail.com");
		object.setPassword("Password");
		object.setStatus("registered");
		object.setPlatformName("TEST PLATFORM");
		object.setCreated(new Date ());
		accountController.createAccount(object);
		
		Project project = new Project ();
		project.setName("Operation X service");
		project.setOwner(object);
		project.setCreated(new Date ());
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
	}
	
	@Test (expected = ProjectConflictException.class)
	public void createProjectAlreadyExists () throws ProjectControllerException {
		Project project = new Project ();
		//Project name must be unique
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
		projectController.createProject(project, web.getID());
	}
	
	@Test 
	public void deleteProjectPerfect () throws ProjectControllerException {
		Project project = new Project ();
		project.setName("Operation X service");
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
		assertTrue (projectController.deleteProject(project.getID(), web.getID()));
	}
	
	@Test (expected = RequestParameterConstraintViolation.class)
	public void deleteProjectNullId () throws ProjectControllerException {
		Project project = new Project ();
		projectController.deleteProject(project.getID(), web.getID());
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void deleteProjectNotFound () throws ProjectControllerException {
		Project project = new Project ();
		project.setID(1212L);
		projectController.deleteProject(project.getID(), web.getID());
	}
	
	@Test 
	public void updateProjectPerfect () throws ProjectControllerException {
		Project project = new Project ();
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
		
		Project project1 = new Project ();
		project1.setName("new name");
		projectController.updateProject(project1, project.getID(), web.getID());
		assertTrue (!project.getName().equals("Hello world"));
	}
	
	@Test (expected = ProjectNotFoundException.class)
	public void readProjectNotFound () throws ProjectControllerException {
		Project project = new Project ();
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		project = projectController.createProject(project, web.getID());
		
		Project read = new Project ();
		read.setID(2121313L);
		read = projectController.readProject(project.getID(), web.getID());
	}
	
	@Test 
	public void readProjectPerfect () throws ProjectControllerException {
		Project project = new Project ();
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
		
		Project read = new Project ();
		read.setID(project.getID());
		projectController.readProject(read.getID(), web.getID());
		assertTrue (read.getName() != null);
	}
	
	@Test 
	public void listProjectByUser () throws ProjectControllerException {
		assertTrue (projectController.listProjectsByUser(web.getID(), web.getID()).size()> 0);
	}
	
	@Test (expected = AccountNotFoundException.class)
	public void listProjectByUserNotFound () throws ProjectControllerException {
		User user = new User ();
		user.setID(313L);
		projectController.listProjectsByUser(user.getID(), web.getID());
	}
	
	//Bundle Controller
	
	@Autowired
	BundleController bundleController;
	
	//Create Bundle Unauthenticated, Unauthorized, ConstraintViolation, Perfect
	@Test 
	public void createBundle () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(web);
		bundle.setSymbolicName("test-b");
		assertTrue (bundleController.createBundle(bundle, web.getID()).getID() > 0);
	}
	
	@Test (expected = BundleAlreadyExistsException.class)
	public void createBundleAlreadyExists () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(web);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle, web.getID());
		bundleController.createBundle(bundle, web.getID());
	}
	
	@Test
	public void deleteBundle () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(web);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle, web.getID());
		
		Bundle bundleTemp = new Bundle ();
		bundleTemp.setOwner(web);
		bundleTemp.setID (bundle.getID());
		assertTrue (bundleController.deleteBundle(bundleTemp.getID(), web.getID()));
	}
	
	@Test
	public void updateBundle () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(web);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle, web.getID());
		
		Bundle bundleTemp = new Bundle ();
		bundleTemp.setOwner(web);
		bundleTemp.setSymbolicName("adadasdaa");
		bundleController.updateBundle(bundleTemp, bundle.getID(), web.getID());
		assertTrue (bundle.getSymbolicName().equals(bundleTemp.getSymbolicName()));
	}
	
	@Test (expected = BundleNotFoundException.class)
	public void updateBundleNotFound () throws BundleControllerException {
		Bundle bundleTemp = new Bundle ();
		bundleTemp.setOwner(web);
		bundleTemp.setID (43L);
		bundleTemp.setSymbolicName("adadasdaa");
		bundleController.updateBundle(bundleTemp, 43L, web.getID());
	}
	
	@Test
	public void readBundle () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http://afafad.com/fjaj.jar");
		bundle.setOwner(web);
		bundle.setSymbolicName("test-b");
		bundleController.createBundle(bundle, web.getID());
		
		Bundle bundleTemp = new Bundle ();
		bundleTemp.setID (bundle.getID());
		assertTrue (bundleController.readBundle(bundleTemp.getID(), web.getID()).getSymbolicName().equals(bundle.getSymbolicName()));
	}
	
	@Test (expected = BundleNotFoundException.class)
	public void readBundleNotFound () throws BundleControllerException {
		Bundle bundleTemp = new Bundle ();
		bundleTemp.setID (4005L);
		bundleController.readBundle(bundleTemp.getID(), web.getID());
	}
	
	@Test 
	public void createInstance () throws ProjectControllerException, InstanceControllerException {
		Project project = new Project ();
		project.setCreated(new Date ());
		project.setName("Hello world");
		project.setOwner(web);
		project.setUpdated(new Date());
		projectController.createProject(project, web.getID());
		
		Instance instance = new Instance ();
		instance.setHost("127.0.0.1");
		instance.setOwner(web);
		instance.setPort(8080);
		instance.setProject(project);
		instanceController.createInstance(instance, web.getID());
		
	}
	
	@Test (expected = InstanceAlreadyExistsException.class)
	public void createInstanceAlreadyExists () {
		
	}
	
	@Test
	public void readInstance () {
		
	}
	
	@Test (expected = InstanceNotFoundException.class)
	public void readInstanceNotFound () {
		
	}
	
	@Test 
	public void readInstanceByProject () {
		
	}
	
	@Test 
	public void readInstanceByUser () {
		
	}
	
	@Test 
	public void deleteInstance () {
		
	}
	
	@Test (expected = InstanceNotFoundException.class)
	public void deleteInstanceNotFound () {
		
	}
	
	@Test
	public void createCollaborator () throws CollaboratorControllerException {
		
	}
	
	@Test
	public void createCollaboratorBatch () throws CollaboratorControllerException {
		
	}
	
	@Test
	public void deleteCollaborator () throws CollaboratorControllerException {
		
	}
	
	@Test
	public void updateCollaborator () throws CollaboratorControllerException {
		
	}
	
	@Test
	public void readCollaborator () throws CollaboratorControllerException {
		
	}
	
	
	
	
	
*/	
}
	
	
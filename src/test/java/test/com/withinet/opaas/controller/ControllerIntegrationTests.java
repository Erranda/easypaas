/*package test.com.withinet.opaas.controller;

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
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.PermissionController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.RolePermissionController;
import com.withinet.opaas.controller.common.AccountConflictException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.AccountNotFoundException;
import com.withinet.opaas.controller.common.BundleAlreadyExistsException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.BundleNotFoundException;
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
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ControllerIntegrationTests {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserController accountController;
	
	@Autowired
	BundleController controller;
	
	@Autowired
	RoleController roleController;
	
	@Autowired
	ServiceProperties serviceProperties;
	
	@Autowired
	PermissionController permissionController;
	
	@Autowired
	RolePermissionController rolePermissionController;
	
	@Autowired
	InstanceController instanceController;
	User web = new User ();
	
	@Before
	public void controllerIntegrationTests () throws ControllerException {
		
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("Webber@123");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("web@xyz.com");
		web.setAdministrator(web);
		web.setQuota(-1);
		web.setRole("ADMIN");
		web.setLocation("Somewhere");
		userRepository.save(web);
		UserRole webRole = new UserRole ();
		webRole.setDescription("A way of authenticating clients");
		webRole.setName("WEB");
		webRole.setOwner(web);
		
		UserPermission permission1 = new UserPermission ();
		permission1.setValue(serviceProperties.PERM_CREATE_ACCOUNT);
		
		UserPermission permission2 = new UserPermission ();
		permission2.setValue(serviceProperties.PERM_LOGIN_ACCOUNT);
		
		RolePermission rolePerm = new RolePermission ("test", webRole, permission1);
		
		RolePermission rolePerm1 = new RolePermission ("test", webRole, permission2);
	
		
	}
	
	@After
	public void cleanUserRepository () {
		userRepository.delete(web);
	}
	
	@Test (expected = BundleNotFoundException.class)
	public void deleteBundle () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http:..adada");
		bundle.setOwner(web);
		bundle.setSymbolicName("Hello");
		bundle.setUpdated(new Date ());
		
		controller.createBundle(bundle, web.getID());
		controller.deleteBundle(bundle.getID(), web.getID());
		controller.readBundle(bundle.getID(), web.getID());
	}

	
	@Test
	public void loginSucceeded () throws AccountException, UserControllerException {
		User object = new User ();
		object.setFullName ("Folarin O");
		object.setEmail("folarinomotoriogun1@gmail.com");
		object.setPassword("Password");
		object.setStatus("registered");
		object.setAdministrator(null);
		object.setPlatformName("TEST PLATFORM");
		object.setLocation("Somewhere");
		object.setQuota(-1);
		object.setRole("");
		object.setCreated(new Date ());
		accountController.createAccount(object);
		
		assertTrue (accountController.login("folarinomotoriogun1@gmail.com", "Password").getID() > 0);
	}
	
	@Test (expected = AccountLoginException.class)
	public void loginFailed () throws AccountLoginException {
		accountController.login("folarin@xyz.com", "Password");
	}
	@Test
	public void createUserNoException () throws UserControllerException {
		User object = new User ();
		object.setCreated(new Date ());
		object.setFullName("Web");
		object.setPassword("Webbere@123");
		object.setPlatformName("Web");
		object.setStatus("active");
		object.setEmail("oweb@xyz.com");
		object.setLocation("Somewhere");
		object.setQuota(-1);
		object.setRole("ADMIN");
		accountController.createAccount(object);
		assertTrue (object.getID() > 0);
	}

	@Test (expected = AccountConflictException.class)
	public void createUserConflict () throws UserControllerException {
		User conflict = new User ();
		conflict.setCreated(new Date ());
		conflict.setFullName("Web");
		conflict.setPassword("Webber@123");
		conflict.setPlatformName("Web");
		conflict.setStatus("active");
		conflict.setEmail("ino@xyz.com");
		conflict.setLocation("Somewhere");
		conflict.setQuota(-1);
		conflict.setRole("ADMIN");
		accountController.createAccount(conflict);
		
		accountController.createAccount(conflict);
	}
	
	@Test 
	public void updateUserOk () throws UserControllerException {
		User conflict = new User ();
		conflict.setCreated(new Date ());
		conflict.setFullName("Web");
		conflict.setPassword("Webber@123");
		conflict.setPlatformName("Web");
		conflict.setStatus("active");
		conflict.setEmail("ino@xyz.com");
		conflict.setLocation("Somewhere");
		conflict.setAdministrator(web);
		conflict.setQuota(-1);
		conflict.setRole("ADMIN");
		web.getTeamMembers().add(conflict);
		accountController.createAccount(conflict);
		
		User user = new User ();
		user.setFullName("Master Yo");
		user.setPassword("NewPassword");
		
		accountController.updateAccount(user, conflict.getID(),conflict.getID());
		assertTrue (accountController.readAccount(conflict.getID(), web.getID()).getFullName().equals("Master Yo"));
	}
	
	@Test
	public void addTeamMemberOk() throws UserControllerException {
		User web = new User();
		web.setCreated(new Date());
		web.setFullName("Web Dev");
		web.setPassword("abc@xyz.com");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("abcd@xyz.com");
		web.setLocation("United Kingdom");
		web.setAdministrator(web);
		web.setQuota(-1);
		web.setRole("ADMINISTRATOR");
		accountController.createAccount(web);

		User pao = new User();
		pao.setCreated(new Date());
		pao.setFullName("Pao");
		pao.setPassword("Pao@123");
		pao.setPlatformName("Yes man");
		pao.setStatus("active");
		pao.setEmail("paod@xyz.com");
		pao.setLocation("United Kingdom");
		pao.setQuota(-1);
		pao.setRole("ADMINISTRATOR");

		User ming = new User();
		ming.setCreated(new Date());
		ming.setFullName("Ming");
		ming.setPassword("Ming@123");
		ming.setPlatformName("Web");
		ming.setStatus("active");
		ming.setEmail("mingd@xyz.com");
		ming.setLocation("United Kingdom");
		ming.setQuota(-1);
		ming.setRole("ADMININSTRATOR");

		accountController.addTeamMember(pao, web.getID(), web.getID());
		accountController.addTeamMember(ming, web.getID(), web.getID());
		System.out.println (accountController.listTeamMembers(web.getID(), web.getID()).size());
		assertTrue (accountController.listTeamMembers(web.getID(), web.getID()).size() == 2);
	}
	
	@Test
	public void listTeamMembers () throws UserControllerException {
		User collab = new User ();
		collab.setCreated(new Date ());
		collab.setFullName("Web");
		collab.setPassword("Webber@123");
		collab.setPlatformName("Web");
		collab.setStatus("active");
		collab.setEmail("ino@xyz.com");
		collab.setLocation("Somewhere");
		accountController.createAccount(collab);
		
		User collado = new User ();
		collado.setCreated(new Date ());
		collado.setFullName("Web");
		collado.setPassword("Webber@123");
		collado.setPlatformName("Web");
		collado.setStatus("active");
		collado.setEmail("incxo@xyz.com");
		collado.setLocation("Somewhere");
		accountController.createAccount(collado);
		
		System.out.println (accountController.addTeamMember(collado, collab.getID(), collab.getID()).size());
		System.out.println (collado.getTeamMembers().size());
		assertTrue (collado.getAdministrator().equals(collab));
	}
		
}
	
	*/
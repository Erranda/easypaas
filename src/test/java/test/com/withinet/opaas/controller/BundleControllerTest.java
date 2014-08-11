package test.com.withinet.opaas.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;




import static org.junit.Assert.*;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleConflictException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ControllerException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.impl.BundleControllerImpl;
import com.withinet.opaas.controller.system.impl.FileServiceImpl;
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.ProjectBundleRepository;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;
import com.withinet.opaas.model.domain.User;

public class BundleControllerTest {
	
	BundleControllerImpl bundleController;
	
	BundleRepository bundleRepository;
	
	UserController userController;
	
	ProjectBundleRepository projectBundleRepository;

	User user = new User ();
	
	Bundle forSave = new Bundle ();
	

	List<Bundle> bundles = new ArrayList<Bundle> ();
	
	@Before
	public void controllerIntegrationTests () throws ControllerException {
		user = new User ();
		user.setID(1L);
		user.setAdministrator(user);
		
		Bundle bundle = new Bundle ();
		bundle.setID(1L);
		bundle.setLocation("http://helloworld.jar");
		bundle.setSymbolicName("Hello World");
		bundle.setUpdated(new Date());
		bundle.setOwner(user);
		
		forSave.setID(1L);
		forSave.setLocation("http://helloworld.jar");
		forSave.setSymbolicName("HelloWorld");
		forSave.setUpdated(new Date());
		forSave.setOwner(user);
	
		bundles.add (bundle);
		bundles.add (forSave);
		
		bundleRepository = Mockito.mock(BundleRepository.class);
		Mockito.when(bundleRepository.save(forSave)).thenReturn (forSave);
		Mockito.when(bundleRepository.findAll()).thenReturn (bundles);
		Mockito.when (bundleRepository.findOne(1L)).thenReturn(forSave);
		Mockito.when(bundleRepository.findByOwner(user)).thenReturn(bundles);
		Mockito.when(bundleRepository.findByOwnerAndSymbolicName (user, "Hello World")).thenReturn(bundle);
		
		userController = Mockito.mock(UserController.class);
		Mockito.when(userController.readAccount(1L, 1L)).thenReturn(user);
		
		bundleController = new BundleControllerImpl ();
		bundleController.setBundleRepository(bundleRepository);
		bundleController.setUserController(userController);
		bundleController.setFileService(new FileServiceImpl ());
		
		List<ProjectBundle> pbs = new ArrayList<ProjectBundle> ();
		pbs.add(new ProjectBundle ("fola", new Project(), forSave));
		projectBundleRepository = Mockito.mock(ProjectBundleRepository.class);
		Mockito.when(projectBundleRepository.findByBundle(forSave)).thenReturn(pbs);
		bundleController.setProjectBundleRepository(projectBundleRepository);
	}
	
	@Test 
	public void createBundleOk () throws BundleControllerException {
		assertTrue (bundleController.createBundle(forSave, user.getID()).getID() > 0);
	}

	@Test (expected = BundleConflictException.class) 
	public void createBundleConflict () throws BundleControllerException {
		Mockito.when(bundleRepository.findByOwnerAndSymbolicName (user, "HelloWorld")).thenReturn(forSave);
		bundleController.createBundle(forSave, user.getID());
	}
	
	@Test
	public void updateProject () throws BundleControllerException {
		Bundle newBundle = new Bundle ();
		newBundle.setLocation("hiworld");
		newBundle.setID(1L);
		bundleController.updateBundle(newBundle, user.getID()).getLocation().equals("hiworld");
	}
	
	@Test (expected = ControllerSecurityException.class)
	public void updateProjectFail () throws BundleControllerException {
		Bundle newBundle = new Bundle ();
		newBundle.setLocation("hiworld");
		newBundle.setID(1L);
		bundleController.updateBundle(newBundle, 2L).getLocation().equals("hiworld");
	}
	
	@Test
	public void deleteBundle()
			throws BundleControllerException {
		assertTrue (bundleController.deleteBundle(1L, user.getID()));
	}
	
	@Test
	public void listBundlesByOwner() throws BundleControllerException {
		assertTrue (bundleController.listBundlesByOwner(1L, 1L).size() > 0);
	}

	@Test
	public void readBundleByName()
			throws BundleControllerException {
		assertTrue (bundleController.readBundleByName("Hello World", user.getID()) != null);
	}
}
	
	
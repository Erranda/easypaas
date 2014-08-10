package test.com.withinet.opaas.controller;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class BundleControllerTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	BundleController bundleController;
	
	

	User web = new User ();
	
	@Before
	public void controllerIntegrationTests () throws ControllerException {
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("Webber@123");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("web@xyz.com");
		web.setLocation("Somewhere");
		userRepository.save(web);
	}
	
	@After
	public void cleanUserRepository () {
		userRepository.delete(web);
	}
	
	@Test 
	public void createBundleOk () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http:ssdasaa");
		bundle.setOwner(web);
		bundle.setSymbolicName("Hello");
		web.getBundles().add(bundle);
		assertTrue (bundleController.createBundle(bundle, web.getID()).getID() > 0);
		bundleController.deleteBundle(bundle.getID(), web.getID());
	}

	@Test (expected = BundleConflictException.class) 
	public void createBundleConflict () throws BundleControllerException {
		Bundle bundle = new Bundle ();
		bundle.setLocation("http:ssdasaa");
		bundle.setOwner(web);
		bundle.setSymbolicName("Hello");
		web.getBundles().add(bundle);
		bundleController.createBundle(bundle, web.getID());
		bundleController.createBundle(bundle, web.getID());
		bundleController.deleteBundle(bundle.getID(), web.getID());
	}
	
}
	
	
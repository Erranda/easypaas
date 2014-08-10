package test.com.withinet.opaas.controller;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class InstanceControllerTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProjectController projectController;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	BundleRepository bundleRepository;
	
	@Autowired
	InstanceController instanceController;

	User web = new User ();
	
	User admin = new User ();
	
	Project p = new Project ();
	
	
	public InstanceControllerTest () throws ProjectControllerException {
			
		
	}
	
	@Before
	public void setup () throws ProjectControllerException {
		admin.setCreated(new Date ());
		admin.setFullName("Web");
		admin.setPassword("Webber@123");
		admin.setPlatformName("Web");
		admin.setStatus("active");
		admin.setEmail("admin@xyz.com");
		admin.setLocation("Somewhere");
		userRepository.save(admin);
		
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("Webber@123");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("web@xyz.com");
		web.setLocation("Somewhere");
		web.setAdministrator(admin);
		
		userRepository.save(web);
		
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setOwner(web);
		p.setStatus("Active");
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
	}
	
	@After
	public void delete () {
		
		userRepository.delete(web);
		userRepository.delete(admin);
	}
	@Test
	public void testCreateInstance() throws ProjectControllerException, InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setStatus("Active");
		instance.setOwnerName(web.getFullName());
		instance.setProject(p);
		instance.setPort(9090);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		assertTrue (instanceController.createInstance(instance, p.getID(), web.getID(), web.getID()).getId() > 0);
	}

	@Test
	public void testDeleteInstance() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		assertTrue (instanceController.deleteInstance(instance.getId(), web.getID()));
	}

	@Test
	public void testUpdateInstance() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		
		Instance update = new Instance ();
		update.setStatus("Stopped");
		update.setId(instance.getId());
		assertTrue (instanceController.updateInstance(update, web.getID()).getStatus().equals("Stopped"));
	}

	@Test
	public void testReadInstance() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		assertTrue (instanceController.readInstance(instance.getId(), web.getID()) != null);
	}

	@Test
	public void testListInstancesByUser() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		assertTrue (instanceController.listInstancesByUser(web.getID(), web.getID()).size() == 1);
	}

	@Test
	public void testListInstancesByProject() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		assertTrue (instanceController.listInstancesByProject(p.getID(), web.getID()).size() == 1);
	}

	@Test
	public void testListInstancesByAdministrator() throws InstanceControllerException {
		Instance instance = new Instance ();
		instance.setCpanelUrl("http://cloud.withinet.com");
		instance.setCreated(new Date());
		instance.setHostName("live-1");
		instance.setOsgiContainerName("felix");
		instance.setOwnerName(web.getFullName());
		instance.setPort(9090);
		instance.setStatus("Active");
		instance.setProject(p);
		instance.setOwner(web);
		instance.setAdministrator(admin);
		instance = instanceController.createInstance(instance, p.getID(), web.getID(), web.getID());
		
		Instance instance1= new Instance ();
		instance1.setCpanelUrl("http://cloud.withinet.com");
		instance1.setCreated(new Date());
		instance1.setHostName("live-1");
		instance1.setOsgiContainerName("felix");
		instance1.setOwnerName(web.getFullName());
		instance1.setPort(9090);
		instance1.setStatus("Active");
		instance1.setProject(p);
		instance1.setOwner(web);
		instance1.setAdministrator(admin);
		instance1 = instanceController.createInstance(instance1, p.getID(), web.getID(), web.getID());
		
		assertTrue (instanceController.listInstancesByAdministrator(admin.getID(), admin.getID()).size() == 2);
	}

}

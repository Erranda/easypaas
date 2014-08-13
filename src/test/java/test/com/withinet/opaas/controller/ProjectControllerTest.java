package test.com.withinet.opaas.controller;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;
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
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class ProjectControllerTest {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProjectController projectController;
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	BundleRepository bundleRepository;

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
	
	@Test public void createProjectOk () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setOwner(web);
		p.setStatus("Active");
		p.setUpdated(new Date());
		web.getProjects().add(p);
		assertTrue (projectController.createProject(p, web.getID()).getID() > 0);
		
		//projectRepository.delete(p);
	}
	
	@Test (expected = ProjectConflictException.class)
	public void createProjectException () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setOwner(web);
		p.setStatus("Active");
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
		projectController.createProject(p, web.getID());
		//projectRepository.delete(p);
	}
	
    @Test 
    public void deleteProject () throws ProjectControllerException {
    //	fail ();
    }
	
	@Test public void updateProject () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setOwner(web);
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
		p.setName("Nice project");
		Project  p1 = projectController.updateProject(p, p.getID(), web.getID());
		assertTrue (p1.getName().equals("Nice project"));
		//projectRepository.delete(p);projectRepository.delete(p1);
	}
	
	@Test 
	public void readProjectByOwner () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setOwner(web);
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
		
		Project p1 = new Project ();
		p1.setCreated(new Date ());
		p1.setDetails("Hello");
		p1.setName("Hello");
		p1.setStatus("Active");
		p1.setOwner(web);
		p1.setUpdated(new Date());
		web.getProjects().add(p1);
		projectController.createProject(p1, web.getID());
		
		assertTrue (projectController.listCreatedProjectsByOwner(web.getID(), web.getID ()).size()  == 2);
		//projectRepository.delete(p);
		//projectRepository.delete(p1);
	}

	@Test 
	public void addCollaborator () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello m");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setOwner(web);
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
		
		User mem = new User ();
		mem.setCreated(new Date ());
		mem.setFullName("Web");
		mem.setPassword("Webbder@123");
		mem.setPlatformName("Web");
		mem.setStatus("active");
		mem.setEmail("mem@xyz.com");
		mem.setLocation("Somewhere");
		web.getCollaborators().add(mem);
		userRepository.save(mem);
		
		projectController.addCollaborator(mem, p.getID(), web.getID());
		
		assertTrue (projectController.listProjectTeamMembersByProject(p.getID(), web.getID()).size() == 1);
		///userRepository.delete(mem);
	}
	
	@Test 
	public void addBundles () throws ProjectControllerException {
		Project p = new Project ();
		p.setCreated(new Date ());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setOwner(web);
		p.setUpdated(new Date());
		web.getProjects().add(p);
		projectController.createProject(p, web.getID());
		
		Bundle b = new Bundle ();
		b.setLocation("http://dadiddiadaid.com/adja.jar");
		b.setOwner(web);
		b.setSymbolicName("Bundle");
		b.setUpdated(new Date ());
		web.getBundles().add(b);
		bundleRepository.save(b);
		
		projectController.addBundle(b, p.getID(), web.getID());
		
		assertTrue (projectController.listProjectBundlesByProject(p.getID(), web.getID()).size() == 1);
	}
		
}
	
	
package test.com.withinet.opaas.domain.access;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.withinet.opaas.Application;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Instance;
import com.withinet.opaas.domain.Organisation;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.ProjectBundle;
import com.withinet.opaas.domain.RolePermission;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.domain.UserPermission;
import com.withinet.opaas.domain.UserRole;
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.InstanceRepository;
import com.withinet.opaas.model.OrganisationRepository;
import com.withinet.opaas.model.ProjectBundleRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.RolePermissionRepository;
import com.withinet.opaas.model.UserPermissionRepository;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.UserRoleRepository;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Integration tests for {@link CityRepository}.
 *
 * @author Folarin Omotoriogun
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@ActiveProfiles("test")
public class RepositoryIntegrationTests {

	@Autowired
	UserRepository repository;
	
	@Autowired
	OrganisationRepository oRepository;
	
	@Autowired
	UserRoleRepository uRoleRepository;
	
	@Autowired
	UserPermissionRepository uPermissionRepository;
	
	@Autowired
	BundleRepository bRepository;
	
	@Autowired
	ProjectRepository pRepository;
	
	@Autowired
	InstanceRepository iRepository;
	
	@Autowired
	RolePermissionRepository rpRepo;
	
	@Autowired
	ProjectBundleRepository projectBundleRepo;
	
	Organisation o = new Organisation ();
	
	User user = new User ();
	
	UserRole r = new UserRole ();
	
	UserPermission p = new UserPermission ();
	
	Bundle b = new Bundle ();
	
	Project pr = new Project ();
	
	Instance i = new Instance ();
	
	RolePermission rp = new RolePermission ();
	
	@Before
	public void setUp() throws Exception {
		o.setLocation("Nigeria");
		o.setName("University of Lagos");
		o.setAddedBy("foo@xyz.com");
		o.setCreated(new Date ());
		this.oRepository.save(o);
		
		user.setCreated(new Date());
		user.setEmail("foo@xyz.com");
		user.setFullName("Someone");
		user.setPassword("Password");
		user.setPlatformName("CS5041");
		user.setStatus("registered");
		user.setOrganisation(o);
		user.setApiKey(UUID.randomUUID().toString());
		this.repository.save(user);
		
		r.setName("Project Manager");
		r.setDescription("Can create projects");
		r.setOwner(user);
		this.uRoleRepository.save(r);
		
		p.setValue("Start-Instance");
		uPermissionRepository.save(p);
		 
		
		
		b.setLocation("file://C:/sample/location");
		b.setOwner(user);
		b.setSymbolicName("opaasbundle");
		bRepository.save(b);
		
		pr.setName("Hello");
		pr.setOwner(user);
		pr.setDetails("Some information");
		pr.setCreated(new Date ());
		pr.setUpdated(new Date ());
		pRepository.save(pr);
		
		ProjectBundle pb = new ProjectBundle (user.getEmail(), pr, b);
		projectBundleRepo.save(pb);
		
		i.setHost("127.0.0.1");
		i.setPort(8080);
		i.setProject(pr);
		i.setOwner(user);
		iRepository.save(i);
		
		rp = new RolePermission (user.getEmail(), r, p);
		rpRepo.save(rp);
	}
	
	@After
	public void tearDown () {
		this.rpRepo.delete(rp);
		this.oRepository.delete(o);
		
		
		

	}
	
	@Test
	public void findsFirstPageOfUsers() {
		Page<User> users = this.repository.findAll(new PageRequest (0, 10));
		assertThat(users.getTotalElements(), is(org.hamcrest.Matchers.equalTo(1L)));
	}
	
	@Test
	public void findByEmail () {
		User user = this.repository.findByEmail("foo@xyz.com");
		assertThat(user, org.hamcrest.Matchers.notNullValue ());
	}
	
	@Test
	public void saveAndFind() {;
		Page<Organisation> object = this.oRepository.findAll(new PageRequest (0, 10));
		assertThat(object.getTotalElements(), is(org.hamcrest.Matchers.equalTo(1L)));
	}
	
	@Test
	public void saveAndFindByName() {
		List<Organisation> object = this.oRepository.findByName("University of Lagos");
		assertThat(object.size(), is(org.hamcrest.Matchers.equalTo(1)));
	}
	
	@Test
	public void getRolesForUser () {
		List<UserRole> object =  uRoleRepository.findByOwner(user);
		assertThat(object.size(), is(org.hamcrest.Matchers.equalTo(1)));	
	}
	
	@Test
	public void getRolesForPermission () {
		List<RolePermission> object =  rpRepo.findByUserRole(r);
		assertThat(object.size(), is(org.hamcrest.Matchers.equalTo(1)));	
	}
	
	@Test
	public void getRolesForOwnerName () {
		UserRole object =  uRoleRepository.findByOwnerAndName(user, r.getName());
		assertThat(object, org.hamcrest.Matchers.notNullValue());	
	}
	
	@Test
	public void listBundlesByProject () {
		List<ProjectBundle> object = projectBundleRepo.findByUserProject(pr);
		assertThat(object, org.hamcrest.Matchers.notNullValue());
		
	}
	
}
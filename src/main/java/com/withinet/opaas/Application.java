package com.withinet.opaas;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.Cookie;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.settings.IRequestCycleSettings;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.apache.wicket.util.lang.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.model.PermissionRepository;
import com.withinet.opaas.model.RolePermissionRepository;
import com.withinet.opaas.model.RoleRepository;
import com.withinet.opaas.model.domain.Permission;
import com.withinet.opaas.model.domain.Role;
import com.withinet.opaas.model.domain.RolePermission;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.Index;
import com.withinet.opaas.wicket.html.BundleIndex;
import com.withinet.opaas.wicket.html.Dashboard;
import com.withinet.opaas.wicket.html.InstanceIndex;
import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.html.ProjectIndex;
import com.withinet.opaas.wicket.html.Register;
import com.withinet.opaas.wicket.html.TeamIndex;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * The web application class also serves as spring boot starting point by using
 * spring boot's EnableAutoConfiguration annotation and providing the main
 * method.
 * 
 * @author kloe and Folarin
 * 
 */
@Component
@EnableAutoConfiguration
@ComponentScan
public class Application extends WebApplication {

	private final static Logger logger = LoggerFactory
			.getLogger(Application.class);

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * spring boot main method to build context
	 * 
	 * @param args
	 */
	public static void main(String... args) {
		SpringApplication.run(Application.class, args);

	}

	/**
	 * provides page for default request
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return Index.class;
	}

	@Autowired
	com.withinet.opaas.model.UserRepository userRepo;
	
	@Autowired
	PermissionRepository permissionRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	RolePermissionRepository rpRepo;
	
	@Autowired
	RoleController roleCtrl;

	/**
	 * <ul>
	 * <li>making the wicket components injectable by activating the
	 * SpringComponentInjector</li>
	 * <li>mounting the test page</li>
	 * <li>logging spring service method output to showcase working integration</li>
	 * </ul>
	 */
	@Override
	protected void init() {
		super.init();
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setCompressWhitespace(true);
		getApplicationSettings().setUploadProgressUpdatesEnabled(true);
		getRequestCycleSettings().setRenderStrategy(
				IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
		getMarkupSettings().setStripWicketTags(true);
		getMarkupSettings().setStripComments(true);
		getMarkupSettings().setCompressWhitespace(true);
		getApplicationSettings().setUploadProgressUpdatesEnabled(true);
		getStoreSettings().setMaxSizePerSession(Bytes.kilobytes(5000));
		getStoreSettings().setInmemoryCacheSize(500);
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, applicationContext));
		mountPage("/login", Login.class);
		mountPage("/register", Register.class);
		mountPage("/projects", ProjectIndex.class);
		mountPage("/dashboard", Dashboard.class);
		mountPage("/bundles", BundleIndex.class);
		mountPage("/team", TeamIndex.class);
		mountPage("/instances", InstanceIndex.class);

		User web = new User();
		web.setCreated(new Date());
		web.setFullName("Withinet System Team");

		web.setPlatformName("OSGi Cloud Platform as a Service");
		web.setStatus("Active");
		web.setEmail(ServiceProperties.SUPER_ADMIN_EMAIL);
		web.setPassword(ServiceProperties.SUPER_ADMIN_PASSWORD);
		web.setLocation("United Kingdom");
		web.setAdministrator(web);
		web.setQuota(-1);
		web.setRole("SUPER ADMINISTRATOR");
		web.setWorkingDirectory("");
		web.setIntroduction("I am the main adminitrator for this system");
		userRepo.save(web);
		
		Permission p1 = new Permission ();
		p1.setValue("readProject");
		p1.setDescription("Read projects");
		permissionRepo.save(p1);
		
		Permission p2 = new Permission ();
		p2.setValue("createProject");
		p2.setDescription("Create projects");
		permissionRepo.save(p2);
		
		Permission p3 = new Permission ();
		p3.setValue("deleteProject");
		p3.setDescription("Delete projects");
		permissionRepo.save(p3);
		
		Permission p4 = new Permission ();
		p4.setValue("updateProject");
		p4.setDescription("Update projects");
		permissionRepo.save(p4);
		
		Permission p5 = new Permission ();
		p5.setValue("projectAdmin");
		p5.setDescription("Project Admin");
		permissionRepo.save(p5);
		
		Permission p6 = new Permission ();
		p6.setValue("createBundle");
		p6.setDescription("Create bundle");
		permissionRepo.save(p6);
		
		Permission p7 = new Permission ();
		p7.setValue("deleteBundle");
		p7.setDescription("Delete bundle");
		permissionRepo.save(p7);
		
		Permission p8 = new Permission ();
		p8.setValue("readBundle");
		p8.setDescription("Read bundle");
		permissionRepo.save(p8);
		
		Permission p9 = new Permission ();
		p9.setValue("updateBundle");
		p9.setDescription("Update bundle");
		permissionRepo.save(p9);
		
		Permission p10 = new Permission ();
		p10.setValue("bundleAdmin");
		p10.setDescription("Bundle Admin");
		permissionRepo.save(p10);
		
		Permission p11 = new Permission ();
		p11.setValue("createInstance");
		p11.setDescription("Create Instance");
		permissionRepo.save(p11);
		
		Permission p12 = new Permission ();
		p12.setValue("readInstance");
		p12.setDescription("Read Instance");
		permissionRepo.save(p12);
		
		Permission p13 = new Permission ();
		p13.setValue("deleteInstance");
		p13.setDescription("Delete Instance");
		permissionRepo.save(p13);
		
		Permission p14 = new Permission ();
		p14.setValue("adminInstance");
		p14.setDescription("Instance Administrator");
		permissionRepo.save(p14);
		
		Permission p15 = new Permission ();
		p15.setValue("createUser");
		p15.setDescription("Create user");
		permissionRepo.save(p15);
		
		Permission p16 = new Permission ();
		p16.setValue("readUser");
		p16.setDescription("Read user");
		permissionRepo.save(p16);
		
		Permission p17 = new Permission ();
		p17.setValue("deleteUser");
		p17.setDescription("Delete user");
		permissionRepo.save(p17);
		
		Permission p18 = new Permission ();
		p18.setValue("updateUser");
		p18.setDescription("Update user");
		permissionRepo.save(p18);
		
		Permission p19 = new Permission ();
		p19.setValue("userAdmin");
		p19.setDescription("User Admin");
		permissionRepo.save(p19);
		
		Permission p20 = new Permission ();
		p20.setValue("createRole");
		p20.setDescription("Create Role");
		permissionRepo.save(p20);
		
		Permission p21 = new Permission ();
		p21.setValue("deleteRole");
		p21.setDescription("Delete Role");
		permissionRepo.save(p21);
		
		Permission p22 = new Permission ();
		p22.setValue("readRole");
		p22.setDescription("Read Role");
		permissionRepo.save(p22);
		
		Permission p23 = new Permission ();
		p23.setValue("updateRole");
		p23.setDescription("Update Role");
		permissionRepo.save(p23);
		
		Permission p24 = new Permission ();
		p24.setValue("adminRole");
		p24.setDescription("Role Admin");
		permissionRepo.save(p24);
		
		Permission p25 = new Permission ();
		p25.setValue("superAdminRole");
		p25.setDescription("Super Admin");
		permissionRepo.save(p25);
		
		Permission p26 = new Permission ();
		p26.setValue("admin");
		p26.setDescription("Platform Administrator");
		permissionRepo.save(p26);
		
		List<Permission> admin = Arrays.asList(p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, 
				p16, p17, p18, p19, p20, p21, p22, p23, p24, p25, p26);
		
		Role role = new Role ();
		role.setName("ADMINISTRATOR");
		role.setOwner(web);
		roleRepo.save(role);
		web.setAssignedRole(role);
		userRepo.save(web);
		
		RolePermission rp = new RolePermission (web.getFullName(), role, p26);
		rpRepo.save(rp);
		
		try {
			roleCtrl.addPermission(role.getId(), admin, web.getID());
		} catch (RoleControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	@Autowired
	UserController userController;

	@Override
	public Session newSession(Request request, Response response) {
		WebRequest webRequest = (WebRequest) RequestCycle.get().getRequest();
		UserSession session = new UserSession(webRequest);
		Cookie email = webRequest
				.getCookie(ServiceProperties.REMEMBER_ME_EMAIL_COOKIE);
		Cookie password = webRequest
				.getCookie(ServiceProperties.REMEMBER_ME_PASSWORD_COOKIE);
		if (email != null && password != null) {
			try {
				User user = userController.login(email.getValue(),
						password.getValue());

				if (user != null) {
					session.setUser(user);
				}
			} catch (AccountLoginException e) {

			}
		}
		return session;
	}

}

package com.withinet.opaas;

import java.util.Date;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.core.request.mapper.IMapperContext;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.Url;
import org.apache.wicket.request.resource.UrlResourceReference;
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

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.html.BundleIndex;
import com.withinet.opaas.wicket.html.Dashboard;
import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.html.ProjectIndex;
import com.withinet.opaas.wicket.html.Register;
import com.withinet.opaas.wicket.services.SessionProvider;


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
	
	@Autowired
	SessionProvider sessionProvider;

	/**
	 * spring boot main method to build context
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	/**
	 * provides page for default request
	 */
	@Override
	public Class<? extends Page> getHomePage() {
		return ProjectIndex.class;
	}

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
		if (getConfigurationType().equals(RuntimeConfigurationType.DEPLOYMENT)) {
	          getMarkupSettings().setStripWicketTags(true);
	          getMarkupSettings().setStripComments(true);
	          getMarkupSettings().setCompressWhitespace(true);
	    }
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
	}
	
	@Override
    public Session newSession(Request request, Response response) {
        return sessionProvider.createNewSession(request);
    }
	
	
	@Autowired
	BundleController bundleController; 
	
	@Autowired
	ProjectController projectController;
	
	@Autowired
	public void createUser (UserController userController) throws BundleControllerException, UserControllerException, ProjectControllerException {
		User web = new User ();
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("abc@xyz.com");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("abc@xyz.com");
		web.setLocation("United Kingdom");
		userController.createAccount(web);
		
		User pao = new User ();
		pao.setCreated(new Date ());
		pao.setFullName("Pao");
		pao.setPassword("Pao@123");
		pao.setPlatformName("Yes man");
		pao.setStatus("active");
		pao.setEmail("pao@xyz.com");
		pao.setLocation("United Kingdom");
		userController.createAccount(pao);
		
		User ming = new User ();
		ming.setCreated(new Date ());
		ming.setFullName("Ming");
		ming.setPassword("Ming@123");
		ming.setPlatformName("Web");
		ming.setStatus("active");
		ming.setEmail("ming@xyz.com");
		ming.setLocation("United Kingdom");
		userController.createAccount(ming);
		
		userController.addCollaborator(pao, web.getID(), web.getID());
		userController.addCollaborator(ming, web.getID (), web.getID());
		
		Bundle bundle = new Bundle ();
		bundle.setLocation("http:ssdasaa");
		bundle.setOwner(web);
		bundle.setSymbolicName("Hello");
		
		Bundle bundle1 = new Bundle ();
		bundle1.setLocation("http:ssdasaa");
		bundle1.setOwner(web);
		bundle1.setSymbolicName("Hello Two");
		
		bundleController.createBundle(bundle, web.getID());
		bundleController.createBundle(bundle1, web.getID());
		
		Project p = new Project ();
		p.setCreated(new Date());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setUpdated(new Date());
		p.setOwner(web);
		projectController.createProject(p, web.getID());
		Project p1 = new Project ();
		p1.setCreated(new Date());
		p1.setDetails("Hello world2");
		p1.setName("Hello world1");
		p1.setStatus("Active");
		p1.setUpdated(new Date());
		p1.setOwner(web);
		projectController.createProject(p1, web.getID());
		
	}

}

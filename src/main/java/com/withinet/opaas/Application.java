package com.withinet.opaas;

import java.util.Date;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.wicket.html.Dashboard;
import com.withinet.opaas.wicket.html.Login;
import com.withinet.opaas.wicket.html.ProjectSetup;
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
		return ProjectSetup.class;
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
		getApplicationSettings().setUploadProgressUpdatesEnabled(true);
		getComponentInstantiationListeners().add(
				new SpringComponentInjector(this, applicationContext));
		mountPage("/login", Login.class);
		mountPage("/register", Register.class);
		mountPage("/dashboard", Dashboard.class);
	}
	
	@Override
    public Session newSession(Request request, Response response) {
        return sessionProvider.createNewSession(request);
    }
	
	@Autowired
	public void createUser (UserRepository userRepository) {
		User web = new User ();
		web.setCreated(new Date ());
		web.setFullName("Web");
		web.setPassword("Folarin@123");
		web.setPlatformName("Web");
		web.setStatus("active");
		web.setEmail("abc@xyz.com");
		web.setLocation("United Kingdom");
		userRepository.save(web);
	}

}

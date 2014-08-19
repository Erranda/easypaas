package com.withinet.opaas;

import java.util.Date;

import javax.servlet.http.Cookie;

import org.apache.wicket.Page;
import org.apache.wicket.RuntimeConfigurationType;
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

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.ServiceProperties;
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
	public static void main(String[] args) {
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
			getApplicationSettings().setUploadProgressUpdatesEnabled(true);
			getRequestCycleSettings().setRenderStrategy(
		            IRequestCycleSettings.RenderStrategy.ONE_PASS_RENDER); 
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

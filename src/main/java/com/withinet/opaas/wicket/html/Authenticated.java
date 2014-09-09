/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.WicketApplication;
import com.withinet.opaas.controller.Authorizer;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.wicket.services.UserSession;
import static com.withinet.opaas.controller.common.ServiceProperties.*;

/**
 * @author Folarin
 *
 */
public abstract class Authenticated extends WebPage {

	/**
	 * 
	 */
	private Component feedback = null;
	
	@SpringBean
	Authorizer authorizer;
	
	public Authenticated() {
		if(UserSession.get().userNotLoggedIn()) throw new RestartResponseAtInterceptPageException(
				Login.class);
		setVersioned (false);
		if (UserSession.get().getUser().getStatus().equals("Disabled"))
			throw new RestartResponseAtInterceptPageException(
					Login.class);
		feedback = new FeedbackPanel ("feedback");
		feedback.setEscapeModelStrings(false);
		feedback.setOutputMarkupId(true);
		
		add (feedback);
		BookmarkablePageLink homeLink = new BookmarkablePageLink ("appNameLink", WicketApplication.get().getHomePage(), null);
		homeLink.add(new Label ("appName", "Withinet OSGi Cloud"));
		add (homeLink);
		
		try {
			
		} catch (ControllerSecurityException e) {
			
		}
		BookmarkablePageLink home = new BookmarkablePageLink ("home", WicketApplication.get().getHomePage(), null);
		add (home);
		
		Long uid = UserSession.get().getUser().getID();
		BookmarkablePageLink<ProjectIndex> projects = new BookmarkablePageLink <ProjectIndex> ("projects", ProjectIndex.class, null);
		add (projects);

		try {
			List<String> composite = new ArrayList<String> (CREATE_PROJECT);
			composite.addAll(READ_PROJECT);
			composite.addAll(SYSTEM_ADMIN);
			authorizer.authorize(composite, uid);
		} catch (ControllerSecurityException e) {
			projects.setVisible(false);
		}
		
		BookmarkablePageLink<InstanceIndex> instances = new BookmarkablePageLink <InstanceIndex> ("instances", InstanceIndex.class, null);
		add (instances);
		
		try {
			List<String> composite = new ArrayList<String> (CREATE_INSTANCE);
			composite.addAll(READ_INSTANCE);
			composite.addAll(SYSTEM_ADMIN);
			authorizer.authorize(composite, uid);
		} catch (ControllerSecurityException e) {
			instances.setVisible(false);
		}
		
		BookmarkablePageLink <BundleIndex> bundles = new BookmarkablePageLink <BundleIndex> ("bundles", BundleIndex.class, null);
		add (bundles);
		
		try {
			List<String> composite = new ArrayList<String> (CREATE_PROJECT);
			composite.addAll(READ_PROJECT);
			composite.addAll(CREATE_BUNDLE);
			composite.addAll(READ_BUNDLE);
			composite.addAll(SYSTEM_ADMIN);
			authorizer.authorize(composite, uid);
		} catch (ControllerSecurityException e) {
			bundles.setVisible(false);
		}
		
		BookmarkablePageLink team = new BookmarkablePageLink <TeamIndex> ("team", TeamIndex.class, null);
		add (team);
		
		try {
			authorizer.authorize(SYSTEM_ADMIN, uid);
		} catch (ControllerSecurityException e) {
			team.setVisible(false);
		}
		add (new BookmarkablePageLink <AccountIndex> ("account", AccountIndex.class, null));
		add (new Label ("name", UserSession.get().getUser().getFullName()));
		add (new BookmarkablePageLink <Logout> ("logout", Logout.class, null));
	}

	/**
	 * @param model
	 */
	public Authenticated(IModel<?> model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param parameters
	 */
	public Authenticated(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	protected void setPageTitle (IModel model) {
		get ("pageTitle").setDefaultModel (model);
	}
	
	public Component getFeedbackPanel () {
		return feedback;
	}

}


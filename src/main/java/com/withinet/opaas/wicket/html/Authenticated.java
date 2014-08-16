/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.Application;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
public abstract class Authenticated extends WebPage {

	/**
	 * 
	 */
	private Component feedback = null;
	
	public Authenticated() {
		String userName = UserSession.get().getUser().getFullName();
		feedback = new FeedbackPanel ("feedback");
		feedback.setEscapeModelStrings(false);
		feedback.setOutputMarkupId(true);
		
		add (feedback);
		BookmarkablePageLink homeLink = new BookmarkablePageLink ("appNameLink", Application.get().getHomePage(), null);
		homeLink.add(new Label ("appName", "Withinet OSGi Cloud"));
		add (homeLink);
		
		BookmarkablePageLink home = new BookmarkablePageLink ("home", Application.get().getHomePage(), null);
		add (home);
		add (new BookmarkablePageLink <ProjectIndex> ("projects", ProjectIndex.class, null));
		add (new BookmarkablePageLink <InstanceIndex> ("instances", InstanceIndex.class, null));
		add (new BookmarkablePageLink <BundleIndex> ("bundles", BundleIndex.class, null));
		add (new BookmarkablePageLink <TeamIndex> ("team", TeamIndex.class, null));
		add (new BookmarkablePageLink <AccountIndex> ("account", AccountIndex.class, null));
		add (new Label ("name", userName));
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

package com.withinet.opaas.wicket.html;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.wicket.services.UserSession;

public abstract class Base extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2464320241644232730L;

	public Base() {
		super ();
		add (new BookmarkablePageLink<String>("register", Register.class));
	}
	
	public Base (PageParameters pageParameters) {
		super (pageParameters);
	}
	

}

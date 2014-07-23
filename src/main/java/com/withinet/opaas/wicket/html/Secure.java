package com.withinet.opaas.wicket.html;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

public abstract class Secure extends WebPage {
	
	public Secure() {
		if(UserSession.get().userNotLoggedIn()) throw new RestartResponseAtInterceptPageException(
				Login.class);
	}

	public Secure(IModel<?> model) {
		super(model);
	}

	public Secure(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}
}

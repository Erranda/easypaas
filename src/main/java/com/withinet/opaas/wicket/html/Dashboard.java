package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.wicket.services.UserSession;

public class Dashboard extends Secure {

	public Dashboard() {
		
	}

	public Dashboard(IModel<?> model) {
		super(model);
	}

	public Dashboard(PageParameters parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

}

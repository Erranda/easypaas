package com.withinet.opaas.wicket.services;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.http.WebRequest;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.model.domain.Role;
import com.withinet.opaas.model.domain.RolePermission;
import com.withinet.opaas.model.domain.User;

public class UserSession extends AuthenticatedWebSession  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1713951920830505354L;
	
	private User user;
	
	public static FeedbackMessage message = null;
	
    public UserSession(Request request) {
        super(request);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean userLoggedIn() {
        return user != null;
    }

    public boolean userNotLoggedIn() {
        return user == null;
    }

    public static UserSession get() {
        return (UserSession) WebSession.get();
    }
    
    public void logout (Request request, Response response) {
    	user = null;  
    }

	@Override
	public boolean authenticate(String arg0, String arg1) {
		return user != null;
	}

	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		Role assigned = user.getAssignedRole();
		if (assigned != null) {
			for (RolePermission rp : assigned.getRolePermissions()) {
				roles.add(rp.getPermission().getValue());
			}
		}
		return roles;
	}
}
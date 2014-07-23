package com.withinet.opaas.wicket.services;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;

import com.withinet.opaas.domain.User;

public class UserSession extends WebSession {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1713951920830505354L;
	
	private User user;

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
}
package com.withinet.opaas.wicket.services;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.User;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

import javax.servlet.http.Cookie;

@Service
public class SessionProvider {
    
    @Autowired
    private UserController accountService;
    
    @Autowired
    private CookieService cookieService;

    public WebSession createNewSession(Request request) {
        UserSession session = new UserSession(request);

        Cookie loginCookie = cookieService.loadCookie(request, REMEMBER_ME_EMAIL_COOKIE);
        Cookie passwordCookie = cookieService.loadCookie(request, REMEMBER_ME_PASSWORD_COOKIE);

        if(loginCookie != null && passwordCookie != null) {
            User user;
			
            try {
				user = accountService.login(loginCookie.getValue(), passwordCookie.getValue());
				
				if(user != null) {
	                session.setUser(user);
	            }
			} catch (AccountLoginException e) {
				
			}            
        }

        return session;
    }
}
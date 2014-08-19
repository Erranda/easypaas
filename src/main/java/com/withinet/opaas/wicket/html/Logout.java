/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import static com.withinet.opaas.controller.common.ServiceProperties.*;
import com.withinet.opaas.wicket.services.UserSession;
import org.apache.wicket.util.cookies.CookieUtils;
/**
 * Sign out page
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class Logout extends Stateless {
	
	public Logout() {
		CookieUtils util = new CookieUtils ();
		util.remove(REMEMBER_ME_EMAIL_COOKIE);
		util.remove(REMEMBER_ME_PASSWORD_COOKIE);
		
		UserSession.get().logout(getRequestCycle().getRequest(), getRequestCycle ().getResponse());
		throw new RestartResponseAtInterceptPageException(getApplication()
				.getHomePage());
	}
}

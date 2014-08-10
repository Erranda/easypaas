/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * Sign out page
 * 
 * @author Folarin Omotoriogun
 * @since August 23, 2013
 */
public class Logout extends Stateless {
	
	public Logout() {
		getSession().invalidate();
		throw new RestartResponseAtInterceptPageException(getApplication()
				.getHomePage());
	}
}

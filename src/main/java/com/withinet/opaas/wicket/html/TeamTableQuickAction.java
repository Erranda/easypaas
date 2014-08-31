/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;

/**
 * @author Folarin
 *
 */
public class TeamTableQuickAction extends Panel {

	public TeamTableQuickAction(String id, WebMarkupContainer updateUserLink, WebMarkupContainer deleteUserLink, 
			WebMarkupContainer flushUserLink, WebMarkupContainer resetPassword) {
		super(id);
		add (updateUserLink);
		add (deleteUserLink);
		add (flushUserLink);
		add (resetPassword);
	}

}

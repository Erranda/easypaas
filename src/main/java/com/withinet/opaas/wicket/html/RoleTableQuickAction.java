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
public class RoleTableQuickAction extends Panel {

	public RoleTableQuickAction(String id, WebMarkupContainer updateLink, WebMarkupContainer deleteLink) {
		super(id);
		add (updateLink);
		add (deleteLink);
	}

}

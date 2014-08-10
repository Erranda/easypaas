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
public class InstanceTableQuickAction extends Panel {

	public InstanceTableQuickAction(String id, WebMarkupContainer stopInstanceLink, WebMarkupContainer flushInstanceLink, 
			WebMarkupContainer startInstanceLink, WebMarkupContainer cpanelLink) {
		super(id);
		add (stopInstanceLink);
		add (flushInstanceLink);
		add (startInstanceLink);
		add (cpanelLink);
	}

}

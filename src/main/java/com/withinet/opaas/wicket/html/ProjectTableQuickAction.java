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
public class ProjectTableQuickAction extends Panel {

	public ProjectTableQuickAction(String id, WebMarkupContainer startInstanceLink, WebMarkupContainer deleteProject, WebMarkupContainer viewProject) {
		super(id);
		add (startInstanceLink);
		add (viewProject);
		add (deleteProject);
	}

}

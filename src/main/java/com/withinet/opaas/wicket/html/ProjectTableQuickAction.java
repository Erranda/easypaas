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

	public ProjectTableQuickAction(String id, WebMarkupContainer startInstanceLink, 
			WebMarkupContainer deleteProject, WebMarkupContainer viewProject,
			WebMarkupContainer viewBundles, WebMarkupContainer viewInstances) {
		super(id);
		add (startInstanceLink);
		add (viewProject);
		add (deleteProject);
		add (viewBundles);
		add (viewInstances);
	}

}

/**
 * 
 */
package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

import com.withinet.opaas.controller.ProjectController;

/**
 * @author Folarin
 *
 */
public class ProjectStatWidget extends Panel {
	
	/**
	 * @param id
	 */
	public ProjectStatWidget(String id, int numberOfProjects) {
		super(id);
		setVersioned(false);
		add (new Label ("projects-size", numberOfProjects));
		// TODO Auto-generated constructor stub
	}

}

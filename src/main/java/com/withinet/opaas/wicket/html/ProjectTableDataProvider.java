/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * 
 * @author Folarin
 *
 */
public class ProjectTableDataProvider extends SortableDataProvider<Project, String> {
	
	@SpringBean
	private static ProjectController projectController;
	
	private final long USER_ID = UserSession.get().getUser().getID();
	
	public ProjectTableDataProvider () {
		
	}
	
	private List<Project> userProjects = new ArrayList<Project> ();
	
	@Override
	public Iterator<? extends Project> iterator(long arg0, long arg1) {
		return userProjects.subList((int) arg0, Math.min((int) userProjects.size(), (int) arg1)).iterator();
	}

	@Override
	public IModel<Project> model(Project arg0) {
		return Model.of(arg0);
	}

	@Override
	public long size() {
		try {
			userProjects = projectController.listCreatedProjectsByOwner(USER_ID, USER_ID);
		} catch (ProjectControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return userProjects.size();
	}

}

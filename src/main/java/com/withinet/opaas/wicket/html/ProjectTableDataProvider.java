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

import com.withinet.opaas.model.domain.Project;

/**
 * 
 * @author Folarin
 *
 */
public class ProjectTableDataProvider extends SortableDataProvider<Project, String> {
	
	public ProjectTableDataProvider (Long userId) {
		//Initialize projects for user
		Project p = new Project ();
		p.setCreated(new Date());
		p.setDetails("Hello world");
		p.setName("Hello world");
		p.setStatus("Active");
		p.setUpdated(new Date());
		p.setID(1L);
		userProjects.add(p);
		Project p1 = new Project ();
		p1.setCreated(new Date());
		p1.setDetails("Hello world2");
		p1.setName("Hello world1");
		p1.setStatus("Active");
		p1.setUpdated(new Date());
		p1.setID(2L);
		userProjects.add(p1);
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
		return userProjects.size();
	}

}

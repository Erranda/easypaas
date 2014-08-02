/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.domain.Project;

/**
 * @author Folarin
 *
 */
public class ProjectViewWidget extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private transient SortableDataProvider<Project, String> provider;
	private final List<IColumn<Project, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<Project, String>>());
	private int resultSize = 20;
	/**
	 * @param id
	 * @param userId2 
	 * @param projectController 
	 * @throws ProjectControllerException 
	 */
	public ProjectViewWidget(String id, ProjectController projectController, Long userId) throws ProjectControllerException {
		super(id);
		provider = new ProjectTableDataProvider (projectController, userId);
		columns.add(new PropertyColumn<Project, String>(
				new Model<String>("Name"), "name"));
		columns.add(new PropertyColumn<Project, String>(
				new Model<String>("Created"), "created"));
		columns.add(new PropertyColumn<Project, String>(
				new Model<String>("Status"), "status"));
		columns.add(new AbstractColumn<Project, String>(
				new Model<String>("Quick Action")) {

			private static final long serialVersionUID = 1L;

			@Override
			public void populateItem(Item<ICellPopulator<Project>> item,
					String componentId, final IModel<Project> model) {
				// TODO Auto-generated method stub
				BookmarkablePageLink<InstanceLauncher> startInstance = new BookmarkablePageLink<InstanceLauncher> ("start-instance", InstanceLauncher.class, setStartInstanceLinkParameters (model.getObject()));
				if (model.getObject().getStatus().equals("Disabled"))
					startInstance.setVisible(false);
			
				BookmarkablePageLink<ProjectAction> viewProject = new BookmarkablePageLink<ProjectAction> ("view-project", ProjectAction.class, setViewProjectLinkParameters (model.getObject()));
				BookmarkablePageLink<ProjectAction> deleteProject = new BookmarkablePageLink<ProjectAction> ("delete-project", ProjectAction.class, setDeleteProjectLinkParameters (model.getObject()));
				ProjectTableQuickAction button = new ProjectTableQuickAction (componentId, startInstance, viewProject, deleteProject);
				item.add(button);
			}
			
			private PageParameters setStartInstanceLinkParameters (Project project) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", project.getID());
				return linkParameters;
			}
			
			private PageParameters setViewProjectLinkParameters (Project project) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("id", project.getID());
				return linkParameters;
			}
			
			private PageParameters setDeleteProjectLinkParameters(Project project) {
					PageParameters linkParameters = new PageParameters();
					linkParameters.add("id", project.getID());
					return linkParameters;
			}
		});
		DataTable<Project, String> dataTable = new DefaultDataTable <Project, String> ("project-view-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
	

}

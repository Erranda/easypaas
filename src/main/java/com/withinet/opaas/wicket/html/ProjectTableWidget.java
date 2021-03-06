/**
 * 
 */
package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxLink;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.beans.factory.annotation.Autowired;

import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

/**
 * @author Folarin
 *
 */
@AuthorizeAction(action="RENDER", roles={"authorizedProject", "projectAdmin"})
public class ProjectTableWidget extends Panel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6426148890485840838L;
	private SortableDataProvider<Project, String> provider = new ProjectTableDataProvider ();
	private final List<IColumn<Project, String>> columns = Collections.synchronizedList(new ArrayList<IColumn<Project, String>>());
	private int resultSize = 20;
	private Long selected = null;
	private boolean authorized = false;
	
	private String projectName = null;
	private String containerChoice = "Felix";
	
	Form<Void> setupForm = null;
	
	@SpringBean
	private InstanceController instanceController;
	
	@SpringBean
	private ProjectController projectController;
	/**
	 * @param id
	 */
	public ProjectTableWidget(String id) {
		super(id);
		
	}
	
	public ProjectTableWidget(String id, Long iid) {
		super(id);
		
	}
	
	public ProjectTableWidget(String id, boolean authorized) {
		super(id);
		this.authorized = authorized;
		setVisible (authorized);
	}

	@Override
	public void onInitialize () {
		super.onInitialize();
		addStartInstanceForm ();
		columns.add(new PropertyColumn<Project, String>(
				new Model<String>("Name"), "name"));
		columns.add(new PropertyColumn<Project, String>(
				new Model<String>("Created By"), "owner.fullName"));
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
				IndicatingAjaxLink startInstance = new IndicatingAjaxLink("start-instance"){                                                                                                                                                                                                                                                                                                  
					private static final long serialVersionUID = 1L;                                                                                                       

					@Override                                                                                                                                              
					public void onClick(AjaxRequestTarget target) {                                                                                                        
						 selected = model.getObject().getID();
						 projectName = model.getObject().getName();
						 target.add(setupForm);
						 target.appendJavaScript("showModal ()"); 
					}                                                                                                                                                      
				};
				
				ConfirmationLink<String>  deleteProject = new ConfirmationLink<String> ("delete-project", "All instances will be deleted as well. Continue?") {

					@Override
					public void onClick(AjaxRequestTarget arg0) {
						try {
							projectController.deleteProject(model.getObject().getID(), UserSession.get().getUser().getID());
							getPage().info (model.getObject().getName() + " deleted");
							setResponsePage (getPage());
						} catch (ProjectControllerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							error (e.getMessage());
						}
					}
				};
				Long uid = UserSession.get().getUser().getID();
				if (model.getObject().getOwner().getID() != uid && model.getObject().getOwner().getAdministrator().getID() != uid) {
					//You can only delete a project you created or you are an administrator to the owner
					deleteProject.setVisible(false);
				}
					
				BookmarkablePageLink<ProjectIndex> viewBundles = new BookmarkablePageLink<ProjectIndex> ("view-bundles", BundleIndex.class, setBundlesLinkParameters (model.getObject()));
				BookmarkablePageLink<ProjectIndex> viewInstances = new BookmarkablePageLink<ProjectIndex> ("view-instances", InstanceIndex.class, setInstancesLinkParameters (model.getObject()));
				ProjectTableQuickAction button = new ProjectTableQuickAction (componentId, startInstance, deleteProject, viewBundles, viewInstances);
				item.add(button);
			}
			private PageParameters setInstancesLinkParameters(Project project) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("pid", project.getID());
				return linkParameters;
			}
			private PageParameters setBundlesLinkParameters(Project project) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("pid", project.getID());
				return linkParameters;
			}
			
			private PageParameters setViewProjectLinkParameters (Project project) {
				PageParameters linkParameters = new PageParameters();
				linkParameters.add("pid", project.getID());
				return linkParameters;
			}
		});
		DataTable<Project, String> dataTable = new DefaultDataTable <Project, String> ("project-view-table", columns, provider, resultSize);
		add (dataTable);
		
	}
	
	private void addStartInstanceForm () {
		setupForm = new Form<Void>("startInstance");
		add(setupForm);

		final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
		feedback.setOutputMarkupPlaceholderTag(true);
		setupForm.add(feedback);
		
		 
	    TextField wicketName = new TextField("name", new PropertyModel<String>(this, "projectName"));
	    setupForm.add(wicketName);
		
		List<String> containerChoices = new ArrayList<String> ();
		containerChoices.add("Felix");
		containerChoices.add("Equinox");
		
		DropDownChoice<String> wicketContainerChoices = new DropDownChoice<String>("containerChoice" ,new PropertyModel<String>(this,"containerChoice"), containerChoices);
		setupForm.add (wicketContainerChoices);
		
		setupForm.add(new IndicatingAjaxButton("submit", setupForm) {
	            /**
				 * 
				 */
				private static final long serialVersionUID = -6415555183396288060L;

				@Override
	            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
				{

					Instance instance = new Instance ();
					try {
						Long uid = UserSession.get().getUser().getID();
						Long start = System.currentTimeMillis();
						Double startD = start.doubleValue();
						instance.setContainerType(containerChoice);
						instanceController.createInstance(instance, selected, uid, uid);
						Long end = System.currentTimeMillis();
						Double endD = end.doubleValue();
						info("Instance created in " + (endD - startD)/1000 + " seconds for " + UserSession.get().getUser().getFullName() + " <a target=\"_blank\" style=\"color:#ff0\" href=\"" + instance.getCpanelUrl() + "\"> Go to Cpanel</a>");
					} catch (InstanceControllerException e) {
						error ("Error: " + e.getMessage());
						e.printStackTrace();
						setResponsePage (getPage());
					}
					
					setResponsePage (getPage());
	            }

	            @Override
	            protected void onError(AjaxRequestTarget target, Form<?> form)
	            {
	            	target.add(feedback);
	            }
	        });
	}
	private class ProjectTableDataProvider extends SortableDataProvider<Project, String> {
		
	
		
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
				long USER_ID = UserSession.get().getUser().getID();
				if (authorized)
					userProjects = projectController.listAllProjects(USER_ID, USER_ID);					
			} catch (ProjectControllerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				error (e.getMessage());
			} 
			return userProjects.size();
		}

	}
}

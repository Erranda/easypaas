package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.Application;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

public class InstanceSetupWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258781378426675695L;

	private String projectName = "";

	private String containerChoice = "Felix";
	
	private ArrayList<String> selectedTeam = new ArrayList<String> ();
	 
	private Map<String, User> teamModel = new HashMap<String, User> ();
	
	private Map<String, Project> projectsModel = new HashMap<String, Project> ();

	private List<String> containerTypes = null;
	
	private List<String> userProjects = null;
	
	@SpringBean
	private ProjectController projectController;
	
	@SpringBean
	private UserController userController;
	
	@SpringBean
	private InstanceController instanceController;

	private String status;

	public InstanceSetupWidget(String id) throws ProjectControllerException, UserControllerException {
		super (id);Long uid = UserSession.get().getUser ().getID();
		
		containerTypes = new ArrayList<String>();
		containerTypes.add("Felix");
		containerTypes.add("Knopflerfish");
		containerTypes.add("Equinox");
		containerTypes.add("Concierge");

		Form<Void> setupForm = new Form<Void>("form");
		add(setupForm);

		setupForm.add(new FeedbackPanel("feedback"));

		DropDownChoice<String> containerType = new DropDownChoice<String>(
				"containerChoice", new PropertyModel<String>(this,
						"containerChoice"), containerTypes);
		setupForm.add(containerType);
		
		DropDownChoice<String> userProject = new DropDownChoice<String>(
				"userProjects", new PropertyModel<String>(this,
						"projectName"), listProjects (uid));
		userProject.setRequired(true);
		setupForm.add(userProject);
		
		ListMultipleChoice<String> teamMembers = new ListMultipleChoice<String>(
				"team", new Model(selectedTeam), listTeam(uid));
		teamMembers.setRequired(true);
		setupForm.add(teamMembers);

		setupForm.add(new IndicatingAjaxButton("submit", setupForm) {
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				for (String user : selectedTeam) {
					if (teamModel.get(user) != null) {
						User thisUser = teamModel.get(user);
						Instance instance = new Instance ();
						try {
							Long uid = UserSession.get().getUser().getID();
							instance.setContainerType(containerChoice);
							instanceController.createInstance(instance, projectsModel.get(projectName).getID(), thisUser.getID(), uid);
							info("Instance is ready for " + thisUser.getFullName() + " <a style=\"color:#ff0\" href=\"" + instance.getCpanelUrl() + "\"> Go to Cpanel</a>");
							target.add(((Authenticated) getPage ()).getFeedbackPanel());
						} catch (InstanceControllerException e) {
							error (e.getMessage());
							e.printStackTrace();
							setResponsePage (getPage());
						}
						
						setResponsePage (getPage());
					}
						
				}
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(((Authenticated) getPage ()).getFeedbackPanel());
			}
		});
	
	}
	
	private List<String> listProjects (Long uid) throws ProjectControllerException {
		userProjects = new ArrayList<String>();
		for (Project project : projectController.listCreatedProjectsByOwner(uid, uid)){
			userProjects.add(project.getName());
			projectsModel.put(project.getName(), project);
		}
		return userProjects;
	}
	
	private List<String> listTeam (Long uid) throws UserControllerException {
		List<String> initTeam = new ArrayList<String> ();
		for (User user : userController.listTeamMembers(uid, uid)) {
			String key = user.getFullName() + " [ " + user.getEmail() + " ]";
			initTeam.add(key);
			teamModel.put(key, user);
		}
		User user = UserSession.get().getUser();
		String key = user.getFullName() + " [ " + user.getEmail() + " ]";
		initTeam.add(key);
		teamModel.put(key, user);
		return initTeam;
	}		

}

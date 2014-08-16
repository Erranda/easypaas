package com.withinet.opaas.wicket.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.RestartResponseException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.BundleConflictException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.system.BundleInstaller;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

public class ProjectSetupWidget extends Panel {

	private static final long serialVersionUID = 2258781378426675695L;

	private String name;

	private FileUploadField wicketFileUploadField;
	
	private ArrayList<String> projectTeam = new ArrayList<String>();

	private HashMap<String, User> thisTeamModel = new HashMap<String, User>();

	private List<User> projectTeamSelected = new ArrayList<User>();

	private ArrayList<String> projectFileBundlesSelected = new ArrayList<String>();

	private HashMap<String, Bundle> thisBundleModel = new HashMap<String, Bundle>();

	private FileUpload upload;

	private Boolean active = true;

	private Project thisProject = null;

	private List<Bundle> projectFileBundles = new ArrayList<Bundle>();

	@SpringBean
	private FileLocationGenerator fileMan;

	@SpringBean
	private BundleInstaller bundleInstaller;

	@SpringBean
	private BundleController bundleController;

	@SpringBean
	private ProjectController projectController;

	@SpringBean
	private UserController accountController;

	public ProjectSetupWidget(String id) throws UserControllerException, BundleControllerException {
		super (id);
		Form<Void> setupForm = new Form<Void>("form");
		add(setupForm);

		final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
		feedback.setOutputMarkupPlaceholderTag(true);
		setupForm.add(feedback);

		RequiredTextField<String> wicketName = new RequiredTextField<String>(
				"name", new PropertyModel<String>(this, "name"));
		wicketName.add(StringValidator.minimumLength(2));
		wicketName.setLabel(new ResourceModel("label.name"));
		setupForm.add(wicketName);

		ListMultipleChoice<String> bundles = new ListMultipleChoice<String>(
				"bundles", new Model(projectFileBundlesSelected), listBundles());
		setupForm.add(bundles);

		ListMultipleChoice<String> team = new ListMultipleChoice<String>(
				"team", new Model(projectTeam), listTeam());
		setupForm.add(team);

		wicketFileUploadField = new FileUploadField("file");
		wicketFileUploadField.setRequired(false);
		setupForm.add(wicketFileUploadField);

		setupForm.setMaxSize(Bytes.megabytes(10000));
		UploadProgressBar progress = new UploadProgressBar("progress",
				setupForm, wicketFileUploadField);
		setupForm.add(progress);
		setupForm.add(new CheckBox("active", new PropertyModel<Boolean>(this,
				"active"))); // this line
		setupForm.add(new IndicatingAjaxButton ("submit", setupForm){
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				Long start = System.currentTimeMillis();
				Double startD = start.doubleValue();
				upload = wicketFileUploadField.getFileUpload();

				User thisUser = UserSession.get().getUser();
				Long userId = UserSession.get().getUser().getID();
				try {
					if (upload != null) {
						String fileName = upload.getClientFileName().toLowerCase()
								.trim();
						String extension = FilenameUtils.getExtension(fileName);
						extension = extension.toLowerCase().trim();
						if (extension.equals("xml") || extension.equals("jar")
								|| extension.equals("zip")) {
							processUpload();
							try {
								Project project = createProject();
								Long end = System.currentTimeMillis();
								Double endD = end.doubleValue();
								info ("Project " + name + " created in " + (endD - startD)/1000 + " seconds \n");
								setResponsePage (this.getPage());
							} catch (ProjectControllerException e) {
								e.printStackTrace();
								error (e.getMessage());
							}
						} else {
							error("Only .zip, .xml, and .jar extensions allowed");
							//deleteProject();
						}
					} else {
						Project project = createProject();
						Long end = System.currentTimeMillis();
						Double endD = end.doubleValue();
						info ("Project " + name + " created in " + (endD - startD)/1000 + " seconds with ");
						setResponsePage (this.getPage());
					}
				} catch (ProjectControllerException e) {
					e.printStackTrace();
					error (e.getMessage());
				} catch (RuntimeException e) {
					e.printStackTrace();
					error (e.getMessage());
				}
				target.add(feedback);
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            	target.add(feedback);
            }
		});

	}
	
	private List<Bundle> processUpload() {
		// If project creation succeeds, check for file upload
		File thisFile = new File((fileMan.getTempDirectory ()).getAbsolutePath() + "/"
				+ upload.getClientFileName());
		Long userId = UserSession.get().getUser().getID();
		try {
			if (thisFile.createNewFile()){
			upload.writeTo(thisFile);
			File userLibDirectory = fileMan.getUserLibrary(userId);
			System.out.println (userLibDirectory.getAbsolutePath());
			List<String> list = new ArrayList<String>();
			list.add(thisFile.getAbsolutePath());
			projectFileBundles = bundleInstaller.installBundles(list,
					userLibDirectory.getAbsolutePath());
				for (int i = 0; i < projectFileBundles.size(); i++) {
					try {
						Bundle bundle = bundleController.createBundle(projectFileBundles.get(i), userId);
						if (bundle != null) 
							projectFileBundles.set(i, bundle);
					}  catch (BundleControllerException e) {
						if (e instanceof BundleConflictException) {
							try {
								projectFileBundles.set(i, bundleController.readBundleByName(projectFileBundles.get(i).getSymbolicName(), userId));
								info ("Some bundles have been reused from your library due to name conflict");
								info (e.getMessage());
							} catch (BundleControllerException e1) {
								throw new RuntimeException (e1.getMessage());
							}
						}
					}
				}
			} else {
				error("Could not process your file at this time");
			}
		} catch (IOException e) {
			e.printStackTrace();
			error(e.getMessage());
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			error(e.getMessage());
		}
		return projectFileBundles;
	}

	private List<Bundle> processSelectedBundles() {
		List<Bundle> bundles = new ArrayList<Bundle>();
		for (String bundle : projectFileBundlesSelected)
			if (thisBundleModel.get(bundle) != null)
				bundles.add(thisBundleModel.get(bundle));
		return bundles;

	}

	private List<String> listBundles() throws BundleControllerException {
		List<String> bundles = new ArrayList<String>();
		Long uid = UserSession.get().getUser().getID();
		
		List<Bundle> bundlesRaw = bundleController.listBundlesByOwner(uid, uid);
		for (Bundle bundle : bundlesRaw) {
			bundles.add(bundle.getSymbolicName());
			thisBundleModel.put(bundle.getSymbolicName(), bundle);
		}
		if (bundles.size() == 0)
			bundles.add("Bundle Library Empty");
		return bundles;
	}

	private List<String> listTeam() throws UserControllerException {
		Long uid = UserSession.get().getUser().getID();
		
		List<User> collaborators = accountController.listCollaborators(uid, uid);
		ArrayList<String> formList = new ArrayList<String>();
		for (User user : collaborators) {
			String formKey = user.getFullName() + " [" + user.getEmail() + "]";
			formList.add(formKey);
			thisTeamModel.put(formKey, user);
		}
		if (formList.size () == 0)
			formList.add("Your team list is empty");
		Collections.sort(formList);
		return formList;
	}

	private void deleteProject() {
		Long userId = UserSession.get().getUser().getID();
		try {
			projectController.deleteProject(thisProject.getID(), userId);
		} catch (ProjectControllerException e) {
			error("An error has occured");
		}
	}

	private Project createProject() throws ProjectControllerException {
		// Create project first
		Project thisProject = new Project();
		thisProject.setName(name);
		thisProject.setStatus(active ? "Active" : "Disabled");
		Long userId = UserSession.get().getUser().getID();
		thisProject = projectController.createProject(thisProject, userId);
		// Add project team

		for (Bundle bundle : processSelectedBundles()) {
			projectController.addBundle(bundle, thisProject.getID(), userId);
		}
		
		for (Bundle bundle : projectFileBundles) {
			projectController.addBundle(bundle, thisProject.getID(), userId);
		}
		
		for (User user : processSelectedTeam()) {
			projectController.addCollaborator(user, thisProject.getID(), userId);
		}

		return projectController.readProjectById(thisProject.getID(), userId);
	}

	private List<User> processSelectedTeam() {
		List<User> projectMembers = new ArrayList<User>();
		for (String memberKey : projectTeam) {
			if (thisTeamModel.get(memberKey) != null)
				projectMembers.add(thisTeamModel.get(memberKey));
		}
		return projectMembers;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}

}

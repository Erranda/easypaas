package com.withinet.opaas.wicket.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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
import com.withinet.opaas.controller.system.BundleFileInstaller;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.wicket.services.UserSession;

public class ProjectSetup extends Secure {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258781378426675695L;

	private String name;

	private FileUploadField wicketFileUploadField;
	
	private ArrayList<String> projectTeam = new ArrayList<String>();

	private HashMap<String, User> thisTeamModel = new HashMap<String, User>();

	private List<User> projectTeamSelected = new ArrayList<User>();

	private Long userId = UserSession.get().getUser().getID();

	private ArrayList<String> projectFileBundlesSelected = new ArrayList<String>();

	private HashMap<String, Bundle> thisBundleModel = new HashMap<String, Bundle>();

	private FileUpload upload;

	private Boolean active = true;

	private User thisUser = UserSession.get().getUser();

	private Project thisProject = null;

	private boolean noError = true;

	private List<Bundle> projectFileBundles = new ArrayList<Bundle>();

	@SpringBean
	private FileLocationGenerator fileMan;

	@SpringBean
	private BundleFileInstaller bundleInstaller;

	@SpringBean
	private BundleController bundleController;

	@SpringBean
	private ProjectController projectController;

	@SpringBean
	private UserController accountController;

	public ProjectSetup() throws UserControllerException {
		// Set the user
		thisUser = accountController.readAccount(userId, userId);

		Form<Void> setupForm = new Form<Void>("form") {
			@Override
			protected void onSubmit() {
				Long start = System.currentTimeMillis();
				Double startD = start.doubleValue();
				upload = wicketFileUploadField.getFileUpload();
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
								info ("Project " + name + " created in " + (endD - startD)/1000 + " seconds");
								info ("Cached bundles: " + project.getProjectBundles().size());
								info ("Team Size: " + project.getProjectTeam().size());
							} catch (ProjectControllerException e) {
								e.printStackTrace();
								error (e.getMessage());
							}
						} else {
							error("Only .zip, .xml, and .jar extensions allowed");
							deleteProject();
						}
					} else {
						Project project = createProject();
						Long end = System.currentTimeMillis();
						Double endD = end.doubleValue();
						info ("Project " + name + " created in " + (endD - startD)/1000 + " seconds");
						info ("Cached bundles: " + project.getProjectBundles().size());
						info ("Team Size: " + project.getProjectTeam().size());
					}
				} catch (ProjectControllerException e) {
					e.printStackTrace();
					error (e.getMessage());
				} catch (RuntimeException e) {
					e.printStackTrace();
					error (e.getMessage());
				}
				
			}
		};
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
		wicketFileUploadField.setRequired(true);
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
				target.add(feedback);
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            	target.add(feedback);
            }
		});

	}

	public ProjectSetup(IModel<?> model) {
		super(model);
	}

	public ProjectSetup(PageParameters parameters) {
		super(parameters);
	}

	private List<Bundle> processUpload() {
		// If project creation succeeds, check for file upload
		File thisFile = new File((fileMan.getTempDirectory ()).getAbsolutePath() + "/"
				+ upload.getClientFileName());
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
						projectFileBundles.set(i, bundleController.createBundle(
							projectFileBundles.get(i), userId));
					}  catch (BundleControllerException e) {
						if (e instanceof BundleConflictException) {
							e.printStackTrace();
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

	private ArrayList<String> listBundles() {
		ArrayList<String> bundles = new ArrayList<String>();
		Set<Bundle> bundlesRaw = thisUser.getBundles();
		for (Bundle bundle : bundlesRaw) {
			bundles.add(bundle.getSymbolicName());
			thisBundleModel.put(bundle.getSymbolicName(), bundle);
		}
		if (bundles.size() == 0)
			bundles.add("Bundle Library Empty");
		return bundles;
	}

	private ArrayList<String> listTeam() {
		Set<User> collaborators = thisUser.getCollaborators();
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
		thisProject.setOwner(thisUser);
		thisProject.setStatus(active ? "Active" : "Disabled");
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

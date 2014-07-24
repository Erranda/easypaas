package com.withinet.opaas.wicket.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
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

import com.withinet.opaas.controller.AccountController;
import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.AccountControllerException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.system.BundleFileInstaller;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.wicket.services.UserSession;


public class ProjectSetup extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258781378426675695L;

	private String name;

	private FileUploadField wicketFileUploadField;	

	private ArrayList <String> projectTeam = new ArrayList<String> ();
	
	private HashMap<String, User> thisTeamModel = new HashMap<String, User> ();
	
	private List<User> projectTeamSelected = new ArrayList<User> ();
	
	private Long userId = UserSession.get().getUser().getID();

	private ArrayList<String> projectBundles = new ArrayList<String> ();
	
	private FileUpload upload;
	
	private Boolean active = true;
	
	private User thisUser = UserSession.get ().getUser();
	
	@SpringBean
	private FileLocationGenerator fileMan;
	
	@SpringBean
	private BundleFileInstaller bundleInstaller;
	
	@SpringBean
	private BundleController bundleController;
	
	@SpringBean
	private ProjectController projectController;
	
	@SpringBean
	private AccountController accountController;

	
	public ProjectSetup() {
		//Set the user
		
		Form<Void> setupForm = new Form<Void>("form") {
			@Override
            protected void onSubmit()
            {
				//Create project first
				Project thisProject = new Project ();
				thisProject.setName(name);
				thisProject.setStatus(active ? "Active" : "Disabled");
				try {
					thisProject = projectController.createProject(thisProject, userId);
					//Add project team
					if (projectTeam.size() > 0) {
						for (String member : projectTeam) {
							User user = thisTeamModel.get(member);
							if (user == null) continue;
							projectTeamSelected.add(user);
						}
						accountController.addTeamMembers(projectTeamSelected, userId, userId);
					}
					//If project creation succeeds, check for file upload
					upload = wicketFileUploadField.getFileUpload();
					if (upload != null)
		            {
		            	String fileName =  upload.getClientFileName().toLowerCase().trim();
		            	String extension = FilenameUtils.getExtension(fileName);
						if (extension.equals(".xml") || fileName.equals(".jar") || fileName.equals(".zip")){
							File thisFile = new File (fileMan.getTempDirectoryPath() + "/" + upload.getClientFileName());
			            	try {
								if (thisFile.createNewFile() == true) {
									upload.writeTo(thisFile);
									File userLibDirectory = fileMan.getUserLibrary(userId);
									List<String> list = new ArrayList<String> ();
									list.add(thisFile.getAbsolutePath());
									List<Bundle> bundles = bundleInstaller.installBundles(list, userLibDirectory.getAbsolutePath());
									for (int i = 0; i < bundles.size(); i++) {
										bundles.set(i, bundleController.createBundle(bundles.get(i), userId));
									}
									for (Bundle bundle : bundles) {
										projectController.addBundle(bundle, thisProject.getID(), userId);
									}	
								} else {
									error ("Could not process your file at this time");
								}
							} catch (IOException e) {
								error (e.getMessage());
							} catch (BundleControllerException e) {
								error (e.getMessage());
							}
						} else {
							error ("Only .zip, .xml, and .jar extensions allowed");
						}
		            }
				} catch (ProjectControllerException e1) {
					error (e1.getMessage());
				} catch (AccountControllerException e) {
					error (e.getMessage());
				}
            }
		};
		add(setupForm);
		
		final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
		feedback.setOutputMarkupPlaceholderTag(true);
		setupForm.add(feedback);
		
		RequiredTextField<String> wicketName = new RequiredTextField<String>("name",
				new PropertyModel<String>(this, "name"));
		wicketName.add(StringValidator.minimumLength(2));
		wicketName.setLabel(new ResourceModel("label.name"));
		setupForm.add(wicketName);
		
		ListMultipleChoice<String> bundles = new ListMultipleChoice<String>(
				"bundles", new Model(projectBundles), listBundles());
		setupForm.add(bundles);
		
		ListMultipleChoice<String> team;
		try {
			team = new ListMultipleChoice<String>(
					"team", new Model(projectTeam), listTeam());
		} catch (AccountControllerException e) {
			team = new ListMultipleChoice<String>(
					"team", new Model(projectTeam), Collections.EMPTY_LIST);
			error (e.getMessage());
		}
		setupForm.add(team);
		
		wicketFileUploadField = new FileUploadField ("file");
		wicketFileUploadField.setRequired(true);
		setupForm.add(wicketFileUploadField);
		
		setupForm.setMaxSize(Bytes.megabytes(10000));
		UploadProgressBar  progress = new UploadProgressBar("progress", setupForm, wicketFileUploadField);
		setupForm.add(progress);
		setupForm.add(new CheckBox("active", new PropertyModel<Boolean>(this, "active"))); // this line
				

	}

	public ProjectSetup(IModel<?> model) {
		super(model);
	}

	public ProjectSetup(PageParameters parameters) {
		super(parameters);
	}
	
	private ArrayList<String> listBundles () {
		ArrayList<String> bundles = new ArrayList<String> ();
		bundles.add("apache.log.system/hdsh/1.0.0");
		return bundles;	
	}
	
	private ArrayList<String> listTeam () throws AccountControllerException {
		List<User> collaborators = accountController.listCollaborators(userId, userId);
		ArrayList<String> formList = new ArrayList<String> ();
		for (User user : collaborators) {
			String formKey = user.getFullName() + "[" + user.getEmail() + "]";
			formList.add(formKey);
			thisTeamModel.put(formKey, user);
		}
		Collections.sort(formList);
		return formList;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
	}

}

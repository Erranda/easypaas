package com.withinet.opaas.wicket.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.WicketRuntimeException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;

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

public class BundleSetupWidget extends Panel {

	private static final long serialVersionUID = 2258781378426675695L;

	private String name;

	private FileUploadField wicketFileUploadField;

	private ArrayList<String> projectNameSelected = new ArrayList<String>();

	private ArrayList<String> projectFileBundlesSelected = new ArrayList<String>();

	private HashMap<String, Bundle> thisBundleModel = new HashMap<String, Bundle>();
	
	private HashMap<String, Project> thisProjectModel = new HashMap<String, Project>();

	private FileUpload upload;

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
	
	Boolean authorized;

	public BundleSetupWidget(String id) throws UserControllerException, BundleControllerException, ProjectControllerException {
		super (id);
		
	}
	
	public BundleSetupWidget(String id, boolean b) {
		super (id);
		authorized = b;
	}
	
	@Override
	public void onInitialize () {
		super.onInitialize();
		setVisible (authorized);
		Form<Void> setupForm = new Form<Void>("form");
		add(setupForm);

		final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
		feedback.setOutputMarkupPlaceholderTag(true);
		setupForm.add(feedback);
		
		ListMultipleChoice<String> projects;
		try {
			projects = new ListMultipleChoice<String>(
					"projects", new Model(projectNameSelected), listProjects());
			setupForm.add(projects);
		} catch (ProjectControllerException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw new WicketRuntimeException ();
		}
		
		
		ListMultipleChoice<String> bundles;
		try {
			bundles = new ListMultipleChoice<String>(
					"bundles", new Model(projectFileBundlesSelected), listBundles());
			setupForm.add(bundles);
		} catch (BundleControllerException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			throw new WicketRuntimeException ();
		}
		
		
		wicketFileUploadField = new FileUploadField("file");
		wicketFileUploadField.setRequired(false);
		setupForm.add(wicketFileUploadField);
		setupForm.setMaxSize(Bytes.megabytes(10000));
		
		UploadProgressBar progress = new UploadProgressBar("progress",
				setupForm, wicketFileUploadField);
		setupForm.add(progress);
		
		final ConfirmationLink<String>  updateInstances = new ConfirmationLink<String> ("update-instances", "Update project instances with distribution?") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				try {
					Long uid = UserSession.get().getUser().getID();
					for (Project project : processSelectedProject ())
						projectController.refreshProjectInstances(project.getID(), uid);
					getPage().info ("Instances updated");
					setResponsePage (getPage());
				} catch (ProjectControllerException e) {
					e.printStackTrace();
					error (e.getMessage());
					target.add(feedback);
				}
			}
		};
		updateInstances.setOutputMarkupId(true);
		add (updateInstances);
		
		setupForm.add(new IndicatingAjaxButton ("submit", setupForm){
			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (processSelectedProject().size() == 0) {
					error ("No project selected");
					target.add(feedback);
				} else {
					Long start = System.currentTimeMillis();
					Double startD = start.doubleValue();
					upload = wicketFileUploadField.getFileUpload();

					User thisUser = UserSession.get().getUser();
					Long uid = UserSession.get().getUser().getID();
					try {
						if (upload != null) {
							String fileName = upload.getClientFileName().toLowerCase().trim();
							String extension = FilenameUtils.getExtension(fileName);
							extension = extension.toLowerCase().trim();
							if (extension.equals("xml") || extension.equals("jar")
									|| extension.equals("zip")) {
								List<Bundle> fileBundles = processUpload();
								List<Bundle> selectedBundles = processSelectedBundles ();
								List<Project> selectedProjects = processSelectedProject ();
								selectedBundles.addAll(fileBundles);
								addBundlesToProjects (uid, selectedBundles, selectedProjects);
								info ("Distribution added to projects");
								target.appendJavaScript("document.getElementById(\"" + updateInstances.getMarkupId() + "\").click();");
							} else {
								error("Only .zip, .xml, and .jar extensions allowed");
							}
						} else {
							List<Bundle> selectedBundles = processSelectedBundles ();
							List<Project> selectedProjects = processSelectedProject ();
							addBundlesToProjects (uid, selectedBundles, selectedProjects);
							info ("Distribution added to projects successfully");
							target.appendJavaScript("document.getElementById(\"" + updateInstances.getMarkupId() + "\").click();");
						}
					} catch (RuntimeException e) {
						e.printStackTrace();
						error (e.getMessage());
					} catch (ProjectControllerException e) {
						e.printStackTrace();
						error (e.getMessage());
					}
					target.add(feedback);
				}
				
			}
			
			private void addBundlesToProjects (Long uid, List<Bundle> selectedBundles, List<Project> selectedProjects) throws ProjectControllerException {
				for (Project project : selectedProjects) {
					for (Bundle bundle : selectedBundles) {
						projectController.addBundle(bundle, project.getID(), uid);
					}
				}
			}
			
			@Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            	target.add(feedback);
            }
			
			private List<Bundle> processUpload() {
				Long uid = UserSession.get().getUser().getID();
				// If project creation succeeds, check for file upload
				File thisFile = new File((fileMan.getConcurrentTempDirectory (uid)).getAbsolutePath() + "/"
						+ upload.getClientFileName());
				Long userId = UserSession.get().getUser().getID();
				try {
					if (thisFile.createNewFile()){
					upload.writeTo(thisFile);
					File userLibDirectory = fileMan.getUserLibrary(userId);
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
		});
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
		if (authorized) {
			List<Bundle> bundlesRaw = bundleController.listBundlesByOwner(uid, uid);
			for (Bundle bundle : bundlesRaw) {
				bundles.add(bundle.getSymbolicName());
				thisBundleModel.put(bundle.getSymbolicName(), bundle);
			}
		}
		return bundles;
	}

	private List<String> listProjects() throws ProjectControllerException  {
		Long uid = UserSession.get().getUser().getID();	
		ArrayList<String> formList = new ArrayList<String>();
		if (authorized) {
			List<Project> projects = projectController.listCreatedProjectsByOwner(uid, uid);					
			for (Project project : projects) {
				String formKey = project.getName();
				formList.add(formKey);
				thisProjectModel.put(formKey, project);
			}
			Collections.sort(formList);
		}
		
		return formList;
	}

	private List<Project> processSelectedProject() {
		List<Project> projects = new ArrayList<Project>();
		for (String key : projectNameSelected) {
			if (thisProjectModel.get(key) != null);
				projects.add(thisProjectModel.get(key));
		}
		return projects;
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
	}

}

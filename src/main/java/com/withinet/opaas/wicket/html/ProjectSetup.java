package com.withinet.opaas.wicket.html;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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

import com.withinet.opaas.controller.system.FileLocationGenerator;


public class ProjectSetup extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258781378426675695L;

	private String name;

	private FileUploadField wicketFileUploadField;	

	private ArrayList <String> projectTeam = new ArrayList<String> ();
	
	private Long userId;

	private ArrayList<String> projectBundles = new ArrayList<String> ();
	
	private FileUpload file;
	
	private boolean active = true;
	
	@SpringBean
	private FileLocationGenerator fileMan;


	public ProjectSetup() {
		//Set the user
		
		Form<Void> setupForm = new Form<Void>("form") {
			@Override
            protected void onSubmit()
            {
				FileUpload upload = wicketFileUploadField.getFileUpload();
				if (upload != null)
	            {
	            	String fileName =  upload.getClientFileName().toLowerCase().trim();
	            	String extension = FilenameUtils.getExtension(fileName);
	            	
					if (extension.equals(".xml") || fileName.equals(".jar") || fileName.equals(".zip")){
						File thisFile = new File (fileMan.getTempDirectoryPath() + "/" + upload.getClientFileName());
		            	try {
							if (thisFile.createNewFile() == true) {
								upload.writeTo(thisFile);
								
							} else {
								error ("Could not process your file at this time");
							}
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						error ("Only .zip, .xml, and .jar extensions allowed");
					}
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
		
		ListMultipleChoice<String> team = new ListMultipleChoice<String>(
				"team", new Model(projectTeam), listTeam());
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
	
	private ArrayList<String> listTeam () {
		ArrayList<String> collaborators = new ArrayList<String> ();
		collaborators.add("Yi Chang [yi@s.com]");
		collaborators.add("Mark Boss [si@g.com]");
		return collaborators;
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
	}

}

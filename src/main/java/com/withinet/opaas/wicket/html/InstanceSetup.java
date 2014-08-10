package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
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
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.lang.Bytes;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.Application;

public class InstanceSetup extends WebPage {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2258781378426675695L;

	private String projectName = "Hello World";

	private String containerName = "Felix";

	private List<String> containerTypes = null;
	
	private List<String> userProjects = null;

	private Long projectId;

	private Long userId = null;

	private String status;

	public InstanceSetup() {
		this(null);
	}

	public InstanceSetup(PageParameters parameters) {
		// Set the user
		super(parameters);

		if (parameters == null || parameters.get("pid") == null)
			// throw new RestartResponseAtInterceptPageException
			// (Application.get().getHomePage());
			// projectId = Long.parseLong(parameters.get("pid").toString());
		userProjects = new ArrayList<String>();
		userProjects.add("Hello World");
		userProjects.add("Analytics");
		
		containerTypes = new ArrayList<String>();
		containerTypes.add("Felix");
		containerTypes.add("Knopflerfish");
		containerTypes.add("Equinox");
		containerTypes.add("Concierge");

		Form<Void> setupForm = new Form<Void>("form");
		add(setupForm);

		setupForm.add(new FeedbackPanel("feedback"));

		DropDownChoice<String> containerType = new DropDownChoice<String>(
				"containerName", new PropertyModel<String>(this,
						"containerName"), containerTypes);
		setupForm.add(containerType);
		
		DropDownChoice<String> userProject = new DropDownChoice<String>(
				"userProjects", new PropertyModel<String>(this,
						"projectName"), userProjects);
		setupForm.add(userProject);

		setupForm.add(new AjaxButton("submit", setupForm) {
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {

			}
		});
	}

}

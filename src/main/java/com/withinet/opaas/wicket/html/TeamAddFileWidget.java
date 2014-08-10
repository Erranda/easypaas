package com.withinet.opaas.wicket.html;

import org.apache.wicket.extensions.ajax.markup.html.form.upload.UploadProgressBar;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;


/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class TeamAddFileWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370041751528976156L;
	private FileUpload upload;

	public TeamAddFileWidget (String id) {
		super (id);
	    Form<Void> signupForm = new Form<Void>("addMemberFileForm");
	    add(signupForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    signupForm.add(feedback);
	    FileUploadField file = new FileUploadField ("file");
	    file.setRequired(true);
	    signupForm.add(file);
	    UploadProgressBar  progress = new UploadProgressBar("progress", signupForm, file);
	    signupForm.add(progress);
	}
}

package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;


/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class AccountUpdateWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370041751528976156L;
	
	private String fullName = "John Doe";
	private String password = "blalslalasl";
	private String email = "email@xyz.com";

	public AccountUpdateWidget (String id) {
		super (id);
	    Form<Void> signupForm = new Form<Void>("form");
	    add(signupForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    signupForm.add(feedback);
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(this, "email"));
	    wicketEmail.add(StringValidator.minimumLength(2));
	    signupForm.add(wicketEmail);
	    
	    TextField<String> wicketName = new TextField<String>("name", new PropertyModel<String>(this, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    signupForm.add(wicketName);
	    
	    PasswordTextField wicketPassword = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
	    signupForm.add(wicketPassword);
	}
}

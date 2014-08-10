package com.withinet.opaas.wicket.html;

import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.validation.validator.StringValidator;


/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class TeamAddMemberWidget extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6370041751528976156L;
	private String fullName;
	private String email;

	public TeamAddMemberWidget (String id) {
		super (id);
	    Form<Void> signupForm = new Form<Void>("form");
	    add(signupForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    signupForm.add(feedback);
	    
	    RequiredTextField<String> wicketName = new RequiredTextField<String>("name", new PropertyModel<String>(this, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    signupForm.add(wicketName);
	    
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(this, "email"));
	    wicketEmail.setRequired(true);
	    signupForm.add(wicketEmail);
	}
}

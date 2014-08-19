package com.withinet.opaas.wicket.html;

import org.apache.wicket.Component;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;



/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class LoginWidget extends Panel {

	private String email;
	private String password;
	private boolean rememberMe;
	
	public LoginWidget (String id) {
		super (id);
		Form<Void> loginForm = new Form<Void>("form");
	    add(loginForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    
	    loginForm.add(feedback);
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(this, "email"));
	    wicketEmail.setLabel(new ResourceModel ("label.email"));
	    wicketEmail.setRequired(true);
	    loginForm.add(wicketEmail);
	    PasswordTextField wicketPassword = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
	    wicketPassword.setLabel(new ResourceModel ("label.password"));
	    wicketPassword.add(StringValidator.minimumLength(6));
	    loginForm.add(wicketPassword);
	    
	    loginForm.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe"))); // this line
	    
	    loginForm.add(new IndicatingAjaxButton("submit", loginForm)
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
               
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            	target.add(feedback);
            }
        });
	}

	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		
	}
}

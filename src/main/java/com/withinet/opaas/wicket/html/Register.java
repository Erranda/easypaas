package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.head.CssHeaderItem;
import org.apache.wicket.markup.head.IHeaderResponse;
import org.apache.wicket.markup.head.JavaScriptHeaderItem;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.User;



/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class Register extends Stateless {

	private String fullName;
	private String email;
	private String password;
	private String location = "";
	private String platformName;
	private String message;
	
	private boolean terms;
	
	@SpringBean
	private UserController userController;
	

	public Register () {
	    StatelessForm<Void> signupStatelessForm = new StatelessForm<Void>("form");
	    add(signupStatelessForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    signupStatelessForm.add(feedback);
	    
	    RequiredTextField wicketName = new RequiredTextField("name", new PropertyModel<String>(this, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    wicketName.setLabel(new ResourceModel ("label.name"));
	    signupStatelessForm.add(wicketName);
	    
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(this, "email"));
	    wicketEmail.setLabel(new ResourceModel ("label.email"));
	    wicketEmail.setRequired(true);
	    signupStatelessForm.add(wicketEmail);
	    
	    PasswordTextField wicketPassword = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
	    wicketPassword.setLabel(new ResourceModel ("label.password"));
	    wicketPassword.add(StringValidator.minimumLength(6));
	    wicketPassword.setRequired(true);
	    signupStatelessForm.add(wicketPassword);
	    
	    RequiredTextField wicketPlatformName = new RequiredTextField("platformName", new PropertyModel<String>(this, "platformName"));
	    wicketPlatformName.add(StringValidator.minimumLength(5));
	    wicketPlatformName.add(StringValidator.maximumLength(20));
	    wicketPlatformName.setLabel(new ResourceModel ("label.platformName"));
	    signupStatelessForm.add(wicketPlatformName);
	    
	    TextArea wicketMessage = new TextArea("message", new PropertyModel<String>(this, "message"));
	    wicketMessage.add(StringValidator.maximumLength(255));
	    wicketMessage.setLabel(new ResourceModel ("label.message"));
	    wicketMessage.setRequired(true);
	    signupStatelessForm.add(wicketMessage);
		
		List<String> countries = new ArrayList<String>();
	    
		String[] locales = Locale.getISOCountries();
		String unitedKingdom = "United Kingdom";
		countries.add(unitedKingdom);
    	for (String countryCode : locales) {
		    Locale obj = new Locale("", countryCode);
		    if (obj.getDisplayCountry().equals(unitedKingdom)); //Do nothing already added
		    else countries.add(obj.getDisplayCountry());
		}
    	
    	Collections.sort(countries.subList(1, countries.size() - 1));
    	
    	DropDownChoice wicketLocation = new DropDownChoice<String>("location" ,new PropertyModel<String>(this,"location"), countries){
    	
    	};
    	wicketLocation.setNullValid(false);
    	signupStatelessForm.add(wicketLocation);		
    	
    	signupStatelessForm.add(new CheckBox("terms", new PropertyModel<Boolean>(this, "terms"))); // this line
	    
	    signupStatelessForm.add(new IndicatingAjaxButton("submit", signupStatelessForm)
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
				if (terms) {
				User user = new User ();
				user.setRole("ADMIN");
				user.setEmail(email);
				user.setFullName(fullName);
				user.setLocation(location);
				user.setPassword(password);
				user.setPlatformName(platformName);
				user.setQuota(-1);
				user.setStatus("Disabled");
				user.setIntroduction(message);
				
				try {
					userController.createAccount(user);
					info ("A confirmation email has been sent to your email account");
					target.add(feedback);
				} catch (UserControllerException e) {
					error (e.getMessage());
					target.add(feedback);
				}
				} else {
					error ("Platform in Beta, we need some Wittiness");
					target.add(feedback);
				}
					
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

package com.withinet.opaas.wicket.html;

import javax.servlet.http.Cookie;

import org.apache.wicket.Application;
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
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebResponse;
import org.apache.wicket.request.resource.CssResourceReference;
import org.apache.wicket.request.resource.JavaScriptResourceReference;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.cookies.CookieUtils;
import org.apache.wicket.util.visit.IVisit;
import org.apache.wicket.util.visit.IVisitor;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountLoginException;

import static com.withinet.opaas.controller.common.ServiceProperties.*;

import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;


/**
 * Login page
 * 
 * @author kloe and Folarin
 *
 */
public class Login extends Base {

	private String email;
	private String password;
	private boolean rememberMe;
	
	@SpringBean
	UserController accountController;
	

	public Login () {
		if(UserSession.get().userLoggedIn()) throw new RestartResponseAtInterceptPageException(
				ProjectIndex.class);
		
		StatelessForm<Void> loginStatelessForm = new StatelessForm<Void>("form");
	    add(loginStatelessForm);
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    
	    loginStatelessForm.add(feedback);
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(this, "email"));
	    wicketEmail.setLabel(new ResourceModel ("label.email"));
	    wicketEmail.setRequired(true);
	    loginStatelessForm.add(wicketEmail);
	    PasswordTextField wicketPassword = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
	    wicketPassword.setLabel(new ResourceModel ("label.password"));
	    wicketPassword.add(StringValidator.minimumLength(6));
	    loginStatelessForm.add(wicketPassword);
	    
	    loginStatelessForm.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(this, "rememberMe"))); // this line
	    
	    loginStatelessForm.add(new IndicatingAjaxButton("submit", loginStatelessForm)
        {
            /**
			 * 
			 */
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
            {
               target.add(feedback);
               User user;
			try {
				user = accountController.login(email, password);
				//Excepetion thrown if invalid
				UserSession.get().setUser(user);
                if(rememberMe) {
                	CookieUtils util = new CookieUtils ();
                	util.save(REMEMBER_ME_EMAIL_COOKIE, user.getEmail());
                	util.save(REMEMBER_ME_PASSWORD_COOKIE, user.getPassword());
                }
                setResponsePage(ProjectIndex.class);
			} catch (AccountLoginException e) {
				feedback.error(e.getMessage());
			} catch (RuntimeException e) {
				error ("We couldn't process your request at this time ");
				e.printStackTrace();
			}
			
            }

            @Override
            protected void onError(AjaxRequestTarget target, Form<?> form)
            {
            	target.add(feedback);
            }
        });
	}
}

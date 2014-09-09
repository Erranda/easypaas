package com.withinet.opaas.wicket.html;

import java.util.Date;

import org.apache.wicket.Application;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.cookies.CookieUtils;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.WicketApplication;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.AccountLoginException;
import com.withinet.opaas.controller.common.UserControllerException;

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

	/**
	 * 
	 */
	private static final long serialVersionUID = 8991168083149150709L;
	private String email;
	private String password;
	private boolean rememberMe;

	@SpringBean
	UserController accountController;

	public Login() {
		if (UserSession.get().userLoggedIn())
			throw new RestartResponseAtInterceptPageException(
					ProjectIndex.class);

		Form<Void> loginForm = new Form<Void>("form");
		add(loginForm);
		final CSSFeedbackPanel feedback = new CSSFeedbackPanel("feedback");
		feedback.setOutputMarkupId(true);

		loginForm.add(feedback);
		EmailTextField wicketEmail = new EmailTextField("email",
				new PropertyModel<String>(this, "email"));
		wicketEmail.setLabel(new ResourceModel("label.email"));
		wicketEmail.setRequired(true);
		loginForm.add(wicketEmail);
		PasswordTextField wicketPassword = new PasswordTextField("password",
				new PropertyModel<String>(this, "password"));
		wicketPassword.setLabel(new ResourceModel("label.password"));
		wicketPassword.add(StringValidator.minimumLength(6));
		loginForm.add(wicketPassword);

		loginForm.add(new CheckBox("rememberMe", new PropertyModel<Boolean>(
				this, "rememberMe"))); // this line

		loginForm.add(new IndicatingAjaxButton("submit", loginForm) {
			/**
			 * 
			 */
			private static final long serialVersionUID = -6415555183396288060L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				User user;
				try {
					user = accountController.login(email, password);
					user.setLastSeen(new Date());
					accountController.updateAccount(user, user.getID(),
							user.getID());
					// Excepetion thrown if invalid
					UserSession.get().setUser(user);
					((WicketApplication) Application.get()).getSessions().put(
							user.getID(), UserSession.get());
					if (rememberMe) {
						CookieUtils util = new CookieUtils();
						util.save(REMEMBER_ME_EMAIL_COOKIE, user.getEmail());
						util.save(REMEMBER_ME_PASSWORD_COOKIE,
								user.getPassword());
					}
					setResponsePage(ProjectIndex.class);
				} catch (AccountLoginException e) {
					error(e.getMessage());
				} catch (RuntimeException e) {
					error("We couldn't process your request at this time ");
					e.printStackTrace();
				} catch (UserControllerException e) {
					error("We couldn't process your request at this time ");
					e.printStackTrace();
				}
				target.add(feedback);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedback);
			}
		});
	}
}

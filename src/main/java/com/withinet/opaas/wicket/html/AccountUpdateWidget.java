package com.withinet.opaas.wicket.html;

import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;


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
	final User user = UserSession.get().getUser();
	private String fullName = null;
	private String password = null;
	private String confirmPassword = null;
	private String email = null;
	
	@SpringBean
	UserController userController;

	public AccountUpdateWidget (String id) {
		super (id);
		
	    Form<Void> updateForm = new Form<Void>("form") {
	    	@Override
	    	public void onSubmit () {
	    		if (confirmPassword != null && password != null) {
	    			if( confirmPassword.equals(password)) {
	    				user.setPassword(password);
	    			} else {
	    				error ("Passwords have to match");
	    				throw new RestartResponseAtInterceptPageException (getPage());
	    			}
	    		}
	    		try {
	    			//Save and refresh page
    				UserSession.get().setUser(userController.updateAccount(user, user.getID(), user.getID()));
					AccountIndex newPage = new AccountIndex ();
					newPage.info("Updated");
					setResponsePage (newPage);
				} catch (UserControllerException e) {
					error (e.getMessage());
					setResponsePage (getPage());
				} 
	    	 }
	    };
	    add(updateForm);
	    
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(user, "email"));
	    updateForm.add(wicketEmail);
	    
	    TextField<String> wicketName = new TextField<String>("name", new PropertyModel<String>(user, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    updateForm.add(wicketName);
	    
	    PasswordTextField wicketPassword = new PasswordTextField("password", new PropertyModel<String>(this, "password"));
	    wicketPassword.add (StringValidator.minimumLength(6));
	    wicketPassword.setRequired(false);
	    wicketPassword.setLabel(new ResourceModel ("label.password"));
	    updateForm.add(wicketPassword);
	    
	    PasswordTextField wicketConfirmPassword = new PasswordTextField("confirm-password", new PropertyModel<String>(this, "confirmPassword"));
	    wicketConfirmPassword.add (StringValidator.minimumLength(6));
	    wicketConfirmPassword.setRequired(false);
	    wicketConfirmPassword.setLabel(new ResourceModel ("label.confirmPassword"));
	    updateForm.add(wicketConfirmPassword);
	    
	    
	    
	}
}

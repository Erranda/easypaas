package com.withinet.opaas.wicket.html;

import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.FileControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.wicket.services.UserSession;


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
	
	@SpringBean
	private UserController userController;
	
	public TeamAddMemberWidget (String id) {
		super (id);
	    Form<Void> addMemberForm = new Form<Void>("form");
	    add(addMemberForm);
	    final User user = new User ();
	    
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    addMemberForm.add(feedback);
	    
	    RequiredTextField<String> wicketName = new RequiredTextField<String>("name", new PropertyModel<String>(user, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    addMemberForm.add(wicketName);
	    
	    RequiredTextField<Integer> wicketQuota = new RequiredTextField<Integer>("quota", new PropertyModel<Integer>(user, "quota"));
	    addMemberForm.add(wicketQuota);
	    
	    RequiredTextField<String> wicketRole = new RequiredTextField<String>("role", new PropertyModel<String>(user, "role"));
	    addMemberForm.add(wicketRole);
	    
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(user, "email"));
	    wicketEmail.setRequired(true);
	    addMemberForm.add(wicketEmail);
	    
	    addMemberForm.add(new IndicatingAjaxButton ("submit", addMemberForm){
	    	@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
	    		Long uid = UserSession.get().getUser().getID();
	    		try {
					userController.addTeamMember(user, uid, uid);
					info ("Member added");
					setResponsePage (getPage ());
				} catch (UserControllerException e) {
					error (e.getMessage());
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
}

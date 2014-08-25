package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxButton;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import com.withinet.opaas.controller.RoleController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.FileControllerException;
import com.withinet.opaas.controller.common.RoleControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.model.domain.Role;
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
	
	@SpringBean
	private RoleController roleController;
	
	private final Panel target;
	
	private String selectedRole;
	
	private Map<String, Role> rolesModel = new HashMap<String, Role> ();
	
	final User user = new User ();
	
	public TeamAddMemberWidget (String id, Panel target) {
		super (id);
		this.target = target;
	    Form<Void> addMemberForm = new Form<Void>("form");
	    add(addMemberForm);
	    
	    final CSSFeedbackPanel feedback = new CSSFeedbackPanel ("feedback");
	    feedback.setOutputMarkupId(true);
	    addMemberForm.add(feedback);
	    
	    RequiredTextField<String> wicketName = new RequiredTextField<String>("name", new PropertyModel<String>(user, "fullName"));
	    wicketName.add(StringValidator.minimumLength(2));
	    addMemberForm.add(wicketName);
	    
	    RequiredTextField<Integer> wicketQuota = new RequiredTextField<Integer>("quota", new PropertyModel<Integer>(user, "quota"));
	    addMemberForm.add(wicketQuota);
	    
	    Long uid = UserSession.get().getUser().getID();
	    
	    try {
			DropDownChoice<String> wicketRole = new DropDownChoice<String>("role", new PropertyModel<String> (user, "role"), initRoles ());
			addMemberForm.add (wicketRole);
		} catch (RoleControllerException e1) {
			throw new RuntimeException (e1.getMessage ());
		}
	    
	    EmailTextField wicketEmail = new EmailTextField("email", new PropertyModel<String>(user, "email"));
	    wicketEmail.setRequired(true);
	    addMemberForm.add(wicketEmail);
	    
	    addMemberForm.add(new IndicatingAjaxButton ("submit", addMemberForm){
	    	@Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form)
			{
	    		Long uid = UserSession.get().getUser().getID();
	    		try {
	    			Role role = rolesModel.get(user.getRole());
	    			if (role != null) {
	    				user.setAssignedRole(role);
						userController.addTeamMember(user, uid, uid);
						info ("Member added");
						target.add(feedback);
						target.add(getTarget());
	    			} else {
	    				error ("Invalid Request");
	    				target.add(feedback);
						target.add(getTarget());
	    			}
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
	
	public List<String> initRoles () throws RoleControllerException {
		Long uid = UserSession.get().getUser().getID();
		List<String> roles = new ArrayList<String> ();
		for (Role role : roleController.readRolesByOwner(uid)) {
			roles.add(role.getName());
			rolesModel.put(role.getName(), role);
			user.setRole(role.getName());
		}
		return roles;
    }

	public Panel getTarget() {
		return target;
	}
}

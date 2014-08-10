package com.withinet.opaas.wicket.html;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.wicket.services.UserSession;


/**
 * @author Martijn Dashorst
 */
public class ProjectIndex extends Authenticated
{
	@SpringBean
	ProjectController projectController;
	
	Long userId = UserSession.get().getUser().getID();
	
	transient Integer projectSize;
    /**
     * Constructor.
     */
    public ProjectIndex ()
    {
        try {
        	projectSize = projectController.listCreatedProjectsByOwner(userId, userId).size();
			add (new ProjectTableWidget ("project-table-widget"));
	        add (new ProjectSetupWidget ("project-setup-widget"));
		} catch (ProjectControllerException e) {
			error (e.getMessage());	
		} catch (UserControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
}
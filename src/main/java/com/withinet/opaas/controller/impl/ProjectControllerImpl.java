/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class ProjectControllerImpl implements ProjectController {

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.ProjectController#createProject(com.withinet.opaas.domain.Project)
	 */
	public Project createProject(Project project)
			throws ProjectControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.ProjectController#deleteProject(com.withinet.opaas.domain.Project)
	 */
	public boolean deleteProject(Project project)
			throws ProjectControllerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.ProjectController#updateProject(com.withinet.opaas.domain.Project)
	 */
	public Project updateProject(Project project)
			throws ProjectControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.ProjectController#readProject(com.withinet.opaas.domain.Project)
	 */
	public Project readProject(Project project)
			throws ProjectControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.ProjectController#listProjectsByUser(com.withinet.opaas.domain.User)
	 */
	public List<Project> listProjectsByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}

/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public interface ProjectController {
	
	public Project createProject (Project project) throws ProjectControllerException;
	
	public boolean deleteProject (Project project) throws ProjectControllerException;
	
	public Project updateProject (Project project) throws ProjectControllerException;
	
	public Project readProject (Project project) throws ProjectControllerException;
	
	public List<Project> listProjectsByUser (User user);
	
}

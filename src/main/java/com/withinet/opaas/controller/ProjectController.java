/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;
import java.util.Set;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;

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
	
	public Project addBundle (Bundle bundle);
	
	public Project addBundle (Set<Bundle> bundle);
	
	public List<Project> listProjectsByUser (Long userId);
	
}

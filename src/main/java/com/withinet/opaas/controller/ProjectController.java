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
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public interface ProjectController {
	
	public Project createProject (Project project, Long requesterId) throws ProjectControllerException;
	
	public boolean deleteProject (Long id, Long requesterId) throws ProjectControllerException;
	
	public Project updateProject (Project project, Long id, Long requesterId) throws ProjectControllerException;
	
	public Project readProject (Long id, Long requesterId) throws ProjectControllerException;
	
	public Project addBundles (Set<Bundle> bundles, Long projectId, Long requesterId);
	
	public List<Project> listProjectsByUser (Long id, Long requesterId);
	
	public boolean addCollaborator (Long id, Long requesterId);
	
	
}

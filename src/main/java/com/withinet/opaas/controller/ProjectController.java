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
import com.withinet.opaas.domain.ProjectBundle;
import com.withinet.opaas.domain.ProjectTeam;
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
	
	public Project readProjectById (Long id, Long requesterId) throws ProjectControllerException;
	
	public List<Project> listCreatedProjectsByOwner (Long userId, Long requesterId) throws ProjectControllerException;
	
	public List<ProjectTeam> listParticipatingProjectsByUser (Long userId, Long requesterId) throws ProjectControllerException;
	
	public List<ProjectTeam> listProjectTeamMembersByProject (Long projectId, Long requesterId);
	
	public List<ProjectBundle> listProjectBundlesByProject (Long projectId, Long requesterId);
	
	public Project addBundle (Bundle bundles, Long projectId, Long requesterId) throws ProjectControllerException;
	
	public Project addCollaborator (User user, Long projectId, Long requesterId) throws ProjectControllerException;
	
}

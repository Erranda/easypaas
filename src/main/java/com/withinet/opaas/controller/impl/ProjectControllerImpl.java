package com.withinet.opaas.controller.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.ProjectMemberNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.ProjectConflictException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ProjectNotFoundException;
import com.withinet.opaas.controller.common.ProjectOwnerNotFoundException;
import com.withinet.opaas.controller.common.UnauthorizedException;
import com.withinet.opaas.model.ProjectBundleRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.ProjectTeamRepository;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;
import com.withinet.opaas.model.domain.ProjectTeam;
import com.withinet.opaas.model.domain.User;

@RestController
public class ProjectControllerImpl implements ProjectController {
	
	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProjectTeamRepository projectTeamRepo;
	
	@Autowired
	ProjectBundleRepository projectBundleRepo;

	@Override
	public Project createProject(Project project, Long requesterId)
			throws ProjectControllerException {
		DomainConstraintValidator<Project> dcv = new  DomainConstraintValidator<Project> ();
		if (!dcv.isValid(project)) throw new IllegalArgumentException ("Bad request");
		User user = userRepository.findOne(requesterId);
		if (user == null) 
			throw new ProjectOwnerNotFoundException ("Project owner not found");
		//Check if it already exists
		if (projectRepository.findByOwnerAndName(user, project.getName()).size() > 0)
			throw new ProjectConflictException ("You already have a project with this name.");	
		project.setCreated(new Date ());
		project.setUpdated(new Date ());
		return projectRepository.saveAndFlush(project);
	}

	@Override
	public boolean deleteProject(Long id, Long requesterId)
			throws ProjectControllerException {
		projectRepository.delete(id);
		return true;
	}

	@Override
	public Project updateProject(Project project, Long id, Long requesterId)
			throws ProjectControllerException {
		Project object = projectRepository.findOne(id);
		if (object == null)
			throw new ProjectNotFoundException ("Project requested not found");
		if (project.getOwner().getID () != requesterId)
			throw new ControllerSecurityException ("This action is not authorized");
		if (project.getName() != null) {
			if (projectRepository.findByOwnerAndName(object.getOwner(), project.getName()).size() > 0)
				throw new ProjectConflictException ("You already have a project with this name.");
			else {
				object.setName(project.getName());
			}
		}
		if (project.getStatus() != null)
			object.setStatus(project.getStatus());
		if (project.getDetails() != null)
			object.setDetails(project.getDetails());
		return projectRepository.save(object);
	}

	@Override
	public Project readProjectById(Long id, Long requesterId)
			throws ProjectControllerException {
		// You have to be a team member or owner to see details
		Project thisProject = projectRepository.findOne(id);
		if (thisProject == null) throw new ProjectNotFoundException ("Project requested not found");
		User thisUser = userRepository.findOne(requesterId);
		if (thisUser == null) throw new ControllerSecurityException ("Authenticated request malformed");
		if ((thisProject.getOwner().getID() != requesterId) && !isTeamMember (thisProject, thisUser))
			throw new ControllerSecurityException ("This action is not authorized");
		return thisProject;
	}

	private boolean isTeamMember(Project project, User user) {
		return ((projectTeamRepo.findByProjectAndUser(project, user).size()) > 0);
	}

	@Override
	public List<Project> listCreatedProjectsByOwner(Long userId,
			Long requesterId) throws ProjectControllerException {
		User target = approveAccessCascadeType (userId, requesterId);
		if (target == null || target.getID() == 0) 
			throw new ControllerSecurityException ("Unauthenticated");
		return projectRepository.findByOwner(target);
	}
	
	private User approveAccessCascadeType (Long userId, Long requesterId) {
		User user = userRepository.findOne(userId);
		if (user == null) 
			return null;
		if (userId == requesterId) 
			return user;
		else {
			User admin = user.getAdministrator();
			if (admin == null) 
				return null; //May be accessing resource of super admin
			else if (admin.getID() == requesterId)
				return user;
		}
		return null;
	}
	
	@Override
	public List<ProjectTeam> listParticipatingProjectsByUser(Long userId,
		    Long requesterId) throws ProjectControllerException {
		User target = approveAccessCascadeType (userId, requesterId);
		if (target == null)
			throw new ControllerSecurityException ("Unauthenticated");
		return projectTeamRepo.findByUser(target);
	}

	@Override
	public List<ProjectTeam> listProjectTeamMembersByProject(Long projectId,
			Long requesterId) {
		Project project = projectRepository.findOne(projectId);
		User requester = userRepository.findOne(requesterId);
		if ((project.getOwner().getID() != requesterId) &&
				isTeamMember (project, requester))
			throw new ControllerSecurityException ("Unauthenticated");
		else {
			return projectTeamRepo.findByProject(project);
		}
	}
	
	@Override
	public List<ProjectBundle> listProjectBundlesByProject(Long projectId,
			Long requesterId) {
		Project project = projectRepository.findOne(projectId);
		User requester = userRepository.findOne(requesterId);
		if ((project.getOwner().getID() != requesterId) &&
				isTeamMember (project, requester))
			throw new ControllerSecurityException ("Unauthenticated");
		else {
			return projectBundleRepo.findByProject(project);
		}
	}

	@Override
	public Project addBundle(Bundle bundle, Long projectId, Long requesterId)
			throws ProjectControllerException {
		Project thisProject = projectRepository.findOne(projectId);
		User user = userRepository.findOne(requesterId);
		if (bundle == null) throw new IllegalArgumentException ("Bundle cannot be null");
		if (bundle.getID() == 0) 
			throw new ProjectNotFoundException ("Please save the bundle first");
		if (thisProject == null)
			throw new ProjectNotFoundException ("Project is not recognized");
		if ((thisProject.getOwner().getID() != requesterId) 
				&& (thisProject.getOwner().getAdministrator().getID() != requesterId))
			throw new UnauthorizedException ("Unauthorized request");
		ProjectBundle thisProjectBundle = new ProjectBundle (user.getEmail(), thisProject, bundle);
		projectBundleRepo.save(thisProjectBundle);
		return thisProject;
	}

	@Override
	public Project addCollaborator(User user, Long projectId, Long requesterId)
			throws ProjectControllerException {
		if (user.getID() == 0) 
			throw new ProjectMemberNotFoundException ("Please create this collaborator first");
		Project thisProject = projectRepository.findOne(projectId);
		if (thisProject == null || thisProject.getID() == 0) 
			throw new ProjectNotFoundException ("Project not found");
		User owner = userRepository.findOne(requesterId);
		if (owner == null) 
			throw new UnauthorizedException ("Unauthorized");
		if ((thisProject.getOwner().getID() != requesterId) 
				&& (thisProject.getOwner().getAdministrator().getID() != requesterId))
			throw new UnauthorizedException ("Unauthorized");
		ProjectTeam thisProjectMember = new ProjectTeam (owner.getEmail(), thisProject, user);
		projectTeamRepo.save(thisProjectMember);
		return thisProject;
	}

}

package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectMemberNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.ProjectConflictException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ProjectNotFoundException;
import com.withinet.opaas.controller.common.ProjectOwnerNotFoundException;
import com.withinet.opaas.controller.common.UnauthorizedException;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.ProjectBundleRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.ProjectTeamRepository;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;
import com.withinet.opaas.model.domain.ProjectTeam;
import com.withinet.opaas.model.domain.User;
import static com.withinet.opaas.controller.common.ServiceProperties.*;

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

	@Autowired
	InstanceController instanceController;

	@Autowired
	BundleController bundleController;
	
	private Authorizer authorizer;
	
	@Autowired
	public void setAuthorizer (Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	@Override
	public Project createProject(Project project, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(project);
		Validation.assertNotNull(requesterId);
		DomainConstraintValidator<Project> dcv = new DomainConstraintValidator<Project>();
		User user = authorizer.authorize(CREATE_PROJECT, requesterId);
		if (user == null)
			throw new ProjectOwnerNotFoundException("Project owner not found");
		// Check if it already exists
		if (projectRepository.findByOwnerAndName(user, project.getName())
				.size() > 0)
			throw new ProjectConflictException(
					"You already have a project with this name.");
		project.setOwner(user);
		project.setCreated(new Date());
		project.setUpdated(new Date());
		if (!dcv.isValid(project))
			throw new IllegalArgumentException("Bad request");
		return projectRepository.saveAndFlush(project);
	}

	@Override
	public boolean deleteProject(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(DELETE_PROJECT, requesterId);
		Project project = getWithAdminAuth(id, requesterId);
		try {
			List<Instance> instances = instanceController
					.listInstancesByProject(project.getID(), requesterId);
			if (instances != null && instances.size() > 0) {
				for (Instance instance : instances) {
					instanceController.deleteInstance(instance.getId(),
							requesterId);
				}
			}
			project = getWithAdminAuth(id, requesterId);
			projectRepository.delete(project);

		} catch (InstanceControllerException e) {
			throw new ProjectControllerException(e.getMessage());
		}
		return true;
	}

	@Override
	public boolean disableProject(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(DISABLE_PROJECT, requesterId);
		Project project = getWithAdminAuth(id, requesterId);
		project.setStatus("Disabled");
		projectRepository.saveAndFlush(project);
		return true;
	}

	@Override
	public Project updateProject(Project project, Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(project);
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(UPDATE_PROJECT, requesterId);
		Project object = projectRepository.findOne(id);
		if (object == null)
			throw new ProjectNotFoundException("Project requested not found");
		if (project.getOwner().getID() != requesterId)
			throw new ControllerSecurityException(
					"This action is not authorized");
		if (project.getName() != null) {
			if (projectRepository.findByOwnerAndName(object.getOwner(),
					project.getName()).size() > 0)
				throw new ProjectConflictException(
						"You already have a project with this name.");
			else {
				object.setName(project.getName());
			}
		}
		if (project.getStatus() != null)
			object.setStatus(project.getStatus());
		if (project.getDetails() != null)
			object.setDetails(project.getDetails());
		return projectRepository.saveAndFlush(object);
	}

	@Override
	public Project readProjectById(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String> (READ_PROJECT);
		composite.addAll(CREATE_PROJECT);
		authorizer.authorize(composite, requesterId);
		// You have to be a team member or owner to see details
		Project thisProject = projectRepository.findOne(id);
		if (thisProject == null)
			throw new ProjectNotFoundException("Project requested not found");
		User thisUser = userRepository.findOne(requesterId);
		if (thisUser == null)
			throw new ControllerSecurityException(
					"Authenticated request malformed");
		if ((thisProject.getOwner().getID() != requesterId)
				&& !isTeamMember(thisProject, thisUser))
			throw new ControllerSecurityException(
					"This action is not authorized");
		return thisProject;
	}

	private Project getWithBasicAuth(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		// You have to be a team member or owner to see details
		Project thisProject = projectRepository.findOne(id);
		if (thisProject == null)
			throw new ProjectNotFoundException("Project requested not found");
		User thisUser = userRepository.findOne(requesterId);
		if (thisUser == null)
			throw new ControllerSecurityException("Requester unidentifiable");
		if ((thisProject.getOwner().getID() != requesterId)
				&& !isTeamMember(thisProject, thisUser))
			throw new ControllerSecurityException(
					"This action is not authorized");
		return thisProject;
	}

	private Project getWithAdminAuth(Long id, Long requesterId)
			throws ProjectControllerException {
		Project thisProject = projectRepository.findOne(id);
		if (thisProject == null)
			throw new ProjectNotFoundException("Project requested not found");
		User thisUser = userRepository.findOne(requesterId);
		if (thisUser == null)
			throw new ControllerSecurityException("Requester unidentifiable");
		if ((thisProject.getOwner().getID() != requesterId)
				&& (thisProject.getOwner().getAdministrator().getID() != requesterId))
			throw new ControllerSecurityException(
					"This action is not authorized");
		return thisProject;
	}

	private boolean isTeamMember(Project project, User user) {
		return ((projectTeamRepo.findByProjectAndUser(project, user).size()) > 0);
	}

	@Override
	public List<Project> listCreatedProjectsByOwner(Long userId,
			Long requesterId) throws ProjectControllerException {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String> (READ_PROJECT);
		composite.addAll(CREATE_PROJECT);
		authorizer.authorize(composite, requesterId);
		User target = approveAccessCascadeType(userId, requesterId);
		if (target == null || target.getID() == 0)
			throw new ControllerSecurityException("Unauthenticated");
		return projectRepository.findByOwner(target);
	}
	
	@Override
	public List<Project> listAllProjects(Long userId,
			Long requesterId) throws ProjectControllerException {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String> (READ_PROJECT);
		composite.addAll(CREATE_PROJECT);
		authorizer.authorize(composite, requesterId);
		User target = approveAccessCascadeType(userId, requesterId);
		if (target == null || target.getID() == 0)
			throw new ControllerSecurityException("Unauthenticated");
		List<Project> projects = projectRepository.findByOwner(target);
		for (ProjectTeam pt : listParticipatingProjectsByUser(userId, requesterId)) {
			projects.add(pt.getProject());
		}
		return projects;
	}

	private User approveAccessCascadeType(Long userId, Long requesterId) {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		User user = userRepository.findOne(userId);
		if (user == null)
			return null;
		if (userId == requesterId)
			return user;
		else {
			User admin = user.getAdministrator();
			if (admin == null)
				return null; // May be accessing resource of super admin
			else if (admin.getID() == requesterId)
				return user;
		}
		return null;
	}

	@Override
	public List<ProjectTeam> listParticipatingProjectsByUser(Long userId,
			Long requesterId) throws ProjectControllerException {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		User target = approveAccessCascadeType(userId, requesterId);
		if (target == null)
			throw new ControllerSecurityException("Unauthenticated");
		return projectTeamRepo.findByUser(target);
	}

	@Override
	public List<User> listProjectTeamMembersByProject(Long projectId,
			Long requesterId) {
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(requesterId);
		//No authorization needed
		Project project = projectRepository.findOne(projectId);
		User requester = userRepository.findOne(requesterId);
		
		if ((project.getOwner().getID() != requesterId)
				&& isTeamMember(project, requester))
			throw new ControllerSecurityException("Unauthenticated");
		else {
			List<User> teamMembers = new ArrayList<User> ();
			for (ProjectTeam member : projectTeamRepo.findByProject(project)) {
				teamMembers.add(member.getUser());
			}
			return teamMembers;
		}
	}

	@Override
	public List<ProjectBundle> listProjectBundlesByProject(Long projectId,
			Long requesterId) {
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(READ_PROJECT, requesterId);
		Project project = projectRepository.findOne(projectId);
		User requester = userRepository.findOne(requesterId);
		if ((project.getOwner().getID() != requesterId)
				&& isTeamMember(project, requester))
			throw new ControllerSecurityException("Unauthenticated");
		else {
			return projectBundleRepo.findByProject(project);
		}
	}

	@Override
	public Project addBundle(Bundle bundle, Long projectId, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(CREATE_BUNDLE, requesterId);
		Project thisProject = projectRepository.findOne(projectId);
		User user = userRepository.findOne(requesterId);
		if (bundle == null)
			throw new IllegalArgumentException("Bundle cannot be null");
		if (bundle.getID() == 0)
			throw new ProjectNotFoundException("Please save the bundle first");
		if (thisProject == null)
			throw new ProjectNotFoundException("Project is not recognized");
		if ((thisProject.getOwner().getID() != requesterId)
				&& (thisProject.getOwner().getAdministrator().getID() != requesterId))
			throw new UnauthorizedException("Unauthorized request");
		ProjectBundle thisProjectBundle = new ProjectBundle(user.getEmail(),
				thisProject, bundle);
		projectBundleRepo.saveAndFlush(thisProjectBundle);
		return thisProject;
	}

	@Override
	public Project addCollaborator(User user, Long projectId, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(user);
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(CREATE_PROJECT, requesterId);
		if (user.getID() == 0)
			throw new ProjectMemberNotFoundException(
					"Please create this collaborator first");
		Project thisProject = projectRepository.findOne(projectId);
		if (thisProject == null || thisProject.getID() == 0)
			throw new ProjectNotFoundException("Project not found");
		User owner = userRepository.findOne(requesterId);
		if (owner == null)
			throw new UnauthorizedException("Unauthorized");
		if ((thisProject.getOwner().getID() != requesterId)
				&& (thisProject.getOwner().getAdministrator().getID() != requesterId))
			throw new UnauthorizedException("Unauthorized");
		ProjectTeam thisProjectMember = new ProjectTeam(owner.getEmail(),
				thisProject, user);
		projectTeamRepo.saveAndFlush(thisProjectMember);
		return thisProject;
	}

	@Override
	public void refreshProjectInstances(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String>(UPDATE_PROJECT);
		composite.addAll(DELETE_BUNDLE);
		composite.addAll(CREATE_BUNDLE);
		composite.addAll(UPDATE_BUNDLE);
		
		authorizer.authorize(composite, requesterId);
		getWithBasicAuth(id, requesterId);
		List<Instance> instances;
		try {
			instances = instanceController.listInstancesByProject(id,
					requesterId);
			for (Instance instance : instances) {
				instanceController.stopInstance(instance.getId(), requesterId);
				instanceController.startInstance(instance.getId(), requesterId,
						false);
			}
		} catch (InstanceControllerException e) {
			e.printStackTrace();
			throw new ProjectControllerException(e.getMessage());
		}
	}

	@Override
	public void refreshProjectInstancesDirty(Long id, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String>(UPDATE_PROJECT);
		composite.addAll(DELETE_BUNDLE);
		composite.addAll(CREATE_BUNDLE);
		composite.addAll(UPDATE_BUNDLE);
		authorizer.authorize(composite, requesterId);
		getWithBasicAuth(id, requesterId);
		List<Instance> instances;
		try {
			instances = instanceController.listInstancesByProject(id,
					requesterId);
			for (Instance instance : instances) {
				instanceController.stopInstance(instance.getId(), requesterId);
				instanceController.startInstance(instance.getId(), requesterId,
						true);
			}
		} catch (InstanceControllerException e) {
			e.printStackTrace();
			throw new ProjectControllerException(e.getMessage());
		}
	}

	@Override
	public List<Project> listProjectsByBundle(Long bid, Long requesterId)
			throws ProjectControllerException {
		Validation.assertNotNull(bid);
		Validation.assertNotNull(requesterId);
		List<String> composite = new ArrayList<String>(READ_PROJECT);
		composite.addAll(DELETE_BUNDLE);
		composite.addAll(UPDATE_BUNDLE);
		composite.addAll(CREATE_BUNDLE);

		authorizer.authorize(composite, requesterId);
		List<Project> projects = new ArrayList<Project>();
		try {
			Bundle bundle = bundleController.readBundle(bid, requesterId);
			List<ProjectBundle> pbs = projectBundleRepo.findByBundle(bundle);

			for (ProjectBundle pb : pbs) {
				projects.add(pb.getProject());
			}
		} catch (BundleControllerException e) {
			throw new ProjectControllerException(e.getMessage());
		}
		return projects;
	}

}

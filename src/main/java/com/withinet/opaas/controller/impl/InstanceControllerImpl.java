/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.ServiceProperties;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.system.FileLocationGenerator;
import com.withinet.opaas.controller.system.FileService;
import com.withinet.opaas.controller.system.ProcessService;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.InstanceRepository;
import com.withinet.opaas.model.ProjectRepository;
import com.withinet.opaas.model.UserRepository;
import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class InstanceControllerImpl implements InstanceController {
	
	@Autowired
	InstanceRepository instanceRepository;
	
	@Autowired
	ProjectController projectController;
	
	@Autowired
	UserController userController;
	
	@Autowired
	ProcessService processService;
	
	@Autowired
	FileLocationGenerator fileLocationGenerator;
	
	@Autowired
	FileService fileService;
	
	private Authorizer authorizer;
	
	@Autowired
	public void setAuthorizer (Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#createInstance(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Instance createInstance(Instance instance, Long projectId, Long userId, Long requesterId)
			throws InstanceControllerException {
		Validation.assertNotNull(instance);
		Validation.assertNotNull(instance.getContainerType());
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		try {
			User user = authorizer.authorize(Arrays.asList("createInstance", "instanceAdmin"), requesterId);
			List<Instance> instances = instanceRepository.findByOwner(user);
			if (instances.size() == user.getQuota())
				throw new InstanceControllerException ("You have reached your limit");
			Project project = projectController.readProjectById(projectId, requesterId);
			if (project.getStatus().equals("Disabled"))
				throw new ProjectControllerException ("Oops, look like this project has not been activated");
			instance.setProject(project);
			instance.setProjectName(project.getName());
			instance.setOwner(user);
			instance.setOwnerName(user.getFullName());
			instance.setAdministrator(user.getAdministrator());
			instance.setPort(getOpenPort ());
			instance.setCpanelUrl(ServiceProperties.DOMAIN + ":" + instance.getPort () + "/system/console");
			instance.setStatus("Starting");
			instance.setCreated(new Date());
			//For scalability host will have their identifiers
			instance.setHostName("server-1");
			DomainConstraintValidator<Instance> dcv = new  DomainConstraintValidator<Instance> ();
			if (!dcv.isValid(instance)) throw new IllegalArgumentException ("Bad request");
			//Step one
			instanceRepository.saveAndFlush(instance);
			//Step two
			Long iid = instance.getId();
			File workingDir = fileLocationGenerator.getInstanceDirectory(userId, iid);
			FileUtils.cleanDirectory(workingDir);
			instance.setWorkingDirectory(workingDir.getAbsolutePath());
			File logFile = fileLocationGenerator.getInstanceLogFile(userId, iid);
			instance.setLogFile(logFile.getAbsolutePath());
			instanceRepository.saveAndFlush(instance);
			processService.startProcess(instance);
			//There should be a cron like service monitoring instances
			instance.setStatus("Live");
			instanceRepository.saveAndFlush(instance);
		} catch (ProjectControllerException e) {
			rollBack (instance);
			throw new InstanceControllerException (e.getMessage());
		} catch (IOException e) {
			rollBack (instance);
			throw new InstanceControllerException (e.getMessage());
		} catch (RuntimeException e) {
			rollBack (instance);
			throw e;
		}
		return instance;
	}
	
	public void rollBack (Instance instance) {
		if (instance.getId() != null || instance.getId () != 0)
			instanceRepository.delete(instance);
	}
	
	private int getOpenPort () {
		int min = ServiceProperties.MINPORT;
		int max = ServiceProperties.MAXPORT;
		int difference = max - min;
		int port = (int)((Math.random()*difference) + min);		
		while (true) {
			if(instanceRepository.findByPort (port) == null)
					return port;
			else 
				port = (int)((Math.random()*difference) + min);	
		}
	}
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#deleteInstance(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean deleteInstance(Long id, Long requesterId)
			throws InstanceControllerException {
		authorizer.authorize(Arrays.asList("createInstance", "deleteInstance", "instanceAdmin"), requesterId);
		Instance instance = getWithBasicAuth (id, requesterId);
		
		if (instance.getStatus().equals("Live")){
			stopInstance (id, requesterId);
		}
		
		//Some instances may have launched but be  stuck in starting
		if (instance.getStatus().equals("Starting")){
			stopInstance (id, requesterId);
		}
			
		try {
			fileService.deleteFile(instance.getWorkingDirectory());
		} catch (IOException e) {
			throw new InstanceControllerException (e.getMessage());
		}
		instance.getProject().getInstances().remove(instance);
		instanceRepository.delete(instance);
		return true;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#updateInstance(com.withinet.opaas.domain.Instance, java.lang.Long)
	 */
	@Override
	public Instance updateInstance(Instance instance, Long requesterId)
			throws InstanceControllerException {
		Validation.assertNotNull(instance);
		authorizer.authorize(Arrays.asList("updateInstance", "instanceAdmin"), requesterId);
		Instance instancePersist = getWithBasicAuth (instance.getId(), requesterId);
		if (instance.getStatus() != null)
			instancePersist.setStatus(instance.getStatus());
		instanceRepository.saveAndFlush(instancePersist);
		return instancePersist;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#readInstance(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Instance readInstance(Long id, Long requesterId)
			throws InstanceControllerException {
		authorizer.authorize(Arrays.asList("readInstance", "instanceAdmin"), requesterId);
		return getWithBasicAuth (id, requesterId);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#listInstancesByUser(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Instance> listInstancesByUser(Long userId, Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("createInstance", "instanceAdmin"), requesterId);
		try {
			User thisUser = userController.readAccount(userId, requesterId);
			return instanceRepository.findByOwner(thisUser);
		} catch (UserControllerException e) {
			throw new InstanceControllerException (e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#listInstancesByProject(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Instance> listInstancesByProject(Long projectId,
			Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("createProject", "projectAdmin"), requesterId);
		try {
			Project project = projectController.readProjectById(projectId, requesterId);
			return instanceRepository.findByProject(project);
		} catch (ProjectControllerException e) {
			throw new InstanceControllerException (e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#listInstancesByAdministrator(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Instance> listInstancesByAdministrator(Long adminId,
			Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(adminId);
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("admin"), requesterId);
		return instanceRepository.findByAdministrator(user);
	}
	
	private Instance getWithBasicAuth (Long instanceId, Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(instanceId);
		Validation.assertNotNull(requesterId);
		Instance instance = instanceRepository.findOne(instanceId);
		if (instance == null)
			throw new InstanceControllerException ("Instance not found");
		if ((requesterId
				!= instance.getOwner().getID())
			&&
			requesterId 
			 	!= instance.getAdministrator().getID())
			throw new ControllerSecurityException ("You are not authorized to perform this action");
		return instance;
	}

	@Override
	public void stopInstance(Long id, Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("stopInstance", "instanceAdmin"), requesterId);
		Instance instance = getWithBasicAuth (id, requesterId);
		processService.stopProcess(instance.getId());
		// Important this comes after process call
		instance.setStatus("Dead");
		instanceRepository.saveAndFlush(instance);
	}
	
	@Override
	public void startInstance (Long id, Long requesterId, boolean dirty) throws InstanceControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("createInstance", "instanceAdmin"), requesterId);
		Instance instance = getWithBasicAuth (id, requesterId);
		try {
			fileService.deleteFile(instance.getLogFile());
		} catch (IOException e) {
			throw new InstanceControllerException ("Could not delete previous log file");
		}
		instance.setDirty(dirty);
		processService.startProcess(instance);
		//Important this comes after process call
		instance.setStatus("Live");
		instanceRepository.saveAndFlush (instance);
	}

}

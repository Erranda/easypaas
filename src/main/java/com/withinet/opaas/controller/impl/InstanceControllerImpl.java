/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.Validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.InstanceController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UserControllerException;
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
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#createInstance(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Instance createInstance(Instance instance, Long projectId, Long userId, Long requesterId)
			throws InstanceControllerException {
		Validation.assertNotNull(instance);
		Validation.assertNotNull(projectId);
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
		DomainConstraintValidator<Instance> dcv = new  DomainConstraintValidator<Instance> ();
		if (!dcv.isValid(instance)) throw new IllegalArgumentException ("Bad request");
		try {
			User user = userController.readAccount(userId, requesterId);
			user.getInstances().add(instance);
			Project project = projectController.readProjectById(projectId, requesterId);
			project.getInstances().add(instance);
			instance.setProject(project);
			instance.setOwner(user);
			instance.setAdministrator(user.getAdministrator());
			instanceRepository.save(instance);
		} catch (ProjectControllerException e) {
			throw new InstanceControllerException (e.getMessage());
		} catch (UserControllerException e) {
			throw new InstanceControllerException (e.getMessage());
		}
		
		return instance;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#deleteInstance(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean deleteInstance(Long id, Long requesterId)
			throws InstanceControllerException {
		Instance instance = getWithBasicAuth (id, requesterId);
		// processService.stopProcess(requesterId);
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
		Instance instancePersist = getWithBasicAuth (instance.getId(), requesterId);
		if (instance.getStatus() != null)
			instancePersist.setStatus(instance.getStatus());
		instanceRepository.save(instancePersist);
		return instancePersist;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#readInstance(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Instance readInstance(Long id, Long requesterId)
			throws InstanceControllerException {	
		return getWithBasicAuth (id, requesterId);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.InstanceController#listInstancesByUser(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Instance> listInstancesByUser(Long userId, Long requesterId) throws InstanceControllerException {
		Validation.assertNotNull(userId);
		Validation.assertNotNull(requesterId);
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
		try {
			User user = userController.readAccount(adminId, requesterId);	
			return instanceRepository.findByAdministrator(user);
		} catch (UserControllerException e) {
			throw new InstanceControllerException (e.getMessage());
		}
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

}

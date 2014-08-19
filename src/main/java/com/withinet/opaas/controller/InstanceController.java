package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.model.domain.Instance;

/**
 * @author Folarin
 *
 */
@RestController
public interface InstanceController {
	
	public Instance createInstance (Instance instance, Long projectId, Long userId, Long requesterId) throws InstanceControllerException;
	
	public void stopInstance (Long id, Long requesterId) throws InstanceControllerException;
	
	public boolean deleteInstance (Long id, Long requesterId) throws InstanceControllerException;
	
	public Instance updateInstance (Instance instance, Long requesterId) throws InstanceControllerException;
	
	public Instance readInstance (Long id, Long requesterId) throws InstanceControllerException;
	
	public List<Instance> listInstancesByUser (Long userId, Long requesterId) throws InstanceControllerException;
	
	public List<Instance> listInstancesByProject (Long projectId, Long requesterId) throws InstanceControllerException;
	
	public List<Instance> listInstancesByAdministrator (Long adminId, Long requesterId) throws InstanceControllerException;

	public void startInstance(Long id, Long requesterId, boolean b)
			throws InstanceControllerException;
	
}


package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.InstanceControllerException;
import com.withinet.opaas.domain.Instance;

/**
 * @author Folarin
 *
 */
@RestController
public interface InstanceController {
	
	public Instance createInstance (Instance instance) throws InstanceControllerException;
	
	public void deleteInstance (Long id) throws InstanceControllerException;
	
	public Instance updateInstance (Instance instance) throws InstanceControllerException;
	
	public Instance readInstance (Long id) throws InstanceControllerException;
	
	public List<Instance> listInstancesByUser (Long userId);
	
}


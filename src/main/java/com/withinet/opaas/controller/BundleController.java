package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public interface BundleController {
	
	public Bundle createBundle (Bundle bundle, Long requesterId) throws BundleControllerException;
	
	public boolean deleteBundle (Long id, Long requesterId) throws BundleControllerException;
	
	public Bundle updateBundle (Bundle bundle, Long id, Long requesterId) throws BundleControllerException;
	
	public Bundle readBundle (Long id, Long requesterId) throws BundleControllerException;
	
	public Bundle readBundleByName (String name, Long requesterId) throws BundleControllerException;
	
	public List<Bundle> listBundlesByOwner (Long id, Long requesterId);
	
}

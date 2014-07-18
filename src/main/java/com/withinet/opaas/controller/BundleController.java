package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.User;

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
	
	public List<Bundle> listBundlesByOwner (User user, Long requesterId);
	
}

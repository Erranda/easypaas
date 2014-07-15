package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.domain.Bundle;

/**
 * @author Folarin
 *
 */
@RestController
public interface BundleController {
	
	public Bundle createBundle (Bundle bundle) throws BundleControllerException;
	
	public void deleteBundle (Bundle bundle) throws BundleControllerException;
	
	public Bundle updateBundle (Bundle bundle) throws BundleControllerException;
	
	public Bundle readBundle (Long id) throws BundleControllerException;
	
	public List<Bundle> listBundlesByUser (Long userId);
	
}

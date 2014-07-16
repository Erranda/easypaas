package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public interface BundleController {
	
	public Bundle createBundle (Bundle bundle) throws BundleControllerException;
	
	public void deleteBundle (Bundle bundle) throws BundleControllerException;
	
	public Bundle updateBundle (Bundle bundle) throws BundleControllerException;
	
	public Bundle readBundle (Bundle bundle) throws BundleControllerException;
	
	public List<Bundle> listBundlesByUser (User user);
	
	public List<Bundle> listBundlesByProject (Project project);
	
}

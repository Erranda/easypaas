/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class BundleControllerImpl implements BundleController {

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#createBundle(com.withinet.opaas.domain.Bundle)
	 */
	public Bundle createBundle(Bundle bundle) throws BundleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#deleteBundle(com.withinet.opaas.domain.Bundle)
	 */
	public void deleteBundle(Bundle bundle) throws BundleControllerException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#updateBundle(com.withinet.opaas.domain.Bundle)
	 */
	public Bundle updateBundle(Bundle bundle) throws BundleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#readBundle(com.withinet.opaas.domain.Bundle)
	 */
	public Bundle readBundle(Bundle bundle) throws BundleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#listBundlesByUser(com.withinet.opaas.domain.User)
	 */
	public List<Bundle> listBundlesByUser(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#listBundlesByProject(com.withinet.opaas.domain.Project)
	 */
	public List<Bundle> listBundlesByProject(Project project) {
		// TODO Auto-generated method stub
		return null;
	}

}

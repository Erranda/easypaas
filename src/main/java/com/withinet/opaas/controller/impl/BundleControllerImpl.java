/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.common.BundleConflictException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.UnauthorizedException;
import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.UserRepository;

/**
 * @author Folarin
 *
 */
@RestController
public class BundleControllerImpl implements BundleController {
	
	@Autowired
	BundleRepository bundleRepository;
	
	@Autowired
	UserRepository userRepository;
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#createBundle(com.withinet.opaas.domain.Bundle, java.lang.Long)
	 */
	@Override
	public Bundle createBundle(Bundle bundle, Long requesterId)
			throws BundleControllerException {
		DomainConstraintValidator<Bundle> dcv = new  DomainConstraintValidator<Bundle> ();
		if (!dcv.isValid(bundle)) throw new IllegalArgumentException ("Bad request");
		User user = userRepository.findOne(requesterId);
		if (user == null || user.getID() == 0)
			throw new UnauthorizedException ("Unauthorized");
		if (bundleRepository.findByOwnerAndSymbolicName(user, bundle.getSymbolicName()).size() > 0) 
			throw new BundleConflictException ("Bundle with this name already exists");
		bundle.setUpdated(new Date());
		bundle.setOwner(user);
		return bundleRepository.save(bundle);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#deleteBundle(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean deleteBundle(Long id, Long requesterId)
			throws BundleControllerException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#updateBundle(com.withinet.opaas.domain.Bundle, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Bundle updateBundle(Bundle bundle, Long id, Long requesterId)
			throws BundleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#readBundle(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Bundle readBundle(Long id, Long requesterId)
			throws BundleControllerException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#listBundlesByOwner(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Bundle> listBundlesByOwner(Long id, Long requesterId) {
		// TODO Auto-generated method stub
		return null;
	}

}

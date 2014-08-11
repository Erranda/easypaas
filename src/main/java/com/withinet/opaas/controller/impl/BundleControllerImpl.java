/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.BundleController;
import com.withinet.opaas.controller.ProjectController;
import com.withinet.opaas.controller.UserController;
import com.withinet.opaas.controller.common.BundleConflictException;
import com.withinet.opaas.controller.common.BundleControllerException;
import com.withinet.opaas.controller.common.BundleNotFoundException;
import com.withinet.opaas.controller.common.ControllerSecurityException;
import com.withinet.opaas.controller.common.DomainConstraintValidator;
import com.withinet.opaas.controller.common.ProjectControllerException;
import com.withinet.opaas.controller.common.UnauthorizedException;
import com.withinet.opaas.controller.common.UserControllerException;
import com.withinet.opaas.controller.system.FileService;
import com.withinet.opaas.controller.system.Validation;
import com.withinet.opaas.model.BundleRepository;
import com.withinet.opaas.model.ProjectBundleRepository;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;
import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public class BundleControllerImpl implements BundleController {
	
	
	BundleRepository bundleRepository;
	
	UserController userController;
	
	FileService fileService;
	
	ProjectBundleRepository projectBundleRepository;
	
	@Autowired
	private ProjectController projectController;
	
	@Autowired
	public void setBundleRepository (BundleRepository bundleRepository) {
		this.bundleRepository = bundleRepository;
	}
	
	@Autowired
	public void setUserController (UserController userController) {
		this.userController = userController;
	}
	
	@Autowired
	public void setProjectBundleRepository (ProjectBundleRepository repository) {
		this.projectBundleRepository = repository;
	}
	
	@Autowired
	public void setFileService (FileService fileService) {
		this.fileService = fileService;
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#createBundle(com.withinet.opaas.domain.Bundle, java.lang.Long)
	 */
	@Override
	public Bundle createBundle(Bundle bundle, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(bundle);
		Validation.assertNotNull(requesterId);
		DomainConstraintValidator<Bundle> dcv = new  DomainConstraintValidator<Bundle> ();
		if (!dcv.isValid(bundle)) throw new IllegalArgumentException ("Bad request");
		User user;
		
		try {
			user = userController.readAccount(requesterId, requesterId);
		} catch (UserControllerException e) {
			throw new BundleControllerException (e.getMessage());
		}
		
		if (user == null || user.getID() == 0)
			throw new UnauthorizedException ("Unauthorized");
		if (bundleRepository.findByOwnerAndSymbolicName(user, bundle.getSymbolicName()) != null) 
			throw new BundleConflictException ("Bundle with name " + bundle.getSymbolicName() + " already exists");
		bundle.setUpdated(new Date());
		bundle.setOwner(user);
		user.getBundles().add(bundle);
		return bundleRepository.save(bundle);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#deleteBundle(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean deleteBundle(Long id, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		Bundle forDelete = getWithBasicAuth (id, requesterId);
		List<ProjectBundle> projects = projectBundleRepository.findByBundle(forDelete);
		projectBundleRepository.delete(projects);
		bundleRepository.delete(forDelete);
		bundleRepository.findOne(forDelete.getID());
		return fileService.deleteFile(forDelete.getLocation());
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#updateBundle(com.withinet.opaas.domain.Bundle, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Bundle updateBundle(Bundle bundle, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(bundle);
		Validation.assertNotNull(requesterId);
		Bundle forSave = getWithBasicAuth (bundle.getID(), requesterId);
		if (bundle.getLocation() != null)
			forSave.setLocation(bundle.getLocation());
		bundleRepository.save(forSave);
		return forSave;
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#readBundle(java.lang.Long, java.lang.Long)
	 */
	@Override
	public Bundle readBundle(Long id, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		return getWithBasicAuth (id, requesterId);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#listBundlesByOwner(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Bundle> listBundlesByOwner(Long id, Long requesterId) throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		try {
			User user = userController.readAccount(id, requesterId);
			return bundleRepository.findByOwner(user);
		} catch (UserControllerException e) {
			throw new BundleControllerException (e.getMessage());
		}
	}

	@Override
	public Bundle readBundleByName(String name, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(name);
		Validation.assertNotNull(requesterId);
		try {
			User user = userController.readAccount(requesterId, requesterId);
			return bundleRepository.findByOwnerAndSymbolicName(user, name);
		} catch (UserControllerException e) {
			throw new BundleControllerException (e.getMessage());
		}
	}
	
	private Bundle getWithBasicAuth (Long bundleId, Long requesterId) throws BundleControllerException {
		Validation.assertNotNull(bundleId);
		Validation.assertNotNull(requesterId);
		Bundle bundle = bundleRepository.findOne(bundleId);
		if (bundle == null)
			throw new BundleNotFoundException ("Bundle not found");
		if ((requesterId
				!= bundle.getOwner().getID())
			&&
			requesterId 
			 	!= bundle.getOwner().getAdministrator().getID())
			throw new ControllerSecurityException ("You are not authorized to perform this action");
		return bundle;
	}

	@Override
	public List<Bundle> listBundlesByProject(Long id, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		try {
			Project project = projectController.readProjectById(id, requesterId);
			List<ProjectBundle> projectBundles = projectBundleRepository.findByProject(project);
			List<Bundle> bundles = new ArrayList<Bundle> ();
			if (projectBundles.size() > 0)
				for (ProjectBundle pb: projectBundles)
					bundles.add(pb.getBundle());
			return bundles;
		} catch (ProjectControllerException e) {
			e.printStackTrace();
			throw new BundleControllerException (e.getMessage());
		}
	}
}

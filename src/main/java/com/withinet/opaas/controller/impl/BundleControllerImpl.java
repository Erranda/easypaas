/**
 * 
 */
package com.withinet.opaas.controller.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
	
	Authorizer authorizer;
	
	private ProjectController projectController;
	
	@Autowired
	public void setAuthorizer (Authorizer authorizer) {
		this.authorizer = authorizer;
	}
	
	public void setProjectController (ProjectController projectController) {
		this.projectController = projectController;
	}
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
		User user = authorizer.authorize(Arrays.asList("createBundle", "bundleAdmin"), requesterId);
		if (bundleRepository.findByOwnerAndSymbolicName(user, bundle.getSymbolicName()) != null) 
			throw new BundleConflictException ("Bundle with name " + bundle.getSymbolicName() + " already exists");
		bundle.setUpdated(new Date());
		bundle.setOwner(user);
		return bundleRepository.saveAndFlush(bundle);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#deleteBundle(java.lang.Long, java.lang.Long)
	 */
	@Override
	public boolean deleteBundle(Long id, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("deleteBundle", "bundleAdmin"), requesterId);
		Bundle forDelete = getWithBasicAuth (id, requesterId);
		bundleRepository.delete(forDelete);
		try {
			return fileService.deleteFile(forDelete.getLocation());
		} catch (IOException e) {
			throw new BundleControllerException (e.getMessage());
		}
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#updateBundle(com.withinet.opaas.domain.Bundle, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Bundle updateBundle(Bundle bundle, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(bundle);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("updateBundle", "bundleAdmin"), requesterId);
		Bundle forSave = getWithBasicAuth (bundle.getID(), requesterId);
		if (bundle.getLocation() != null)
			forSave.setLocation(bundle.getLocation());
		if (bundle.getSymbolicName() != null){
			try {
				Bundle temp = bundleRepository.findByOwnerAndSymbolicName(userController.readAccount(requesterId, requesterId), bundle.getSymbolicName());
				if  (temp != null)
					throw new BundleControllerException ("A bundle exists with that name");
				else
					forSave.setSymbolicName(bundle.getSymbolicName());
			} catch (UserControllerException e) {
				throw new BundleControllerException (e.getMessage());
			}
		}
			forSave.setLocation(bundle.getLocation());
		bundleRepository.saveAndFlush(forSave);
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
		authorizer.authorize(Arrays.asList("readBundle", "bundleAdmin"), requesterId);
		return getWithBasicAuth (id, requesterId);
	}

	/* (non-Javadoc)
	 * @see com.withinet.opaas.controller.BundleController#listBundlesByOwner(java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<Bundle> listBundlesByOwner(Long id, Long requesterId) throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("createBundle", "bundleAdmin"), requesterId);			
		return bundleRepository.findByOwner(user);
	}

	@Override
	public Bundle readBundleByName(String name, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(name);
		Validation.assertNotNull(requesterId);
		User user = authorizer.authorize(Arrays.asList("createBundle", "readBundle", "bundleAdmin"), requesterId);
		if (user == null || user.getID() == 0)
			throw new UnauthorizedException ("Unauthorized");
		return bundleRepository.findByOwnerAndSymbolicName(user, name);
	}

	@Override
	public List<Bundle> listBundlesByProject(Long id, Long requesterId)
			throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("readBundle", "bundleAdmin"), requesterId);
		List<ProjectBundle> projectBundles = projectController.listProjectBundlesByProject(id, requesterId);
		List<Bundle> bundles = new ArrayList<Bundle> ();
		if (projectBundles.size() > 0)
			for (ProjectBundle pb: projectBundles)
				bundles.add(pb.getBundle());
		return bundles;
	}
	
	@Override
	public void refreshBundleInstances (Long id, Long requesterId) throws BundleControllerException {
		Validation.assertNotNull(id);
		Validation.assertNotNull(requesterId);
		authorizer.authorize(Arrays.asList("createInstance", "adminInstance"), requesterId);
		try {
			List<Project> projects = projectController.listProjectsByBundle(id, requesterId);
			for (Project project : projects) {
				projectController.refreshProjectInstancesDirty(project.getID(), requesterId);
			}
		} catch (ProjectControllerException e) {
			throw new BundleControllerException ("Could not refresh project bundles : "  + e.getMessage());
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
}

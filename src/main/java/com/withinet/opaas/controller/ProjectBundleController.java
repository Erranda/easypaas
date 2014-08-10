package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.ProjectBundleControllerException;
import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;

/**
 * @author Folarin
 *
 */
@RestController
public interface ProjectBundleController {
	
	public ProjectBundle createProjectBundle (ProjectBundle bundle, Long requesterId) throws ProjectBundleControllerException;
	
	public void deleteProjectBundle (Long id, Long requesterId) throws ProjectBundleControllerException;

	public List<ProjectBundle> listProjectBundlesByProject (Long id, Long requesterId);
	
	public List<ProjectBundle> listProjectBundlesByBundle (Long id, Long requesterId);
	
}

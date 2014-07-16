package com.withinet.opaas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.ProjectBundleControllerException;
import com.withinet.opaas.domain.ProjectBundle;
import com.withinet.opaas.domain.Project;

/**
 * @author Folarin
 *
 */
@RestController
public interface ProjectBundleController {
	
	public ProjectBundle createProjectBundle (ProjectBundle bundle) throws ProjectBundleControllerException;
	
	public void deleteProjectBundle (ProjectBundle bundle) throws ProjectBundleControllerException;

	public List<ProjectBundle> listProjectBundlesByProject (Project project);
	
}

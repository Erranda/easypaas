/**
 * 
 */
package com.withinet.opaas.controller;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.OrganisationControllerException;
import com.withinet.opaas.domain.Organisation;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */
@RestController
public interface OrganisationController {
	
	public Organisation createOrganisation(Organisation account) throws OrganisationControllerException;
	
	public void deleteOrganisation(Organisation id) throws OrganisationControllerException;
	
	public Organisation updateOrganisation(Organisation account) throws OrganisationControllerException;
	
	public Organisation readOrganisation(Long id) throws OrganisationControllerException;
	
	public Organisation listOrganisationByUser (User user);
	
}

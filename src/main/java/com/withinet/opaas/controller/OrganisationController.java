/**
 * 
 */
package com.withinet.opaas.controller;

import org.springframework.web.bind.annotation.RestController;

import com.withinet.opaas.controller.common.OrganisationControllerException;
import com.withinet.opaas.domain.Organisation;

/**
 * @author Folarin
 *
 */
@RestController
public interface OrganisationController {
	
	public Organisation createOrganisation(Organisation account, Long requesterId) throws OrganisationControllerException;
	
	public void deleteOrganisation(Long id, Long requesterId) throws OrganisationControllerException;
	
	public Organisation updateOrganisation(Organisation account, Long id, Long requesterId) throws OrganisationControllerException;
	
	public Organisation readOrganisation(Long id, Long requesterId) throws OrganisationControllerException;
	
	public Organisation getOrganisationByUser (Long id, Long requesterId);
	
}

/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.Set;

import com.withinet.opaas.controller.common.CollaboratorControllerException;
import com.withinet.opaas.domain.User;

/**
 * @author Folarin
 *
 */

public interface CollaboratorController {
	
	public User createCollaborator (User account) throws CollaboratorControllerException;
	
	public Set<User> createCollaboratorBatch (Set<User> accounts) throws CollaboratorControllerException;
	
	public boolean deleteCollaborator (Long id) throws CollaboratorControllerException;
	
	public User updateCollaborator (User account) throws CollaboratorControllerException;
	
	public User readCollaborator (Long id) throws CollaboratorControllerException;
	
	public Set<User> listCollaboratorsByAdminId (User user);
	
}

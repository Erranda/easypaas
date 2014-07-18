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
	
	public User createCollaborator (User account, Long requesterId) throws CollaboratorControllerException;
	
	public Set<User> createCollaboratorBatch (Set<User> accounts, Long requesterId) throws CollaboratorControllerException;
	
	public boolean deleteCollaborator (Long id, Long requesterId) throws CollaboratorControllerException;
	
	public User updateCollaborator (User account, Long id, Long requesterId) throws CollaboratorControllerException;
	
	public User readCollaborator (Long id, Long requesterId) throws CollaboratorControllerException;
	
	public Set<User> listCollaboratorsByAdminId (Long id, Long requesterId);
	
}

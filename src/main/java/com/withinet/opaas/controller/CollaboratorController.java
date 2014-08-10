/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;
import java.util.Set;

import com.withinet.opaas.controller.common.CollaboratorControllerException;
import com.withinet.opaas.model.domain.User;

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
	
	public List<User> findCollaboratorsByOwner (Long id, Long requesterId);
	
}

/**
 * 
 */
package com.withinet.opaas.controller;

import java.util.List;

import com.withinet.opaas.model.domain.User;

/**
 * @author Folarin
 *
 */
public interface Authorizer {
	
	public User authorize (String expectedPermission, Long uid);
	
	public User authorize (List<String> expectedPermission, Long uid);
}

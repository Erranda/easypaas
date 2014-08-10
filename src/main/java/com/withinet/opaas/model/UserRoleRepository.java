package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.model.domain.UserRole;

public interface UserRoleRepository extends JpaRepository <UserRole, Long>{
	
	public UserRole  findByOwnerAndName (User owner, String name);
	
	public List<UserRole> findByOwner (User owner);
	
}

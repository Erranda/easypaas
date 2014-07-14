package com.withinet.opaas.domain.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.domain.User;
import com.withinet.opaas.domain.UserRole;

public interface UserRoleRepository extends JpaRepository <UserRole, Long>{
	
	public UserRole  findByOwnerAndName (User owner, String name);
	
	public List<UserRole> findByOwner (User owner);
	
}

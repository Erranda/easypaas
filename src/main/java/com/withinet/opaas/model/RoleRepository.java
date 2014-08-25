package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.User;
import com.withinet.opaas.model.domain.Role;

public interface RoleRepository extends JpaRepository <Role, Long>{
	
	public Role  findByOwnerAndName (User owner, String name);
	
	public List<Role> findByOwner (User owner);
	
}

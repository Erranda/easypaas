package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.Permission;

public interface PermissionRepository extends JpaRepository <Permission, Long>{
	
	List<Permission> findByValue (String value);
	
}

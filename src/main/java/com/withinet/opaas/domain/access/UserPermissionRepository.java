package com.withinet.opaas.domain.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.domain.UserPermission;

public interface UserPermissionRepository extends JpaRepository <UserPermission, Long>{
	
	List<UserPermission> findByValue (String value);
}

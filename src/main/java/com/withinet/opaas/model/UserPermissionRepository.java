package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.withinet.opaas.domain.UserPermission;

public interface UserPermissionRepository extends JpaRepository <UserPermission, Long>{
	
	List<UserPermission> findByValue (String value);
}

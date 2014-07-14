package com.withinet.opaas.domain.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.domain.RolePermission;
import com.withinet.opaas.domain.UserRole;

@Repository
public interface RolePermissionRepository extends  JpaRepository<RolePermission, Long> {
	
	public List<RolePermission> findByUserRole (UserRole userRole);

}

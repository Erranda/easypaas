package com.withinet.opaas.domain.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.domain.Bundle;
import com.withinet.opaas.domain.User;

public interface BundleRepository extends JpaRepository<Bundle, Long> {
	
	public List<Bundle> findByOwner (User owner) ;
	
	public List<Bundle> findByOwnerAndSymbolicName (User owner, String symbolicName);
	
}

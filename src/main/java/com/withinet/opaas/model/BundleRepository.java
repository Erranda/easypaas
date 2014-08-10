package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.User;

public interface BundleRepository extends JpaRepository<Bundle, Long> {
	
	public List<Bundle> findByOwner (User owner) ;
	
	public Bundle findByOwnerAndSymbolicName (User owner, String symbolicName);
	
}

package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	public List<Project> findByOwner (User owner) ;
	
	public List<Project> findByOwnerAndName (User owner, String name);
	
}

package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.ProjectBundle;

@Repository
public interface ProjectBundleRepository extends  JpaRepository<ProjectBundle, Long> {
	
	public List<ProjectBundle> findByUserProject (Project userProject);

}
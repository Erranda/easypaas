package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.model.domain.Bundle;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectBundle;

@Repository
public interface ProjectBundleRepository extends  JpaRepository<ProjectBundle, Long> {
	
	public List<ProjectBundle> findByProject (Project userProject);
	
	public List<ProjectBundle> findByBundle (Bundle bundle);

}
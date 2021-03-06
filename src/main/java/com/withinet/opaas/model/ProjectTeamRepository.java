package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.ProjectTeam;
import com.withinet.opaas.model.domain.User;

@Repository
public interface ProjectTeamRepository extends  JpaRepository<ProjectTeam, Long> {
	
	public List<ProjectTeam> findByProject (Project userProject);
	
	public List<ProjectTeam> findByUser (User user);
	
	public List<ProjectTeam> findByProjectAndUser (Project project, User user);

}
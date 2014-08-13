package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.model.domain.Instance;
import com.withinet.opaas.model.domain.Project;
import com.withinet.opaas.model.domain.User;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
	
	public List<Instance> findByOwner (User owner) ;
	
	public List<Instance> findByProject (Project project);
	
	public List<Instance> findByAdministrator (User administrator);

	public Instance findByPort(Integer port);
	
}

package com.withinet.opaas.domain.access;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.withinet.opaas.domain.Instance;
import com.withinet.opaas.domain.Project;
import com.withinet.opaas.domain.User;

public interface InstanceRepository extends JpaRepository<Instance, Long> {
	
	public List<Instance> findByOwner (User owner) ;
	
	public List<Instance> findByProject (Project project);
	
}

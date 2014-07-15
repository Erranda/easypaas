package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.domain.Organisation;

@Repository
public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
	
	public Page<Organisation> findAll (Pageable pageable);
	
	public List<Organisation>  findByName (String name);
}

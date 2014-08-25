package com.withinet.opaas.model;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.withinet.opaas.model.domain.User;

@Repository
public interface UserRepository extends  JpaRepository<User, Long> {

	public Page<User> findAll (Pageable pageable);
	
	public User  findByEmail (String email);
	
	public User findByEmailAndPassword (String email, String password);
	
	public List<User> findByAdministrator (User administrator);
	
	public List<User> findByRole (String role);
}

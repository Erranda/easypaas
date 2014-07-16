package com.withinet.opaas.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;

@Entity
public class Organisation implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6803259397295273564L;
	
	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;

	@Column (name="ORGANISATION_NAME", unique = false, nullable = false, length = 255)
	@NotNull
	private String name;
	
	@Column (name="ADDED_BY", unique = false, nullable = false, length = 255)
	@NotNull
	@Email
	private String addedBy;
	
	@Column (name="ORGANISATION_LOCATION", nullable = false, length = 255)
	@NotNull
	private String location;
    
	@OneToMany (mappedBy="organisation", fetch=FetchType.LAZY)	
	@Cascade({org.hibernate.annotations.CascadeType.DELETE})
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private final Set<User> members = new HashSet <User>();
	
	@Column(name="CREATED", nullable=false)	
	@Temporal(TemporalType.DATE)	
	private java.util.Date created;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public java.util.Date getCreated() {
		return created;
	}

	public void setCreated(java.util.Date created) {
		this.created = created;
	}

	public Set<User> getMembers() {
		return members;
	}

	public String getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(String addedBy) {
		this.addedBy = addedBy;
	}
	
}

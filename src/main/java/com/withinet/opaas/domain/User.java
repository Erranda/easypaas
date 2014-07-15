/**
 * "Visual Paradigm: DO NOT MODIFY THIS FILE!"
 * 
 * This is an automatic generated file. It will be regenerated every time 
 * you generate persistence class.
 * 
 * Modifying its content may cause the program not work, or your work may lost.
 */

/**
 * Licensee: 
 * License Type: Evaluation
 */
package com.withinet.opaas.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.Email;

@Entity
public class User implements Serializable {
	
	@Transient
	public String clientApiKey;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7169405079262126560L;

	public User () {
		
	}
	
	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long ID;
		
	@Column(name="USER_FULLNAME", nullable=false, length=255)	
	@NotNull
	@Size (min = 2, max = 30)
	private String fullName;
	
	@NotNull
	@Email
	@Column(name="USER_EMAIL", nullable=false, length=255, unique = true)	
	private String email;
	
	@NotNull
	@Size (min = 2, max = 20)
	@Column(name="USER_PLATFORM_NAME", nullable=false, length=255)	
	private String platformName;
	
	@NotNull
	@Size (min = 5, max = 100)
	@Column(name="USER_PASSWORD", nullable=false, length=255)	
	private String password;
	
	@Column(name="USER_CREATED", nullable=false)	
	@Temporal(TemporalType.DATE)	
	private java.util.Date created;
	
	@Column(name="USER_STATUS", nullable=false)
	@NotNull
	private String status;
	
	@Column(name="USER_API_KEY", nullable=false)
	@NotNull
	private String apiKey;
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.LAZY)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USER_ADMINISTRATOR_ID", referencedColumnName="ID", nullable=true) })	
	private User administrator;
	
	@ManyToOne(targetEntity=Organisation.class, fetch=FetchType.LAZY)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
	@JoinColumns({ @JoinColumn(name="USER_ORGANISATION_ID", referencedColumnName="ID", nullable=true) })	
	private Organisation organisation;
	
	@ManyToOne(targetEntity=UserRole.class, fetch=FetchType.LAZY)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@JoinColumns({ @JoinColumn(name="USER_ROLE_ID", referencedColumnName="ID", nullable=true) })	
	private UserRole assignedRole;
	
	@OneToMany(mappedBy="owner",  fetch=FetchType.LAZY)
	@Cascade({org.hibernate.annotations.CascadeType.DELETE})
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private final Set<UserRole> createdRoles = new HashSet <UserRole> ();
	
	@OneToMany(mappedBy="owner",  fetch=FetchType.LAZY)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private Set<Bundle> bundles = new HashSet <Bundle> ();
			
	@OneToMany(mappedBy="owner", targetEntity=Project.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private Set<Project> projects = new HashSet<Project>();
		
	@OneToMany(mappedBy="administrator", targetEntity=User.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})	
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private Set<User> collaborators = new HashSet<User>();
		
	@OneToMany(mappedBy="owner", targetEntity=Instance.class)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})		
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
	private Set<Instance> instances = new HashSet<Instance>();
	
	
	
	public Long getID() {
		return ID;
	}
	
	public void setID(Long id) {
		ID = id;
	}
	
	public void setFullName(String value) {
		this.fullName = value;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setEmail(String value) {
		this.email = value;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String value) {
		this.password = value;
	}
	
	public String getPassword() {
		return password;
	}
	
	
	public void setCreated(java.util.Date value) {
		this.created = value;
	}
	
	public java.util.Date getCreated() {
		return created;
	}
	
	public void setOrganisation(Organisation value) {
		this.organisation = value;
	}
	
	public Organisation getOrganisation() {
		return organisation;
	}

	public String toString () {
		return ID + ", " + email + ", " + organisation.getName() ;
	}

	public Set<Project> getProjects() {
		return projects;
	}

	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	public Set<User> getCollaborators() {
		return collaborators;
	}

	public void setCollaborators(Set<User> collaborators) {
		this.collaborators = collaborators;
	}

	public Set<Instance> getInstances() {
		return instances;
	}

	public void setInstances(Set<Instance> instances) {
		this.instances = instances;
	}

	public User getAdministrator() {
		return administrator;
	}

	public void setAdministrator(User administrator) {
		this.administrator = administrator;
	}


	public String getPlatformName() {
		return platformName;
	}


	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}


	public Set<Bundle> getBundles() {
		return bundles;
	}


	public void setBundles(Set<Bundle> bundles) {
		this.bundles = bundles;
	}

/*
	public Set<UserRole> getCreatedRoles() {
		return createdRoles;
	}*/


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Set<UserRole> getCreatedRoles() {
		return createdRoles;
	}


	public UserRole getAssignedRole() {
		return assignedRole;
	}


	public void setAssignedRole(UserRole assignedRole) {
		this.assignedRole = assignedRole;
	}


	public String getApiKey() {
		return apiKey;
	}


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}

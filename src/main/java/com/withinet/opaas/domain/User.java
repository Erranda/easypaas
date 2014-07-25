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
	@NotNull (message = "Sorry, please provide your full name")
	@Size (min = 2, max = 30)
	private String fullName;
	
	@NotNull
	@Email
	@Column(name="USER_EMAIL", nullable=false, length=255, unique = false)	
	private String email;
	
	@NotNull
	@Size (min = 2, max = 20)
	@Column(name="USER_PLATFORM_NAME", nullable=false, length=255)	
	private String platformName;
	
	@NotNull
	@Size (min = 6, max = 100)
	@Column(name="USER_PASSWORD", nullable=false, length=255)	
	private String password;
	
	@Column(name="USER_CREATED", nullable=false)	
	@Temporal(TemporalType.DATE)
	private java.util.Date created;
	
	@Column (name="USER_LOCATION", nullable = false, length = 255)
	@NotNull
	private String location;
	
	@Column(name="USER_STATUS", nullable=false)
	private String status;
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="USER_ADMINISTRATOR_ID", referencedColumnName="ID", nullable=true) })	
	private User administrator;
	
	@ManyToOne(targetEntity=UserRole.class, fetch=FetchType.LAZY)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@JoinColumns({ @JoinColumn(name="USER_ROLE_ID", referencedColumnName="ID", nullable=true) })	
	private UserRole assignedRole;
	
	@OneToMany(mappedBy="owner",   fetch=FetchType.EAGER)
	@Cascade({org.hibernate.annotations.CascadeType.DELETE})	
	private final Set<UserRole> createdRoles = new HashSet <UserRole> ();
	
	@OneToMany(mappedBy="owner",  fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})		
	private Set<Bundle> bundles = new HashSet <Bundle> ();
			
	@OneToMany(mappedBy="owner", targetEntity=Project.class,  fetch=FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})		
	private Set<Project> projects = new HashSet<Project>();
		
	@OneToMany(mappedBy="administrator", targetEntity=User.class,  fetch=FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})	
	private Set<User> collaborators = new HashSet<User>();
		
	@OneToMany(mappedBy="owner", targetEntity=Instance.class,  fetch=FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})			
	private Set<Instance> instances = new HashSet<Instance>();
	
	@OneToMany (mappedBy="user", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	private final Set<ProjectTeam> projectTeam = new HashSet <ProjectTeam> ();
	
	
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
		value = value.trim().toLowerCase();
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<ProjectTeam> getProjectTeam() {
		return projectTeam;
	}
	
	@Override
	public boolean equals (Object o) {
		if (!(o instanceof User))
			return false;
		else if (((User) o).getID() == this.getID())
			return true;
		return false;
	}
}

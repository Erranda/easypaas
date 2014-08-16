package com.withinet.opaas.model.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Entity
public class Project implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7879965828859090320L;
	
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	@Column(name="ID", nullable=false)	
	private Long ID;
	
	@Column(name="PROJECT_NAME", nullable=false, length=255)	
	@NotNull
	@Size (min = 2, max = 30)
	private String name;
	
	@Column(name="PROJECT_DETAILS", nullable=true, length=255)	
	private String details;
	
	@Column(name="PROJECT_STATUS", nullable=false, length=255)	
	@NotNull
	private String status;
	
	@Column(name="PROJECT_CREATED", nullable=false, length=255)	
	private Date created;
	
	@Column(name="PROJECT_UPDATED", nullable=false, length=255)
	private Date updated;
	
	@NotNull (message = "Sorry, we need to know who owns this project")
	@ManyToOne (targetEntity=User.class)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})	
	@JoinColumns({ @JoinColumn(name="ADMINISTRATOR_ID", referencedColumnName="ID", nullable=false) })	
	private User owner;
	
	@OneToMany (mappedBy="project", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	private final Set<ProjectBundle> projectBundles = new HashSet <ProjectBundle> ();
	
	@OneToMany (mappedBy="project", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	private final Set<ProjectTeam> projectTeam = new HashSet <ProjectTeam> ();
	
	@OneToMany(mappedBy="project",  fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.DELETE, org.hibernate.annotations.CascadeType.LOCK})	
	private final Set<Instance> instances = new HashSet<Instance> ();
	
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Long getID() {
		return ID;
	}
	
	@Override
	public boolean equals (Object o) {
		if (!(o instanceof Project)) return false;
		if (((Project) o).getID() == ID) return true;
		return false;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<ProjectBundle> getProjectBundles() {
		return projectBundles;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}
	
	public void setID (Long Id){ 
		this.ID = Id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<ProjectTeam> getProjectTeam() {
		return projectTeam;
	}

}
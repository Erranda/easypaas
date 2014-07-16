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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity 
public class Bundle implements Serializable {
	
	private static final long serialVersionUID = 5894978697438021782L;
	
	@Transient
	public String clientApiKey;

	@Column(name="ID", nullable=false)	
	@Id	
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long ID;
	
	@ManyToOne (targetEntity=User.class, fetch=FetchType.LAZY)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="OWNER_ID", referencedColumnName="ID", nullable=false)	
	private User owner;
	
	@Column(name="BUNDLE_SYMBOLIC_NAME", nullable=false, length=255)	
	@NotNull
	@Size (min = 2, max = 30)
	private String symbolicName;
	
	@Column(name="BUNDLE_LOCATION", nullable=false, length=255)	
	@NotNull
	@Size (min = 2, max = 30)
	private String location;
	
	@OneToMany (mappedBy="bundle", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})	
	private final Set<ProjectBundle> projectBundles = new HashSet <ProjectBundle> ();

	public String getSymbolicName() {
		return symbolicName;
	}

	public void setSymbolicName(String symbolicName) {
		this.symbolicName = symbolicName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getID() {
		return ID;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<ProjectBundle> getProjectBundles() {
		return projectBundles;
	}
	
}

package com.withinet.opaas.model.domain;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class UserRole implements Serializable {
	
	private static final long serialVersionUID = -4091270738960503712L;

	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	@ManyToOne (targetEntity=User.class, fetch=FetchType.LAZY)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})
	@JoinColumn(name="OWNER_ID", referencedColumnName="ID", nullable=false)
	private User owner;
	
	@Column(name="ROLE_NAME", nullable=false)
	@NotNull
	@Size (min = 2, max = 30)
	private String name;
	
	@Column(name="ROLE_DESCRIPTION", nullable=false)
	@NotNull
	@Size (min = 2)
	private String description;
	
	@OneToMany (mappedBy="userPermission", fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL, org.hibernate.annotations.CascadeType.LOCK})	
	private Set<RolePermission> permissions = new HashSet <RolePermission> ();

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Set<RolePermission> getRolePermissions() {
		return permissions;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
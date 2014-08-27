package com.withinet.opaas.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Permission implements Serializable {
	
	public Permission () {
		
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4944913179947844644L;

	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="PERMISSION_VALUE", nullable=false, unique=true)
	@NotNull
	private String value;
	
	@Column(name="PERMISSION_DESCRIPTION", nullable=false)
	@NotNull
	private String description;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o == null) return false;
		else if (!(o instanceof Permission))
			return false;
		else if (((Permission) o).getId() != getId())
			return false;
		return true;
	}
}

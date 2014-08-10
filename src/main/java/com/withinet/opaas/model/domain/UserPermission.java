package com.withinet.opaas.model.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class UserPermission implements Serializable {
	
	public UserPermission () {
		
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 4944913179947844644L;

	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="PERMISSION_VALUE", nullable=false)
	@NotNull
	private String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}
}

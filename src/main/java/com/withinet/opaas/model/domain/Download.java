package com.withinet.opaas.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class Download implements Serializable {
	
	public Download () {
		date = new Date ();
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4944913179947844644L;

	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="DATE", nullable=false)
	@NotNull
	private Date date;

	public Long getId() {
		return id;
	}
	
	@Override
	public boolean equals (Object o) {
		if (o == null) return false;
		else if (!(o instanceof Download))
			return false;
		else if (((Download) o).getId() != getId())
			return false;
		return true;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}

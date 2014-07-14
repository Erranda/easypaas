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
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cascade;
import org.hibernate.validator.constraints.URL;


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
	
	@Column(name="PROJECT_WIKI", nullable=true, length=255)	
	@NotNull
	@URL
	private String wiki;
	
	@ManyToOne (targetEntity=User.class)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="ADMINISTRATOR_ID", referencedColumnName="ID", nullable=false) })	
	private User owner;
	
	@ManyToMany
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.ALL})
	    @JoinTable(
	        name = "PROJECT_BUNDLE",
	        joinColumns = @JoinColumn(name = "PROJECT_ID"),
	        inverseJoinColumns = @JoinColumn(name = "BUNDLE_ID")
	)
    private final Set<Bundle> bundles = new HashSet <Bundle> ();
	
	@OneToMany(mappedBy="project",  fetch=FetchType.LAZY)
	@Cascade({org.hibernate.annotations.CascadeType.ALL})
	@org.hibernate.annotations.LazyCollection(org.hibernate.annotations.LazyCollectionOption.TRUE)	
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

	public String getWiki() {
		return wiki;
	}

	public void setWiki(String wiki) {
		this.wiki = wiki;
	}

	public Set<Bundle> getBundles() {
		return bundles;
	}

	public Set<Instance> getInstances() {
		return instances;
	}

}

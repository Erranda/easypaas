package com.withinet.opaas.model.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

@Entity
public class Instance implements Serializable {
	
	private static final long serialVersionUID = 4287545779663725937L;
	
	@Id
	@Column(name="ID", nullable=false)
	@GeneratedValue( strategy = GenerationType.IDENTITY )
	private Long id;
	
	@Column(name="OWNER_NAME", nullable=false)
	@NotNull
	private String ownerName;
	
	@Column(name="CPANEL_URL", nullable=false)
	@NotNull
	@URL
	private String cpanelUrl;
	
	@Column(name="CREATED", nullable=false)	
	@Temporal(TemporalType.DATE)
	private Date created;
	
	@Column(name="HOST_NAME", nullable=false)
	@NotNull
	private String hostName;
	
	@Column(name="CONTAINER_TYPE", nullable=false)
	@NotNull
	private String containerType;
	
	@Column(name="PROJECT_NAME", nullable=false)
	@NotNull
	private String projectName;
	
	@Column(name="PORT", nullable=false)
	@NotNull
	private Integer port;
	
	@Column(name="STATUS", nullable=false)
	@NotNull
	private String status;
	
	@Column(name="WORKING_DIRECTORY")
	private String workingDirectory;
	
	@Column(name="LOG_FILE")
	private String logFile;

	@Column(name="DIRTY")
	private boolean dirty;
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="OWNER_ADMIN_ID", referencedColumnName="ID", nullable=false) 
	@NotNull
	private User administrator;
	
	@ManyToOne(targetEntity=User.class, fetch=FetchType.EAGER)
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumn(name="OWNER_ID", referencedColumnName="ID", nullable=false)
	@NotNull
	private User owner;
	
	@ManyToOne(targetEntity=Project.class, fetch=FetchType.EAGER)	
	@org.hibernate.annotations.Cascade({org.hibernate.annotations.CascadeType.LOCK})	
	@JoinColumns({ @JoinColumn(name="PROJECT_ID", referencedColumnName="ID", nullable=false) })	
	@NotNull
	private Project project;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getCpanelUrl() {
		return cpanelUrl;
	}

	public void setCpanelUrl(String cpanelUrl) {
		this.cpanelUrl = cpanelUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getAdministrator () {
		return administrator;
	}

	public void setAdministrator(User administrator) {
		this.administrator = administrator;
	}

	public void setContainerType(String string) {
		this.containerType = string;
	}

	public void setProjectName(String string) {
		this.projectName = string;
	}
	
	public String getContainerType() {
		return this.containerType;
	}

	public String getProjectName() {
		return this.projectName;
	}

	public String getWorkingDirectory() {
		return workingDirectory;
	}

	public void setWorkingDirectory(String workingDirectory) {
		this.workingDirectory = workingDirectory;
	}

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}
	
	@Override
	public boolean equals (Object o) {
		if (!(o instanceof Instance))
			return false;
		else if (((Instance) o).getId() == getId ()){
			return false;
		}
		return true;
		
	}

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	
}

package com.withinet.opaas.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Immutable
public class ProjectTeam implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 4072070367798093499L;

	@Embeddable
    public static class Id implements Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Column(name = "PROJECT_ID")
        private Long projectId;

        @Column(name = "USER_ID")
        private Long userId;

        public Id() {
        }

        public Id(Long projectId, Long userId) {
            this.projectId = projectId;
            this.userId = userId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.projectId.equals(that.projectId)
                    && this.userId.equals(that.userId);
            }
            return false;
        }

        public int hashCode() {
            return projectId.hashCode() + userId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(updatable = false)
    @NotNull
    private String addedBy;

    @Column(updatable = false)
    @NotNull
    @Temporal(TemporalType.DATE)
    private Date addedOn = new Date();

    @ManyToOne
    @JoinColumn(
        name = "PROJECT_ID",
        insertable = false, updatable = false, referencedColumnName = "ID")
    private Project project;

    @ManyToOne
    @JoinColumn(
        name = "USER_ID",
        insertable = false, updatable = false)
    private User user;


    public ProjectTeam() {
    }

    public ProjectTeam(String addedByUsername,
                           Project project,
                           User user) {

        // Set fields
        this.addedBy = addedByUsername;
        this.project = project;
        this.user = user;
        this.addedOn = new Date ();

        // Set identifier values
        this.id.projectId = project.getID();
        this.id.userId = user.getID();

        // Guarantee referential integrity if made bidirectional
        project.getProjectTeam().add(this);
        user.getProjectTeam().add(this);
    }

    public Id getId() {
        return id;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public Date getAddedOn() {
        return addedOn;
    }

    public Project getProject() {
        return this.project;
    }

    public User getUser() {
        return this.user;
    }

}
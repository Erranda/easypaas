package com.withinet.opaas.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Immutable
public class ProjectBundle {

    @Embeddable
    public static class Id implements Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Column(name = "PROJECT_ID")
        private Long projectId;

        @Column(name = "BUNDLE_ID")
        private Long bundleId;

        public Id() {
        }

        public Id(Long projectId, Long bundleId) {
            this.projectId = projectId;
            this.bundleId = bundleId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.projectId.equals(that.projectId)
                    && this.bundleId.equals(that.bundleId);
            }
            return false;
        }

        public int hashCode() {
            return projectId.hashCode() + bundleId.hashCode();
        }
    }

    @EmbeddedId
    private Id id = new Id();

    @Column(updatable = false)
    @NotNull
    private String addedBy;

    @Column(updatable = false)
    @NotNull
    private Date addedOn = new Date();

    @ManyToOne
    @JoinColumn(
        name = "PROJECT_ID",
        insertable = false, updatable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(
        name = "BUNDLE_ID",
        insertable = false, updatable = false)
    private Bundle bundle;


    public ProjectBundle() {
    }

    public ProjectBundle(String addedByUsername,
                           Project project,
                           Bundle bundle) {

        // Set fields
        this.addedBy = addedByUsername;
        this.project = project;
        this.bundle = bundle;
        this.addedOn = new Date ();
        // Set identifier values
        this.id.projectId = project.getID();
        this.id.bundleId = bundle.getID();

        // Guarantee referential integrity if made bidirectional
        project.getProjectBundles().add(this);
        bundle.getProjectBundles().add(this);
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

    public Bundle getBundle() {
        return this.bundle;
    }

}
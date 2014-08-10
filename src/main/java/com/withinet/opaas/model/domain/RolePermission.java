package com.withinet.opaas.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Immutable
public class RolePermission {

    @Embeddable
    public static class Id implements Serializable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Column(name = "ROLE_ID")
        private Long roleId;

        @Column(name = "PERMISSION_ID")
        private Long permissionId;

        public Id() {
        }

        public Id(Long roleId, Long permissionId) {
            this.roleId = roleId;
            this.permissionId = permissionId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.roleId.equals(that.roleId)
                    && this.permissionId.equals(that.permissionId);
            }
            return false;
        }

        public int hashCode() {
            return roleId.hashCode() + permissionId.hashCode();
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
        name = "ROLE_ID",
        insertable = false, updatable = false)
    private UserRole userRole;

    @ManyToOne
    @JoinColumn(
        name = "PERMISSION_ID",
        insertable = false, updatable = false)
    private UserPermission userPermission;


    public RolePermission() {
    }

    public RolePermission(String addedByUsername,
                           UserRole role,
                           UserPermission userPermission) {

        // Set fields
        this.addedBy = addedByUsername;
        this.userRole = role;
        this.userPermission = userPermission;

        // Set identifier values
        this.id.roleId = role.getId();
        this.id.permissionId = userPermission.getId();

        // Guarantee referential integrity if made bidirectional
        role.getRolePermissions().add(this);
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

    public UserRole getRole() {
        return this.userRole;
    }

    public UserPermission getPermission() {
        return this.userPermission;
    }

}
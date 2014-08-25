package com.withinet.opaas.model.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Entity
@org.hibernate.annotations.Immutable
public class RolePermission implements Serializable {

    @Embeddable
    public static class Id implements Serializable {

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
    private Role role;

    @ManyToOne
    @JoinColumn(
        name = "PERMISSION_ID",
        insertable = false, updatable = false)
    private Permission permission;


    public RolePermission() {
    }

    public RolePermission(String addedByUsername,
                           Role role,
                           Permission userPermission) {

        // Set fields
        this.addedBy = addedByUsername;
        this.role = role;
        this.permission = userPermission;

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

    public Role getRole() {
        return this.role;
    }

    public Permission getPermission() {
        return this.permission;
    }

}
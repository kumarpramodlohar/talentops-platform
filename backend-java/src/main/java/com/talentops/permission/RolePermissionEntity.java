package com.talentops.permission;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "role_permissions")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermissionEntity {

    @EmbeddedId
    private RolePermissionId id;


    public RolePermissionEntity(String roleCode, String permissionCode) {
        this.id = new RolePermissionId(roleCode, permissionCode);
    }
    public String getRoleCode() {
        return id.getRoleCode();
    }
   public String getPermissionCode() {
        return id.getPermissionCode();
   }
}

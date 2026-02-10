package com.talentops.permission;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RolePermissionId implements Serializable {

    @Column(name = "role_code", length = 50)
    private String roleCode;
    @Column(name = "permission_code", length = 100)
    private String permissionCode;

    public RolePermissionId(String roleCode, String permissionCode) {
        this.roleCode = roleCode;
        this.permissionCode = permissionCode;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public String getPermissionCode() {
        return permissionCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RolePermissionId)) return false;

        RolePermissionId that = (RolePermissionId) o;

        if (!roleCode.equals(that.roleCode)) return false;
        return Objects.equals(permissionCode, that.permissionCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode, permissionCode);
    }
}

package com.talentops.permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, RolePermissionId> {

    /**
     * Checks if any of the given roles has the specified permission.
     *  This is the HOT PATH for permission checks.
     */

    @Query("SELECT CASE WHEN COUNT(rp) > 0 THEN true ELSE false END " +
           "FROM RolePermissionEntity rp " +
           "WHERE rp.id.roleCode IN :roles AND rp.id.permissionCode = :permission")
    boolean existsPermissionForRoles(@Param("roles")Collection<String> roles, @Param("permission") String permission);
}

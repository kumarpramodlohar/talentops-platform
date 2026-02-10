package com.talentops.permission;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@Slf4j
public class PermissionService {
    private final RolePermissionRepository rolePermissionRepository;

    public PermissionService(RolePermissionRepository rolePermissionRepository) {
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public boolean hasPermission(Set<String> roleCodes, String permission) {
        log.debug("inside PermissionService hasPermission");
        log.info("Roles: {}, Permission: {}", roleCodes, permission);
        if (roleCodes == null || roleCodes.isEmpty()) {
            return false; // secure by default
        }
        return rolePermissionRepository.existsPermissionForRoles(roleCodes, permission);
    }
}

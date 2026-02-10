package com.talentops.permission;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

@Component
@Slf4j
public class PermissionEvaluatorImpl implements PermissionEvaluator {
    private final PermissionService permissionService;
    public PermissionEvaluatorImpl(PermissionService permissionService) {
        this.permissionService = permissionService;
    }
    @Override
    public boolean hasPermission(@NonNull Authentication authentication, @NonNull Object targetDomainObject, @NonNull Object permission) {
        log.debug("PermissionEvaluatorImpl hasPermission");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) { return false; }

         String permissionCode = String.valueOf(permission);

        Set<String> roles = auth.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull).map(role ->
                        role.replace("ROLE_", ""))
                .collect(java.util.stream.Collectors.toSet());

        log.info("Roles: {}, Permission: {}", roles,  permissionCode);

        return permissionService.hasPermission(roles, permissionCode);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }

}

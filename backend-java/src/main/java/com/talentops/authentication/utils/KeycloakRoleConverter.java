package com.talentops.authentication.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.Collection;
import java.util.Collections;

import java.util.Map;
import java.util.stream.Collectors;


/**
 * Converter to extract roles from Keycloak JWT tokens and map them to Spring Security authorities.
 * Supports both realm roles and client-specific roles.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(KeycloakRoleConverter.class);

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final String clientId;
    private final boolean includeRealmRoles;




    /**
     * Full constructor with configuration options.
     *
     * @param clientId the Keycloak client ID

     */
    public KeycloakRoleConverter(String clientId) {
        this.clientId = clientId;
        this.includeRealmRoles = true;
    }

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        try {
            Collection<String> realmRoles = includeRealmRoles ? extractRealmRoles(jwt) : Collections.emptyList();

            return realmRoles.stream()
                    .distinct()
                    .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role.toUpperCase()))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            log.error("Error extracting roles from JWT token", e);
            return Collections.emptyList();
        }
    }

    /**
     * Extracts realm-level roles from the JWT token.
     *
     * @param jwt the JWT token
     * @return collection of realm roles
     */
    private Collection<String> extractRealmRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim(REALM_ACCESS_CLAIM);

        if (realmAccess == null || !realmAccess.containsKey(ROLES_CLAIM)) {
            log.debug("No realm roles found in JWT token");
            return Collections.emptyList();
        }

        Object rolesObj = realmAccess.get(ROLES_CLAIM);

        if (rolesObj instanceof Collection<?> roles) {
            return roles.stream()
                    .filter(role -> role instanceof String)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }

        log.warn("Realm roles claim is not a collection: {}", rolesObj.getClass());
        return Collections.emptyList();
    }


}
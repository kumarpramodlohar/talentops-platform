package com.talentops.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Converter to extract roles from Keycloak JWT tokens and map them to Spring Security authorities.
 * Supports both realm roles and client-specific roles.
 */
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final Logger log = LoggerFactory.getLogger(KeycloakRoleConverter.class);

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    private final String clientId;
    private final boolean includeRealmRoles;
    private final boolean includeClientRoles;

    /**
     * Default constructor that extracts both realm and client roles.
     */
    public KeycloakRoleConverter() {
        this(null, true, true);
    }

    /**
     * Constructor with client ID for extracting client-specific roles.
     *
     * @param clientId the Keycloak client ID
     */
    public KeycloakRoleConverter(String clientId) {
        this(clientId, true, true);
    }

    /**
     * Full constructor with configuration options.
     *
     * @param clientId the Keycloak client ID
     * @param includeRealmRoles whether to include realm-level roles
     * @param includeClientRoles whether to include client-specific roles
     */
    public KeycloakRoleConverter(String clientId, boolean includeRealmRoles, boolean includeClientRoles) {
        this.clientId = clientId;
        this.includeRealmRoles = includeRealmRoles;
        this.includeClientRoles = includeClientRoles;
    }

    @Override
    public Collection<GrantedAuthority> convert(@NonNull Jwt jwt) {
        try {
            Collection<String> realmRoles = includeRealmRoles ? extractRealmRoles(jwt) : Collections.emptyList();
            Collection<String> clientRoles = includeClientRoles ? extractClientRoles(jwt) : Collections.emptyList();

            return Stream.concat(realmRoles.stream(), clientRoles.stream())
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

    /**
     * Extracts client-specific roles from the JWT token.
     *
     * @param jwt the JWT token
     * @return collection of client roles
     */
    private Collection<String> extractClientRoles(Jwt jwt) {
        if (clientId == null || clientId.isBlank()) {
            log.debug("No client ID configured, skipping client roles extraction");
            return Collections.emptyList();
        }

        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS_CLAIM);

        if (resourceAccess == null || !resourceAccess.containsKey(clientId)) {
            log.debug("No resource access found for client: {}", clientId);
            return Collections.emptyList();
        }

        Object clientAccessObj = resourceAccess.get(clientId);

        if (!(clientAccessObj instanceof Map)) {
            log.warn("Client access is not a map for client: {}", clientId);
            return Collections.emptyList();
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> clientAccess = (Map<String, Object>) clientAccessObj;

        if (!clientAccess.containsKey(ROLES_CLAIM)) {
            log.debug("No roles found for client: {}", clientId);
            return Collections.emptyList();
        }

        Object rolesObj = clientAccess.get(ROLES_CLAIM);

        if (rolesObj instanceof Collection<?> roles) {
            return roles.stream()
                    .filter(role -> role instanceof String)
                    .map(String.class::cast)
                    .collect(Collectors.toList());
        }

        log.warn("Client roles claim is not a collection for client: {}", clientId);
        return Collections.emptyList();
    }
}
package com.talentops.constants;

public class Constant {
    public static final String BASE_URL = "http://localhost:8080";
    public static final String REALM_BASE_PATH = "/realms/{realm}";
    public static final String ADMIN_REALM_BASE_PATH = "/admin/realms/{realm}";
    public static final String TOKEN_PATH = REALM_BASE_PATH + "/protocol/openid-connect/token";
    public static final String USERS_PATH = ADMIN_REALM_BASE_PATH + "/users";
    public static final String RESET_PASSWORD_PATH = USERS_PATH + "/{id}/reset-password";
    public static final String ROLE_BY_NAME_PATH = ADMIN_REALM_BASE_PATH + "/roles/{roleName}";
    public static final String ASSIGN_REALM_ROLE_PATH = USERS_PATH + "/{id}/role-mappings/realm";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String KEYCLOAK_CLIENT_ID = "client_id";
    public static final String KEYCLOAK_CLIENT_SECRET = "client_secret";

}

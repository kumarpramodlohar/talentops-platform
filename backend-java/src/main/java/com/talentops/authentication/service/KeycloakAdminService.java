package com.talentops.authentication.service;

import com.talentops.authentication.dto.KeycloakCreateUserRequest;

public interface KeycloakAdminService {
    String createUser(KeycloakCreateUserRequest payload);
    String getUserIdByEmail(String email);
    void setPassword(String userId, String newPassword);
    void sendVerifyEmail(String userId);
    void sendResetPasswordEmail(String userId);
    void assignRealmRole(String userId, String roleName);
    String getClientUuidByClientId(String clientId);
    void assignClientRole(String userId, String clientId, String roleName);
}

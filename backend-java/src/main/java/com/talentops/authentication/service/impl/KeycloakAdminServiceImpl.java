package com.talentops.authentication.service.impl;

import com.talentops.authentication.config.KeycloakAdminClientProperties;
import com.talentops.authentication.dto.KeycloakCreateUserRequest;
import com.talentops.authentication.service.KeycloakAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.talentops.constants.Constant.*;

@Service
@RequiredArgsConstructor
public class KeycloakAdminServiceImpl implements KeycloakAdminService {

    private final WebClient webClient;
    private final KeycloakAdminClientProperties adminClientProperties;

    @Value("${keycloak.server-url}")
    private String serverUrl;

    @Value("${keycloak.realm}")
    private String realm;

    private String tokenUrl() {
        return serverUrl + TOKEN_PATH.replace("{realm}", realm);
    }

    private String userUrl() {
        return serverUrl + USERS_PATH.replace("{realm}", realm);
    }

    private String resetPasswordUrl(String userId) {
        return serverUrl + RESET_PASSWORD_PATH.replace("{realm}", realm)
                .replace("{userId}", userId);
    }

    private String roleByNameUrl(String roleName) {
        return serverUrl + ROLE_BY_NAME_PATH.replace("{realm}", realm)
                .replace("{roleName}", roleName);
    }

    private String assignRealmRoleUrl(String userId) {
        return serverUrl + ASSIGN_REALM_ROLE_PATH.replace("{realm}", realm)
                .replace("{userId}", userId);
    }

    private String assignClientRoleUrl(String userId, String clientId) {
        return serverUrl + USERS_PATH.replace("{realm}", realm)
                .replace("{id}", userId) + "/role-mappings/clients/" + clientId;
    }

    private String resetPasswordEmailUrl(String userId) {
        return serverUrl + USERS_PATH.replace("{realm}", realm)
                .replace("{id}", userId) + "/execute-actions-email";
    }

    private String sendVerifyEmailUrl(String userId) {
        return serverUrl + USERS_PATH.replace("{realm}", realm)
                .replace("{id}", userId) + "/send-verify-email";
    }


    public String getAdminToken() {
        String clientId = adminClientProperties.getClientId();
        String clientSecret = adminClientProperties.getClientSecret();

       Map response = webClient.post()
               .uri(tokenUrl())
               .contentType(MediaType.APPLICATION_FORM_URLENCODED)
               .body(BodyInserters.fromFormData("grant_type", "client_credentials")
                       .with(KEYCLOAK_CLIENT_ID, clientId)
                       .with(KEYCLOAK_CLIENT_SECRET, clientSecret))
               .retrieve()
               .bodyToMono(Map.class)
               .block();



       if (response == null) {
              throw new RuntimeException("Failed to retrieve access token from Keycloak");
       }
       return response.get("access_token").toString();
    }

    @Override
    public String createUser(KeycloakCreateUserRequest payload) {
        String token = getAdminToken();
        String location = Objects.requireNonNull(webClient.post()
                .uri(userUrl())
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX+ token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.justOrEmpty(clientResponse.headers().asHttpHeaders().getFirst("Location"));
                    }
                    return Mono.justOrEmpty(clientResponse.bodyToMono(String.class).flatMap(body -> Mono.error(
                            new RuntimeException("Failed to create user: " + clientResponse.statusCode() + " body= " + body))));
                }).block()).toString();
       if(location == null) {
              throw new RuntimeException("Keycloak did not return a Location header for the created user");
       }

       return location.substring(location.lastIndexOf("/") + 1);
    }

    @Override
    public String getUserIdByEmail(String email) {
        String token = getAdminToken();

        List<Map> users = webClient.get().uri(userUrl() + "?email=" + email)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .retrieve()
                .bodyToFlux(Map.class)
                .collectList()
                .block();

        if (users == null || users.isEmpty()) {
            throw new RuntimeException("No user found with email: " + email);
        }
        Object userId = users.getFirst().get("id");

        if (userId == null) {
            throw new RuntimeException("Keycloak user object missing id");
        }

        return userId.toString();
    }

    @Override
    public void setPassword(String userId, String newPassword) {
            String token = getAdminToken();

            webClient.put()
                    .uri(resetPasswordUrl(userId))
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "type", "password",
                            "value", newPassword,
                            "temporary", false
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

    }

    @Override
    public void sendVerifyEmail(String userId) {
        String token = getAdminToken();
        webClient.post()
                .uri(sendVerifyEmailUrl(userId))
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    @Override
    public void sendResetPasswordEmail(String userId) {
        String token = getAdminToken();
        List<String> actions = List.of("UPDATE_PASSWORD");
        webClient.post()
                .uri(resetPasswordEmailUrl(userId))
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(actions) // Keycloak expects a JSON array of actions, e.g. ["UPDATE_PASSWORD"]
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    @Override
    public void assignRealmRole(String userId, String roleName) {
        String token = getAdminToken();
        Map<String, Object> roleRepresentation = webClient.get()
                .uri(roleByNameUrl(roleName))
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (roleRepresentation == null) {
            throw new RuntimeException("Failed to retrieve role representation for role: " + roleName);
        }

        List<Map<String, Object>> roles = List.of(
                Map.of(
                        "id", roleRepresentation.get("id"),
                        "name", roleRepresentation.get("name")
                )
        ); // Keycloak expects a JSON array of role representations

        webClient.post()
                .uri(assignRealmRoleUrl(userId))
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roles) // Keycloak expects a JSON array of role representations
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }

    @Override
    public String getClientUuidByClientId(String clientId) {
            String token = getAdminToken();
            List<Map> clients = webClient.get()
                    .uri(serverUrl + ADMIN_REALM_BASE_PATH.replace("{realm}", realm) + "/clients?clientId=" + clientId)
                    .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                    .retrieve()
                    .bodyToFlux(Map.class)
                    .collectList()
                    .block();

            if (clients == null || clients.isEmpty()) {
                throw new RuntimeException("No client found with clientId: " + clientId);
            }
            Object clientUuid = clients.getFirst().get("id");

            if (clientUuid == null) {
                throw new RuntimeException("Keycloak client object missing id");
            }

            return clientUuid.toString();
    }

    @Override
    public void assignClientRole(String userId, String clientId, String roleName) {
        String token = getAdminToken();
        String clientUuid = getClientUuidByClientId(clientId);

        Map<String, Object> roleRepresentation = webClient.get()
                .uri(serverUrl + ADMIN_REALM_BASE_PATH.replace("{realm}", realm) + "/clients/" + clientUuid + "/roles/" + roleName)
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (roleRepresentation == null) {
            throw new RuntimeException("Failed to retrieve role representation for client role: " + roleName);
        }

        List<Map<String, Object>> roles = List.of(
                Map.of(
                        "id", roleRepresentation.get("id"),
                        "name", roleRepresentation.get("name")
                )
        ); // Keycloak expects a JSON array of role representations

        webClient.post()
                .uri(assignClientRoleUrl(userId, clientUuid))
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(roles) // Keycloak expects a JSON array of role representations
                .retrieve()
                .bodyToMono(Void.class)
                .block();

    }


}



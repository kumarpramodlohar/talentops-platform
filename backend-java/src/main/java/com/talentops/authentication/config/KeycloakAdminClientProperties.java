package com.talentops.authentication.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "keycloak.admin-client")
@Getter
@Setter
public class KeycloakAdminClientProperties {
    private String clientId;
    private String clientSecret;
}

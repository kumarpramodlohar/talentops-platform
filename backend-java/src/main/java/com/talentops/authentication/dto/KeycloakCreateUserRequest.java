package com.talentops.authentication.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeycloakCreateUserRequest {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}

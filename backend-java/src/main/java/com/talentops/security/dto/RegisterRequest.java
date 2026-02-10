package com.talentops.security.dto;

import com.talentops.domain.user.dto.Role;

public record RegisterRequest(String email, String password, Role requestedRole) {
}

package com.talentops.common.dto;

import com.talentops.user.Role;

public record RegisterRequest(String email, String password, Role requestedRole) {
}

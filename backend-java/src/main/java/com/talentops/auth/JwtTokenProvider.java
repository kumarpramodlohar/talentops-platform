package com.talentops.auth;

import com.talentops.user.UserEntity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {
    private static final String SECRET = "";
    private static final long EXPIERATION = 1000 * 60 * 60; // 1 hour


}

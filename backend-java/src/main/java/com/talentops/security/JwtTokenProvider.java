package com.talentops.security;

import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {
    private static final String SECRET = "";
    private static final long EXPIERATION = 1000 * 60 * 60; // 1 hour


}

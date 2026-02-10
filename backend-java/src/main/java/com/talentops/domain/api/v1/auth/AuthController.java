package com.talentops.domain.api.v1.auth;

import com.talentops.domain.auth.service.impl.AuthService;
import com.talentops.security.dto.RegisterRequest;
import com.talentops.domain.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/admin/create")
    public ResponseEntity<Void> createAdmin(@RequestBody RegisterRequest request) {
        UserEntity.createAdmin(request.email(), request.password());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //@PreAuthorize("hasRole('ADMIN')")
    @PostMapping("register")
    public ResponseEntity<Void> registerUser(@RequestBody RegisterRequest request) {
        authService.register(request);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/my-roles")
    public ResponseEntity<Map<String, Object>> getMyRoles(Authentication authentication) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("authenticated", authentication.isAuthenticated());
        map.put("authorities", authentication.getAuthorities());
        map.put("principal", authentication.getPrincipal());

        return ResponseEntity.of(java.util.Optional.of(map));
    }
}

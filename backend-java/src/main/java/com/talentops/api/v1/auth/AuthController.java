package com.talentops.api.v1.auth;

import com.talentops.auth.AuthService;
import com.talentops.common.dto.RegisterRequest;
import com.talentops.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

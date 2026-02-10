package com.talentops.domain.auth.service.impl;

import com.talentops.security.dto.RegisterRequest;
import com.talentops.domain.user.dto.Role;
import com.talentops.domain.user.entity.UserEntity;
import com.talentops.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegisterRequest request) {
        if(userRepository.findByEmail(request.email()).isPresent()){
            throw new IllegalStateException("Email already exists");
        }
        if(request.requestedRole() == Role.ADMIN) {
            throw new IllegalStateException("Admin role cannot be self-registered");
        }
        if(request.requestedRole() == null) {
            throw new  IllegalStateException("Role required");
        }

        UserEntity user = UserEntity.create(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.requestedRole()
        );

        userRepository.save(user);
    }
}

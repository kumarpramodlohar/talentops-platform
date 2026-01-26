package com.talentops.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "users")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA only
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

//    @ManyToOne(fetch =  FetchType.LAZY, optional = false)
//    @JoinColumn(name = "role_id")
//    private RoleEntity role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Role role;

    @Column(nullable = false)
    private String status;

    private LocalDateTime lastLogin;

    @OneToOne(mappedBy = "user",
    cascade = CascadeType.ALL,
    orphanRemoval = true)
    private UserProfileEntity profile;

    public static UserEntity create(String email, String passwordHash, Role requestedRole) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.passwordHash = passwordHash;
        userEntity.role = requestedRole;
        userEntity.status = "ACTIVE";
        return  userEntity;
    }

    public static UserEntity createAdmin(String email, String passwordHash) {
        UserEntity userEntity = new UserEntity();
        userEntity.email = email;
        userEntity.passwordHash = passwordHash;
        userEntity.role=Role.ADMIN;
        userEntity.status = "ACTIVE";
        return userEntity;
    }

    // =========================
    // Behavior (State Changes)
    //==========================

    public void changePassword(String newPasswordHash) {
        validatePasswordHash(newPasswordHash);
        this.passwordHash = newPasswordHash;
    }

    public void promoteToAdmin() {
        if (this.role == Role.ADMIN) {
            throw new IllegalStateException("Admin role cannot be self-registered");
        }
        this.role = Role.ADMIN;
    }
    public void demoteToUser() {
        if (this.role == Role.USER) {
            throw new IllegalStateException("User role cannot be self-registered");
        }
        this.role = Role.USER;
    }

    // ==========================
    // Validation (Domain Rules)
    // ==========================

    private void validateEmail (String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalStateException("Invalid email");
        }
    }

    private void validatePasswordHash (String passwordHash) {
        if (passwordHash == null || passwordHash.length() < 20) {
            throw new IllegalStateException("Invalid password hash");
        }
    }

    public void recordLogin() {
        this.lastLogin = LocalDateTime.now();
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }

    public void attachProfile(UserProfileEntity profile) {
        this.profile = profile;
    }


    // ---Getters ---


    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Role getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public UserProfileEntity getProfile() {
        return profile;
    }
}

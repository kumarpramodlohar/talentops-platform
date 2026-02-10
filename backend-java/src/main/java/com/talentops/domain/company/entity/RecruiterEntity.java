package com.talentops.domain.company.entity;

import com.talentops.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "recruiters")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruiterEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    private String specialization;

    public static RecruiterEntity assign(UserEntity user, String specialization) {
        RecruiterEntity recruiter = new RecruiterEntity();
        recruiter.user = user;
        recruiter.specialization = specialization;
        return recruiter;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getSpecialization() {
        return specialization;
    }
}

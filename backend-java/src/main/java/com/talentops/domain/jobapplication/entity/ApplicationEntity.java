package com.talentops.domain.jobapplication.entity;

import com.talentops.domain.job.entity.JobEntity;
import com.talentops.domain.user.entity.UserEntity;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "applications")
public class ApplicationEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobEntity jobEntity;

    @Column(name = "status", length = 30, nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }

    protected  ApplicationEntity() {
        // JPA only
    }

    public ApplicationEntity(UUID id, UserEntity userEntity, JobEntity jobEntity, String status, Instant createdAt) {
        this.id = id;
        this.userEntity = userEntity;
        this.jobEntity = jobEntity;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public JobEntity getJob() {
        return jobEntity;
    }

    public String getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

package com.talentops.domain.candidate.entity;


import com.talentops.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "candidates")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private Integer totalExperience;
    private BigDecimal currentCtc;
    private BigDecimal expectedCtc;
    private Integer noticePeriod;
    private String resumeUrl;

    @OneToMany(
            mappedBy = "candidate",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final List<CandidateSkillEntity> skills = new ArrayList<>();

    public static CandidateEntity create(UserEntity user) {
        CandidateEntity candidate = new CandidateEntity();
        candidate.user = user;
        return candidate;
    }

    public void addSkill(String skill) {
        skills.add(CandidateSkillEntity.create(this, skill));
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public Integer getTotalExperience() {
        return totalExperience;
    }

    public BigDecimal getCurrentCtc() {
        return currentCtc;
    }

    public BigDecimal getExpectedCtc() {
        return expectedCtc;
    }

    public Integer getNoticePeriod() {
        return noticePeriod;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public List<CandidateSkillEntity> getSkills() {
        return skills;
    }
}

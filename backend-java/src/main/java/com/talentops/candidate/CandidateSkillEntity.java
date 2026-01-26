package com.talentops.candidate;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "candidate_skills")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CandidateSkillEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false)
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "candidate_id", nullable = false)
    private CandidateEntity candidate;

    @Column(nullable = false)
    private String skill;

    // =====================
    // Factory (package-private)
    // =====================
    static CandidateSkillEntity create(CandidateEntity candidate, String skill) {
        CandidateSkillEntity cs = new CandidateSkillEntity();
        cs.candidate = candidate;
        cs.skill = skill;
        return cs;
    }

    public UUID getId() {
        return id;
    }

    public CandidateEntity getCandidate() {
        return candidate;
    }

    public String getSkill() {
        return skill;
    }
}


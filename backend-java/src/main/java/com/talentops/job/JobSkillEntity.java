package com.talentops.job;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "job_skills")
@Access(AccessType.FIELD)

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobSkillEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private JobEntity job;

    private String skill;

    static JobSkillEntity create(JobEntity job, String skill) {
        JobSkillEntity js = new JobSkillEntity();
        js.job = job;
        js.skill = skill;
        return js;
    }

    public UUID getId() {
        return id;
    }

    public JobEntity getJob() {
        return job;
    }

    public String getSkill() {
        return skill;
    }
}

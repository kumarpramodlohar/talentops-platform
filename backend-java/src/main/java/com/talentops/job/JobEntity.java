package com.talentops.job;

import com.talentops.company.CompanyEntity;
import com.talentops.user.UserEntity;
import com.talentops.user.UserProfileEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "jobs")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class JobEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserProfileEntity createdBy;


    @Column(name = "title", length = 200)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    private Integer experienceMin;
    private Integer experienceMax;

    private BigDecimal salaryMin;
    private BigDecimal salaryMax;

    private String location;
    private String status;
    @Column(name = "created_at")
    private Instant createdAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true )
    private final List<JobSkillEntity> skills = new ArrayList<>();

    public static JobEntity create(CompanyEntity company, UserEntity creator, String title) {
        JobEntity jobEntity = new JobEntity();
        jobEntity.company = company;
        jobEntity.createdBy = creator.getProfile();
        jobEntity.title = title;
        jobEntity.status = "DRAFT";
        return jobEntity;
    }

    public void publish() {
        this.status = "OPEN";
    }

    public void close() {
        this.status= "CLOSED";
    }

    public void addSkill(String skill) {
        skills.add(JobSkillEntity.create(this, skill));
    }


//
//    protected JobEntity() {
//        // JPA only
//    }

    public JobEntity(UUID id, String title, String description, Instant createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public UserProfileEntity getCreatedBy() {
        return createdBy;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getExperienceMin() {
        return experienceMin;
    }

    public Integer getExperienceMax() {
        return experienceMax;
    }

    public BigDecimal getSalaryMin() {
        return salaryMin;
    }

    public BigDecimal getSalaryMax() {
        return salaryMax;
    }

    public String getLocation() {
        return location;
    }

    public String getStatus() {
        return status;
    }

    public List<JobSkillEntity> getSkills() {
        return skills;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

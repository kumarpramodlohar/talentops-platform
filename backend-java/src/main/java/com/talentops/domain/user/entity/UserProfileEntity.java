package com.talentops.domain.user.entity;

import com.talentops.domain.job.entity.JobEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_profiles")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProfileEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    private  List<JobEntity> jobs = new ArrayList<>();

    private String firstName;
    private String lastName;
    private String phone;

    private String profileImageUrl;

    private String linkedinUrl;

    private String githubUrl;

    static UserProfileEntity create(UserEntity user, String firstName, String lastName) {
        UserProfileEntity profile = new UserProfileEntity();
        profile.user = user;
        profile.firstName = firstName;
        profile.lastName = lastName;
        return profile;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getLinkedinUrl() {
        return linkedinUrl;
    }

    public String getGithubUrl() {
        return githubUrl;
    }

    public List<JobEntity> getJobs() {
        return jobs;
    }
}

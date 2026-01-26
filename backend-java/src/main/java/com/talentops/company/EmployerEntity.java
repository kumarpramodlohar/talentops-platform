package com.talentops.company;

import com.talentops.user.UserEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "employers")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmployerEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private CompanyEntity company;

    private String designation;

    public static EmployerEntity assign(UserEntity user, CompanyEntity company, String designation) {
        EmployerEntity employer = new EmployerEntity();
        employer.user = user;
        employer.company = company;
        employer.designation = designation;
        return employer;
    }

    public UUID getId() {
        return id;
    }

    public UserEntity getUser() {
        return user;
    }

    public CompanyEntity getCompany() {
        return company;
    }

    public String getDesignation() {
        return designation;
    }
}


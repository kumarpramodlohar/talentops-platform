package com.talentops.company;

import com.talentops.job.JobEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "companies")
@Access(AccessType.FIELD)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyEntity {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;
    private String industry;
    private String website;
    private String size;

    @OneToMany (mappedBy = "company")
    private final List<JobEntity> jobs = new ArrayList<>();


    public static CompanyEntity create(String name, String industry) {
        CompanyEntity companyEntity = new CompanyEntity();
        companyEntity.name = name;
        companyEntity.industry = industry;
        return companyEntity;
    }
}

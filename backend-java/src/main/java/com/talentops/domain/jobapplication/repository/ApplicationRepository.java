package com.talentops.domain.jobapplication.repository;

import com.talentops.domain.jobapplication.entity.ApplicationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ApplicationRepository extends JpaRepository<ApplicationEntity, UUID>
{
}

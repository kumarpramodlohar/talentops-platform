package com.talentops.domain.api.v1.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/api/v1/jobs")
public class JobController {

    @PreAuthorize("hasPermission(null,'CREATE_JOB')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> createJob() {
        log.info("inside the jobs ========");
        return  ResponseEntity.ok(Map.of("message", "Job created successfully"));
    }
}

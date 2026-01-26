-- -- =====================================================
-- -- ROLES
-- -- =====================================================
-- INSERT INTO roles (id, name, description)
-- VALUES
--     ('11111111-1111-1111-1111-111111111111', 'SUPER_ADMIN', 'Platform super admin'),
--     ('22222222-2222-2222-2222-222222222222', 'ADMIN', 'Company admin'),
--     ('33333333-3333-3333-3333-333333333333', 'RECRUITER', 'Recruiter'),
--     ('44444444-4444-4444-4444-444444444444', 'EMPLOYER', 'Hiring manager'),
--     ('55555555-5555-5555-5555-555555555555', 'CANDIDATE', 'Job applicant')
--     ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- USERS
-- Password = "password" (BCrypt placeholder)
-- =====================================================
INSERT INTO users (id, email, password_hash, role, status)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'superadmin@talentops.com', '$2a$10$hash', 'SUPER_ADMIN', 'ACTIVE'),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'admin@company.com', '$2a$10$hash', 'ADMIN', 'ACTIVE'),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', 'recruiter@company.com', '$2a$10$hash', 'RECRUITER', 'ACTIVE'),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', 'employer@company.com', '$2a$10$hash', 'EMPLOYER', 'ACTIVE'),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'candidate@gmail.com', '$2a$10$hash', 'CANDIDATE', 'ACTIVE'),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', 'candidate@gmail.com', '$2a$10$hash', 'USER', 'ACTIVE')

    ON CONFLICT (email) DO NOTHING;


-- =====================================================
-- USER PROFILES
-- =====================================================
INSERT INTO user_profiles (id, user_id, first_name, last_name, phone)
VALUES
    (uuid_generate_v4(), 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Rahul', 'Sharma', '9999999999'),
    (uuid_generate_v4(), 'dddddddd-dddd-dddd-dddd-dddddddddddd', 'Anita', 'Verma', '8888888888'),
    (uuid_generate_v4(), 'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', 'Pramod', 'Lohar', '7777777777')
    ON CONFLICT DO NOTHING;

-- =====================================================
-- COMPANY
-- =====================================================
INSERT INTO companies (id, name, industry, website, size)
VALUES
    ('99999999-9999-9999-9999-999999999999', 'TalentOps Technologies', 'Software', 'https://talentops.io', 'MID')
    ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- EMPLOYER & RECRUITER
-- =====================================================
INSERT INTO employers (id, user_id, company_id, designation)
VALUES
    (uuid_generate_v4(), 'dddddddd-dddd-dddd-dddd-dddddddddddd', '99999999-9999-9999-9999-999999999999', 'Engineering Manager')
    ON CONFLICT DO NOTHING;

INSERT INTO recruiters (id, user_id, specialization)
VALUES
    (uuid_generate_v4(), 'cccccccc-cccc-cccc-cccc-cccccccccccc', 'Java Backend')
    ON CONFLICT DO NOTHING;

-- =====================================================
-- JOBS
-- =====================================================
INSERT INTO jobs (
    id, company_id, created_by, title, description,
    experience_min, experience_max,
    salary_min, salary_max,
    location, job_type, status
)
VALUES
    (
        '12121212-1212-1212-1212-121212121212',
        '99999999-9999-9999-9999-999999999999',
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        'Senior Java Backend Engineer',
        'Spring Boot, Microservices, PostgreSQL',
        5, 8,
        2000000, 3500000,
        'Bangalore',
        'HYBRID',
        'OPEN'
    )
    ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- JOB SKILLS
-- =====================================================
INSERT INTO job_skills (id, job_id, skill)
VALUES
    (uuid_generate_v4(), '12121212-1212-1212-1212-121212121212', 'Java'),
    (uuid_generate_v4(), '12121212-1212-1212-1212-121212121212', 'Spring Boot'),
    (uuid_generate_v4(), '12121212-1212-1212-1212-121212121212', 'PostgreSQL')
    ON CONFLICT DO NOTHING;

-- =====================================================
-- CANDIDATE
-- =====================================================
INSERT INTO candidates (
    id, user_id, total_experience,
    current_ctc, expected_ctc,
    notice_period, resume_url
)
VALUES
    (
        '34343434-3434-3434-3434-343434343434',
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        6,
        1800000,
        2800000,
        30,
        'https://cdn.talentops.io/resume/pramod.pdf'
    )
    ON CONFLICT (id) DO NOTHING;

-- =====================================================
-- CANDIDATE SKILLS
-- =====================================================
INSERT INTO candidate_skills (id, candidate_id, skill)
VALUES
    (uuid_generate_v4(), '34343434-3434-3434-3434-343434343434', 'Java'),
    (uuid_generate_v4(), '34343434-3434-3434-3434-343434343434', 'Spring Boot'),
    (uuid_generate_v4(), '34343434-3434-3434-3434-343434343434', 'Microservices')
    ON CONFLICT DO NOTHING;

-- =====================================================
-- APPLICATION
-- =====================================================
INSERT INTO applications (
    id,
    job_id,
    candidate_id,
    user_id,
    status,
    current_stage,
    created_at
)
VALUES (
           '56565656-5656-5656-5656-565656565656',
           '12121212-1212-1212-1212-121212121212',
           '34343434-3434-3434-3434-343434343434',
           'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', -- candidate user_id
           'APPLIED',
           'SCREENING',
           now()
       )
    ON CONFLICT (id) DO NOTHING;


-- =====================================================
-- INTERVIEW
-- =====================================================
INSERT INTO interviews (
    id, application_id, interview_type,
    scheduled_at, interviewer, status
)
VALUES
    (
        uuid_generate_v4(),
        '56565656-5656-5656-5656-565656565656',
        'TECH',
        now() + interval '2 days',
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        'SCHEDULED'
    )
    ON CONFLICT DO NOTHING;

-- =====================================================
-- NOTIFICATIONS
-- =====================================================
INSERT INTO notifications (
    id, user_id, event_type,
    title, message, is_read
)
VALUES
    (
        uuid_generate_v4(),
        'eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee',
        'APPLICATION_RECEIVED',
        'Application Submitted',
        'Your application for Senior Java Backend Engineer has been received.',
        false
    )
    ON CONFLICT DO NOTHING;

-- =====================================================
-- SYSTEM CONFIG
-- =====================================================
INSERT INTO system_configs (id, config_key, config_value)
VALUES
    (uuid_generate_v4(), 'PIPELINE_STAGES', 'APPLIED,SCREENING,INTERVIEW,OFFER,HIRED,REJECTED')
    ON CONFLICT (config_key) DO NOTHING;

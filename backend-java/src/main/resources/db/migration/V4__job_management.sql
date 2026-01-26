CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'jobs' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.jobs (
                             id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                             company_id UUID REFERENCES public.companies(id),
                             created_by UUID REFERENCES public.users(id),
                             title VARCHAR(255),
                             description TEXT,
                             experience_min INT,
                             experience_max INT,
                             salary_min NUMERIC,
                             salary_max NUMERIC,
                             location VARCHAR(100),
                             job_type VARCHAR(50),
                             status VARCHAR(30),
                             created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'job_skills' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.job_skills (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   job_id UUID REFERENCES public.jobs(id) ON DELETE CASCADE,
                                   skill VARCHAR(100)
);
END IF;
END
$$;

-- DO $$
-- BEGIN
--   IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='jobs' AND indexname='idx_jobs_status') THEN
-- CREATE INDEX idx_jobs_status ON public.jobs(status);
-- END IF;
--
--   IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='jobs' AND indexname='idx_jobs_company') THEN
-- CREATE INDEX idx_jobs_company ON public.jobs(company_id);
-- END IF;
-- END
-- $$;
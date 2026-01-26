
-- This migration adds tables to manage the application pipeline for job candidates.
--sql

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
                WHERE c.relname = 'applications' AND n.nspname = 'public') THEN
CREATE TABLE applications (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              job_id UUID REFERENCES jobs(id),
                              candidate_id UUID REFERENCES candidates(id),
                              current_stage VARCHAR(50),
                              applied_at TIMESTAMP DEFAULT now(),
                              updated_at TIMESTAMP DEFAULT now(),
                              UNIQUE (job_id, candidate_id)
);
END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'application_notes' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.application_notes (
                                   id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                   application_id UUID REFERENCES applications(id) ON DELETE CASCADE,
                                   created_by UUID REFERENCES users(id),
                                   note TEXT,
                                   created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='applications' AND indexname='idx_application_stage') THEN
CREATE INDEX idx_application_stage ON applications(current_stage);
END IF;
END
$$;

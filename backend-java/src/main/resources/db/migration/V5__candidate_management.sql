CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'candidates' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.candidates (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            user_id UUID REFERENCES users(id),
                            total_experience INT,
                            current_ctc NUMERIC,
                            expected_ctc NUMERIC,
                            notice_period INT,
                            resume_url TEXT,
                            created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'candidate_skills' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.candidate_skills (
                                  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                  candidate_id UUID REFERENCES candidates(id) ON DELETE CASCADE,
                                  skill VARCHAR(100)
);
END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='candidates' AND indexname='idx_candidate_user') THEN
CREATE INDEX idx_candidate_user ON candidates(user_id);
END IF;
END
$$;
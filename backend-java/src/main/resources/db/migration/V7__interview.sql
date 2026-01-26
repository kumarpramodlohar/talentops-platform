CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
                WHERE c.relname = 'interviews' AND n.nspname = 'public') THEN

CREATE TABLE public.interviews (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            application_id UUID REFERENCES applications(id),
                            interview_type VARCHAR(50),
                            scheduled_at TIMESTAMP,
                            interviewer UUID REFERENCES users(id),
                            status VARCHAR(30),
                            created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
                WHERE c.relname = 'interview_feedback' AND n.nspname = 'public') THEN

CREATE TABLE public.interview_feedback (
                                    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                    interview_id UUID REFERENCES interviews(id) ON DELETE CASCADE,
                                    feedback TEXT,
                                    rating INT CHECK (rating BETWEEN 1 AND 5),
                                    created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;
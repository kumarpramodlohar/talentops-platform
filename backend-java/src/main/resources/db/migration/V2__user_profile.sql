-- sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'user_profiles' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.user_profiles (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               user_id UUID UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                               first_name VARCHAR(100),
                               last_name VARCHAR(100),
                               phone VARCHAR(20),
                               profile_image_url TEXT,
                               linkedin_url TEXT,
                               github_url TEXT,
                               created_at TIMESTAMP DEFAULT now(),
                               updated_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

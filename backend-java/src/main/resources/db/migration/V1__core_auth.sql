-- sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'roles' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.roles (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              name VARCHAR(50) UNIQUE NOT NULL,
                              description TEXT
);
END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'users' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.users (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              email VARCHAR(255) UNIQUE NOT NULL,
                              password_hash TEXT NOT NULL,
                              role_id UUID REFERENCES public.roles(id),
                              status VARCHAR(30) DEFAULT 'ACTIVE',
                              last_login TIMESTAMP DEFAULT now(),
                              updated_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'refresh_tokens' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.refresh_tokens (
                                       id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                       user_id UUID REFERENCES public.users(id) ON DELETE CASCADE,
                                       token TEXT UNIQUE NOT NULL,
                                       expires_at TIMESTAMP NOT NULL,
                                       revoked BOOLEAN DEFAULT FALSE,
                                       created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

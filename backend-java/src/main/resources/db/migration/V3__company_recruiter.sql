-- sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'companies' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.companies (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           name VARCHAR(255) NOT NULL,
                           industry VARCHAR(100),
                           website TEXT,
                           size VARCHAR(50),
                           created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;


DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'employers' AND n.nspname = 'public'
  ) THEN
CREATE TABLE public.employers (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           user_id UUID REFERENCES users(id),
                           company_id UUID REFERENCES companies(id),
                           designation VARCHAR(100),
                           created_at TIMESTAMP DEFAULT now()
);

END IF;
END
$$;

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'recruiters' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.recruiters (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            user_id UUID REFERENCES users(id),
                            specialization VARCHAR(100),
                            created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

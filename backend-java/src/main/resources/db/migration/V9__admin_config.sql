--sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'system_configs' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.system_configs (
                                id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                config_key VARCHAR(100) UNIQUE,
                                config_value TEXT,
                                updated_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;
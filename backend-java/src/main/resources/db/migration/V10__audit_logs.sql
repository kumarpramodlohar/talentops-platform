--sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'audit_logs' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.audit_logs (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            user_id UUID REFERENCES users(id),
                            action VARCHAR(100),
                            entity VARCHAR(100),
                            entity_id UUID,
                            ip_address VARCHAR(50),
                            created_at TIMESTAMP DEFAULT now()
);
END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='audit_logs' AND indexname='idx_audit_user') THEN
CREATE INDEX idx_audit_user ON audit_logs(user_id);
END IF;
END
$$;
DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='audit_logs' AND indexname='idx_audit_entity') THEN
CREATE INDEX idx_audit_entity ON audit_logs(entity);
END IF;
END
$$;

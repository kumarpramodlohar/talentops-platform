--sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_class c JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE c.relname = 'notifications' AND n.nspname = 'public'
  ) THEN

CREATE TABLE public.notifications (
                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                               user_id UUID REFERENCES users(id),
                               event_type VARCHAR(50),
                               title VARCHAR(255),
                               message TEXT,
                               metadata JSONB,
                               is_read BOOLEAN DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT now()
);

END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='notifications' AND indexname='idx_notifications_user') THEN
CREATE INDEX idx_notifications_user ON notifications(user_id);
END IF;
END
$$;

DO $$
BEGIN
IF NOT EXISTS (SELECT 1 FROM pg_indexes WHERE tablename='notifications' AND indexname='idx_notifications_read') THEN
CREATE INDEX idx_notifications_read ON notifications(is_read);
END IF;
END
$$;

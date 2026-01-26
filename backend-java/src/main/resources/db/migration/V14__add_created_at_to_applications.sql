ALTER TABLE public.applications
    ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT now();

-- Options to backfill existing records can be added here if needed
ALTER TABLE public.applications
    ALTER COLUMN created_at SET NOT NULL;
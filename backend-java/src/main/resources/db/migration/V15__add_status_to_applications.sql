 -- Add status column to applications table
ALTER TABLE public.applications ADD COLUMN IF NOT EXISTS status VARCHAR(30) ;

-- Optionally, set a default value for existing records
UPDATE public.applications SET status = 'CREATED' WHERE status IS NULL;

-- Optionally, set NOT NULL constraint if required
ALTER TABLE public.applications ALTER COLUMN status SET NOT NULL;
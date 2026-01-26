-- 2. Backfill existing rows
-- Pick ONE strategy:

-- Option A: link to an existing system user
UPDATE public.applications
SET user_id = (
    SELECT id FROM public.users ORDER BY created_at LIMIT 1
    )
WHERE user_id IS NULL;

-- Option B: create a SYSTEM user and reference it
-- (best for prod)

-- 4. Enforce NOT NULL
ALTER TABLE public.applications
    ALTER COLUMN user_id SET NOT NULL;
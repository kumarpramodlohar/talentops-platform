-- 1. Add column (nullable first)
ALTER TABLE public.applications
    ADD COLUMN IF NOT EXISTS user_id UUID;



-- 3. Add FK constraint
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_constraint
        WHERE conname = 'fk_applications_user'
    ) THEN
ALTER TABLE public.applications
    ADD CONSTRAINT fk_applications_user
        FOREIGN KEY (user_id)
            REFERENCES public.users(id);
END IF;
END$$;



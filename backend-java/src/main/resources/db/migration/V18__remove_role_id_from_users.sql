-- 1. Drop FK constraint (if exists)
DO $$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE constraint_name = 'fk_users_role'
    ) THEN
ALTER TABLE users DROP CONSTRAINT fk_users_role;
END IF;
END $$;

-- 2. Drop role_id column
ALTER TABLE users
DROP COLUMN IF EXISTS role_id;

-- 3. Ensure role column exists
ALTER TABLE users
    ADD COLUMN IF NOT EXISTS role VARCHAR(30);

-- 4. Backfill role values (important for existing rows)
UPDATE users
SET role = 'ADMIN'
WHERE role IS NULL;

-- 5. Enforce NOT NULL
ALTER TABLE users
    ALTER COLUMN role SET NOT NULL;

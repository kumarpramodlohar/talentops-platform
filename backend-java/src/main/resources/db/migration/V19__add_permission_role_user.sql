--sql
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.permissions (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              code VARCHAR(100) UNIQUE NOT NULL,
                              description TEXT,
                              created_at TIMESTAMP DEFAULT now()
);



CREATE TABLE public.role_permissions (
                              role_code VARCHAR(50) not null,
                              permission_code VARCHAR(100) not null,
                              PRIMARY KEY (role_code, permission_code)
);

CREATE TABLE public.users_permissions (
                              user_id UUID not null,
                              permission_code VARCHAR(100) not null,
                              PRIMARY KEY (user_id, permission_code)
);

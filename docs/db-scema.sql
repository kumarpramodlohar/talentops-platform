CREATE TABLE users (
  id UUID PRIMARY KEY,
  email VARCHAR(255) UNIQUE NOT NULL,
  role VARCHAR(30) NOT NULL,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE jobs (
  id UUID PRIMARY KEY,
  title VARCHAR(200),
  description TEXT,
  created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE applications (
  id UUID PRIMARY KEY,
  user_id UUID,
  job_id UUID,
  status VARCHAR(30),
  created_at TIMESTAMP DEFAULT now()
);

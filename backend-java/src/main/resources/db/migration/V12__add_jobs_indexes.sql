-- Index for company filter
CREATE INDEX IF NOT EXISTS idx_jobs_company
    ON public.jobs(company_id);

-- Index for status filter
CREATE INDEX IF NOT EXISTS idx_jobs_status
    ON public.jobs(status);

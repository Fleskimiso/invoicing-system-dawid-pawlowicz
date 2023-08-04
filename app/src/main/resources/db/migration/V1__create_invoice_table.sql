CREATE TABLE public.invoice
(
    id bigserial PRIMARY KEY NOT NULL,
    issue_date date NOT NULL
);
ALTER TABLE invoice
RENAME COLUMN issue_date TO date
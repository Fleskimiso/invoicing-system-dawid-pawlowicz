CREATE TABLE public.car
(
    id                  bigserial             NOT NULL,
    registration_number character varying(20) NOT NULL,
    personal_use       boolean               NOT NULL DEFAULT false,
    PRIMARY KEY (id)
);
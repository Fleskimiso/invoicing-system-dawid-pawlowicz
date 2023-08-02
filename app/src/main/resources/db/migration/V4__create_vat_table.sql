CREATE TABLE public.vat
(
    id   bigserial NOT NULL,
    name character varying(20),
    rate numeric(3, 2) NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('21', 0.23),
       ('8', 0.08),
       ('7', 0.07),
       ('5', 0.05),
       ('0', 0.00),
       ('ZW', 0.00);
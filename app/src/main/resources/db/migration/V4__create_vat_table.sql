CREATE TABLE public.vat
(
    id   bigserial NOT NULL,
    name character varying(20),
    rate numeric(3, 2) NOT NULL,
    PRIMARY KEY (id)
);

insert into public.vat (name, rate)
values ('VAT_21', 0.21),
       ('VAT_8', 0.08),
       ('VAT_7', 0.07),
       ('VAT_5', 0.05),
       ('VAT_0', 0.00),
       ('VAT_ZW', 0.00);

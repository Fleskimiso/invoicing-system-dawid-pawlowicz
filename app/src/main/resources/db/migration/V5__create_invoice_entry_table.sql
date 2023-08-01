CREATE TABLE public.invoice_entry
(
    id                     bigserial      NOT NULL,
    description            character varying(50),
    quantity               numeric(10, 2) NOT NULL DEFAULT 1,
    price                  numeric(10, 2) NOT NULL,
    vat_value              numeric(10, 2) NOT NULL,
    vat_rate               bigint         NOT NULL,
    expense_related_to_car bigint,
    PRIMARY KEY (id)
);
ALTER TABLE public.invoice_entry
    ADD CONSTRAINT vat_rate_fk FOREIGN KEY (vat_rate)
        REFERENCES public.vat (id);

ALTER TABLE public.invoice_entry
    ADD CONSTRAINT car_fk FOREIGN KEY (expense_related_to_car)
        REFERENCES public.car (id);
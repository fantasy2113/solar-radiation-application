-- Table: public.radiation

-- DROP TABLE public.radiation;

CREATE TABLE public.radiation
(
    radiation_type character varying(8) COLLATE pg_catalog."default" NOT NULL,
    radiation_date integer NOT NULL,
    gkr_min integer NOT NULL,
    gkr_max integer NOT NULL,
    gkh_min integer NOT NULL,
    gkh_max integer NOT NULL,
    radiation_value numeric(5, 2) NOT NULL,
    CONSTRAINT radiation_pkey PRIMARY KEY (radiation_type, radiation_date, gkr_min, gkr_max, gkh_min, gkh_max)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.radiation
    OWNER to postgres;
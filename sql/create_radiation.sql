-- Table: public.radiation

DROP TABLE public.radiation;

CREATE TABLE public.radiation
(
    radiation_type  character varying(7) COLLATE pg_catalog."default" NOT NULL,
    radiation_date  NUMERIC(6)                                        NOT NULL,
    gkr_min         NUMERIC(7)                                        NOT NULL,
    gkr_max         NUMERIC(7)                                        NOT NULL,
    gkh_min         NUMERIC(7)                                        NOT NULL,
    gkh_max         NUMERIC(7)                                        NOT NULL,
    radiation_value NUMERIC(5, 2)                                     NOT NULL,
    CONSTRAINT radiation_pkey PRIMARY KEY (radiation_type, radiation_date, gkr_min, gkr_max, gkh_min, gkh_max)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public.radiation
    OWNER to postgres;
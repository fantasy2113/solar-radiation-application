-- Table: public.radiation

-- DROP TABLE public.radiation;

CREATE TABLE public.radiation
(
    typ character varying(10) COLLATE pg_catalog."default" NOT NULL,
    date integer NOT NULL,
    x_min integer NOT NULL,
    x_max integer NOT NULL,
    y_min integer NOT NULL,
    y_max integer NOT NULL,
    value real NOT NULL,
    CONSTRAINT radiation_pkey PRIMARY KEY (typ, date, x_min, x_max, y_min, y_max)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.radiation
    OWNER to ntdzcywnkjukss;
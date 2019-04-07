-- Table: public.radiation_jsb

-- DROP TABLE public.radiation_jsb;

CREATE TABLE public.radiation_jsb
(
    jsb jsonb NOT NULL,
    CONSTRAINT radiation_jsonb_pkey PRIMARY KEY (jsb)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.radiation_jsb
    OWNER to postgres;

-- Index: idx_btree_radiation

-- DROP INDEX public.idx_btree_radiation;

CREATE INDEX idx_btree_radiation
    ON public.radiation_jsb USING gin
    (jsb jsonb_path_ops)
    TABLESPACE pg_default;
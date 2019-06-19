-- Table: public."irradiation"

-- DROP TABLE public."irradiation";

CREATE TABLE public."irradiation"
(
    db_cache jsonb NOT NULL,
    CONSTRAINT "irradiation_pkey" PRIMARY KEY (db_cache)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public."irradiation"
    OWNER to postgres;

CREATE INDEX idxginkeys ON irradiation USING gin ((db_cache -> 'key'));
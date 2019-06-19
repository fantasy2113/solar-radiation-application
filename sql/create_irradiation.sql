-- Table: public."irradiation"

DROP TABLE public."irradiation";

CREATE TABLE public."irradiation"
(
    id       integer NOT NULL,
    db_cache jsonb   NOT NULL,
    CONSTRAINT irradiation_pkey PRIMARY KEY (id)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public."irradiation"
    OWNER to postgres;

CREATE INDEX db_cache_gin_key ON irradiation USING gin ((db_cache -> 'key'));
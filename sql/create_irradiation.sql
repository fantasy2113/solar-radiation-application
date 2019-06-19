-- Table: public."irradiation"

DROP TABLE public."irradiation";

CREATE TABLE public."irradiation"
(
    id       bigserial NOT NULL UNIQUE,
    db_cache jsonb     NOT NULL,
    unique (db_cache)
)
    WITH (
        OIDS = FALSE
    )
    TABLESPACE pg_default;

ALTER TABLE public."irradiation"
    OWNER to postgres;

CREATE INDEX idxginkeys ON irradiation USING gin ((db_cache -> 'key'));
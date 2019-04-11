-- Table: public."user"

-- DROP TABLE public."user";

CREATE TABLE public."user"
(
    id integer NOT NULL DEFAULT nextval('user_id_seq'::regclass),
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    login character varying(12) COLLATE pg_catalog."default" NOT NULL,
    is_active boolean NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id, login),
    CONSTRAINT login UNIQUE (login)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."user"
    OWNER to postgres;
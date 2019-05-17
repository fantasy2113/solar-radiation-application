-- Table: public."user"

-- DROP TABLE public."user";

CREATE TABLE public."user"
(
    id bigserial NOT NULL UNIQUE,
    username character varying(60) COLLATE pg_catalog."default" NOT NULL,
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    is_active boolean NOT NULL,
    last_login timestamp without time zone NOT NULL,
    created timestamp without time zone,
    modified timestamp without time zone,
    CONSTRAINT user_pkey_ PRIMARY KEY (id, username),
    CONSTRAINT user_username_key_ UNIQUE (username)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public."user"
    OWNER to postgres;
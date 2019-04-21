-- Table: public.users

-- DROP TABLE public.users;

CREATE TABLE public.users
(
    id bigserial NOT NULL UNIQUE,
    password character varying(60) COLLATE pg_catalog."default" NOT NULL,
    login character varying(60) COLLATE pg_catalog."default" NOT NULL,
    is_active boolean NOT NULL,
    CONSTRAINT user_pkey PRIMARY KEY (id, login),
    CONSTRAINT unique_login UNIQUE (login)

)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.users
    OWNER to postgres;
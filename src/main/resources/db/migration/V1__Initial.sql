CREATE TABLE irradiation
(
    id       integer NOT NULL,
    db_cache jsonb   NOT NULL,
    PRIMARY KEY (id)
);

CREATE
    INDEX db_cache_gin_key ON irradiation USING gin ((db_cache -> 'key'));

CREATE TABLE radiation
(
    radiation_type  character varying(7) NOT NULL,
    radiation_date  NUMERIC(6)           NOT NULL,
    gkr_min         NUMERIC(7)           NOT NULL,
    gkr_max         NUMERIC(7)           NOT NULL,
    gkh_min         NUMERIC(7)           NOT NULL,
    gkh_max         NUMERIC(7)           NOT NULL,
    radiation_value NUMERIC(5, 2)        NOT NULL,
    PRIMARY KEY (radiation_type, radiation_date, gkr_min, gkr_max, gkh_min, gkh_max)
);

CREATE TABLE user_tab
(
    id         bigserial                   NOT NULL UNIQUE,
    username   character varying(60)       NOT NULL,
    password   character varying(60)       NOT NULL,
    is_active  boolean                     NOT NULL,
    last_login timestamp without time zone NOT NULL,
    created    timestamp without time zone NOT NULL,
    modified   timestamp without time zone NOT NULL,
    PRIMARY KEY (id, username),
    UNIQUE (username)
);